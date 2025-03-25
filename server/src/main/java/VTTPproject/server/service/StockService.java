package VTTPproject.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import VTTPproject.server.model.ActivityRatio;
import VTTPproject.server.model.LiquidityRatio;
import VTTPproject.server.model.MiscItems;
import VTTPproject.server.model.ProfitabilityRatio;
import VTTPproject.server.model.SolvencyRatio;
import VTTPproject.server.model.Stock;
import VTTPproject.server.model.StockSummary;
import VTTPproject.server.model.ValuationRatio;
import VTTPproject.server.repository.StockRepository;
import VTTPproject.server.utils.Utils;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.json.JsonObject;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Value("${stock.api.key}")
    private String stockApi;

    private final Timer stockSearchTimer;
    private final Counter stockSearchCounter;

    public static final String GET_URL = "https://financialmodelingprep.com/api/v3/";


    @Autowired
    public StockService(MeterRegistry meterRegistry) {
        // Metric 2: Timer for stock search duration
        this.stockSearchTimer = Timer.builder("app_stock_search_duration_metric2")
                .description("Time taken for stock searches in seconds")
                .register(meterRegistry);

        // Metric 3: Counter for stock search usage
        this.stockSearchCounter = Counter.builder("app_stock_search_count_metric3")
                .description("Total number of stock searches performed")
                .register(meterRegistry);
    }



    public JsonObject searchDetails(String ticker) throws NullPointerException, IndexOutOfBoundsException{
        // Record the time taken for the search through database/api call
        return stockSearchTimer.record(() -> { //Metric 2
            // Increment the stock search counter
            stockSearchCounter.increment(); //Metric 3

            //try grab from database first
            Optional<JsonObject> stockJson = stockRepository.findStockByTicker(ticker);
            if (stockJson.isEmpty()) {
                //if empty, grab from api call
                Stock stock = searchAPI(ticker);

                JsonObject mi = MiscItems.buildMiscItemsJson(stock.getMi());
                JsonObject ar = ActivityRatio.buildActivityRatioJson(stock.getAr());
                JsonObject lr = LiquidityRatio.buildLiquidityRatioJson(stock.getLr());
                JsonObject pr = ProfitabilityRatio.buildProfitabilityRatioJson(stock.getPr());
                JsonObject sr = SolvencyRatio.buildSolvencyRatioJson(stock.getSr());
                JsonObject vr = ValuationRatio.buildValuationRatioJson(stock.getVr());
                return Stock.toStockJson(mi, ar, lr, pr, sr, vr);
            }
            //returning json from database
            System.out.println("returning json from database \n\n\n");
            return stockJson.get();
        });     
    }

    @Transactional
    public String saveStock(Stock stock, String email){
        JsonObject mi = MiscItems.buildMiscItemsJson(stock.getMi());
        JsonObject ar = ActivityRatio.buildActivityRatioJson(stock.getAr());
        JsonObject lr = LiquidityRatio.buildLiquidityRatioJson(stock.getLr());
        JsonObject pr = ProfitabilityRatio.buildProfitabilityRatioJson(stock.getPr());
        JsonObject sr = SolvencyRatio.buildSolvencyRatioJson(stock.getSr());
        JsonObject vr = ValuationRatio.buildValuationRatioJson(stock.getVr());
        JsonObject stockJson = Stock.toStockJson(mi, ar, lr, pr, sr, vr);

        stockRepository.updateSQL(email, mi.getString("symbol"), mi.getString("companyName"), true);

        return stockRepository.insertStockJson(stockJson);
    }

    public void deleteStock(String symbol, String email){
        stockRepository.updateSQL(email, symbol, null, false);
    }


    public Stock searchAPI(String ticker) throws NullPointerException, IndexOutOfBoundsException{
        String companyUrl = apiCallCompany(ticker); 
        String companyPayload = getPayload(companyUrl); //will be eempty jsonarray if ticker does not exist

        String keyRatiosUrl = apiCallKeyRatios(ticker);
        String keyRatiosPayload = getPayload(keyRatiosUrl); //will be eempty jsonarray if ticker does not exist

        String additionalUrl = apiCallAdditionalRatios(ticker);
        String additionalPayload = getPayload(additionalUrl);

        JsonObject companyObj = Utils.stockInfoParser(companyPayload); //grab 1st json object
        JsonObject keyRatiosObj = Utils.stockInfoParser(keyRatiosPayload); //grab 1st json object
        JsonObject additionalObj = Utils.stockInfoParser(additionalPayload); //grab 1st json object

        MiscItems mi = MiscItems.toMiscObj(companyObj);
        ActivityRatio ar = ActivityRatio.toActObj(keyRatiosObj, additionalObj);
        LiquidityRatio lr = LiquidityRatio.toLiquidObj(additionalObj);
        SolvencyRatio sr = SolvencyRatio.toSolvObj(additionalObj);
        ProfitabilityRatio pr = ProfitabilityRatio.toProfObj(additionalObj);
        ValuationRatio vr = ValuationRatio.toValObj(additionalObj);
        vr.setSustainableGrowthRate(vr.getRetentionRate() * pr.getReturnOnEquityTTM());

        Stock stock = new Stock();
        stock.setMi(mi);
        stock.setAr(ar);
        stock.setLr(lr);
        stock.setSr(sr);
        stock.setPr(pr);
        stock.setVr(vr);

        return stock;
    }

    public String getPayload(String callUrl){
        RequestEntity<Void> req = RequestEntity //nothing in the request's body
            .get(callUrl)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String payload = resp.getBody();
        return payload;
    }


    public String apiCallCompany(String ticker){
        StringBuilder sb = new StringBuilder(GET_URL);
        sb.append("profile/");
        sb.append(ticker.toUpperCase());

        String url = UriComponentsBuilder
            .fromUriString(sb.toString())
            .queryParam("apikey", stockApi)
            .toUriString();

        return url;
    }

    public String apiCallKeyRatios(String ticker){
        StringBuilder sb = new StringBuilder(GET_URL);
        sb.append("key-metrics/");
        sb.append(ticker.toUpperCase());

        String url = UriComponentsBuilder
            .fromUriString(sb.toString())
            .queryParam("period", "annual")
            .queryParam("apikey", stockApi)
            .toUriString();

        return url;
    }

    public String apiCallAdditionalRatios(String ticker){
        StringBuilder sb = new StringBuilder(GET_URL);
        sb.append("ratios-ttm/");
        sb.append(ticker.toUpperCase());

        String url = UriComponentsBuilder
            .fromUriString(sb.toString())
            .queryParam("apikey", stockApi)
            .toUriString();

        return url;
    }

    
    public List<StockSummary> getPortfolio(String email){
        return stockRepository.getPortfolio(email);
    }

}
