package VTTPproject.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class PaymentController {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;


    @PostMapping(path = "/api/donate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> donate(@RequestParam String token, @RequestParam int amount){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){

            Stripe.apiKey = stripeSecretKey; //assign stripe api
            try{
                Map<String, Object> chargeParams = new HashMap<>();
                chargeParams.put("amount", amount); //in ce
                chargeParams.put("currency", "sgd");
                chargeParams.put("source", token);
        
                Charge charge = Charge.create(chargeParams);
                JsonObject resp = Json.createObjectBuilder()
                    .add("message", "Donation success: " + charge.getId())
                    .build();
        
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.toString());
            }catch(StripeException e){
                JsonObject resp = Json.createObjectBuilder()
                    .add("message", "Donation failed: " + e.getMessage())
                    .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.toString());
            }
        }else{
            System.out.println("triggered dashboard error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
            }





        

    }


}
