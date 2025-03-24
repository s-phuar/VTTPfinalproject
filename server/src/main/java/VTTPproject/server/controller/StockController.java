package VTTPproject.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import VTTPproject.server.model.Stock;
import VTTPproject.server.model.StockSummary;
import VTTPproject.server.service.StockService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

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
                JsonObject stockJson = stockService.searchDetails(ticker);
        
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

    @PostMapping(path = "/api/save/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveStock(@RequestBody Stock stock, @PathVariable String email){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            System.out.println("triggered save next");
            String symbol = stockService.saveStock(stock, email);

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

    @DeleteMapping(path = "/api/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStock(@RequestParam String symbol, @RequestParam String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            System.out.println("triggered dashboard next");
            stockService.deleteStock(symbol, email); 
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Deleted from portfolio: " + symbol)
                .build();

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp.toString());
        }else{
            System.out.println("triggered dashboard error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
        }
    }



    @GetMapping(path = "/api/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pullPortfolio(@RequestParam String email){
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            List<StockSummary> stockSumList = stockService.getPortfolio(email);
            
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (StockSummary stock : stockSumList) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("symbol", stock.getTicker())
                    .add("companyName", stock.getCompanyName());
                arrayBuilder.add(objectBuilder);
            }
            String jsonString = arrayBuilder.build().toString();

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonString);
        }else{
            System.out.println("triggered dashboard error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
            }

    }
    

    
}
