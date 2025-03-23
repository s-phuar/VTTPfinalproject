package VTTPproject.server.service;

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
import VTTPproject.server.model.ValuationRatio;
import VTTPproject.server.repository.StockRepository;
import VTTPproject.server.utils.Utils;
import jakarta.json.JsonObject;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Value("${stock.api.key}")
    private String stockApi;

    public static final String GET_URL = "https://financialmodelingprep.com/api/v3/";

    public Stock searchDetails(String ticker) throws NullPointerException{
        // //try grab from database
        // Stock stock = stockRepository.findStockByTicker(ticker)


        //if not grab from api call
        Stock stock = searchAPI(ticker);
        
        return stock;
    }

    @Transactional
    public String saveStock(Stock stock){
        JsonObject mi = MiscItems.buildMiscItemsJson(stock.getMi());
        JsonObject ar = ActivityRatio.buildActivityRatioJson(stock.getAr());
        JsonObject lr = LiquidityRatio.buildLiquidityRatioJson(stock.getLr());
        JsonObject pr = ProfitabilityRatio.buildProfitabilityRatioJson(stock.getPr());
        JsonObject sr = SolvencyRatio.buildSolvencyRatioJson(stock.getSr());
        JsonObject vr = ValuationRatio.buildValuationRatioJson(stock.getVr());
        JsonObject stockJson = Stock.toStockJson(mi, ar, lr, pr, sr, vr);

        //save to SQL
        

        return stockRepository.insertStockJson(stockJson);
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

    
}
