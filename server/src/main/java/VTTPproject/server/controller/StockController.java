package VTTPproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import VTTPproject.server.model.ActivityRatio;
import VTTPproject.server.model.LiquidityRatio;
import VTTPproject.server.model.MiscItems;
import VTTPproject.server.model.ProfitabilityRatio;
import VTTPproject.server.model.SolvencyRatio;
import VTTPproject.server.model.Stock;
import VTTPproject.server.model.ValuationRatio;
import VTTPproject.server.service.StockService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping(path = "/api/search/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDetails(@PathVariable String ticker){

        System.out.println("You reached stock search...");
        //angular http interceptor sends over a token which basically contains the email's hash
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            try{
                System.out.println("triggered search next");
                Stock stock = stockService.searchDetails(ticker);
    
                JsonObject mi = MiscItems.buildMiscItemsJson(stock.getMi());
                JsonObject ar = ActivityRatio.buildActivityRatioJson(stock.getAr());
                JsonObject lr = LiquidityRatio.buildLiquidityRatioJson(stock.getLr());
                JsonObject pr = ProfitabilityRatio.buildProfitabilityRatioJson(stock.getPr());
                JsonObject sr = SolvencyRatio.buildSolvencyRatioJson(stock.getSr());
                JsonObject vr = ValuationRatio.buildValuationRatioJson(stock.getVr());
                JsonObject stockJson = Stock.toStockJson(mi, ar, lr, pr, sr, vr);
    
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(stockJson.toString());
    
            }catch(NullPointerException | IndexOutOfBoundsException ex){
    
                JsonObject error = Json.createObjectBuilder()
                    .add("message", "ticker not found")
                    .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
            }
        }else{
            System.out.println("triggered search error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
            }
    }

    @PostMapping(path = "/api/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveStock(@RequestBody Stock stock){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            System.out.println("triggered save next");

            String symbol = stockService.saveStock(stock);

            JsonObject resp = Json.createObjectBuilder()
                .add("message", "saved " + symbol)
                .build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp.toString());
        }else{
            System.out.println("triggered save error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
            }
    }


    
}
