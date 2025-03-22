package VTTPproject.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import VTTPproject.server.service.EmailService;
import VTTPproject.server.service.LoginService;
import VTTPproject.server.service.TelegramService;
import VTTPproject.server.utils.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class LoginController {
    
    @Autowired
    private LoginService loginService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private EmailService emailService;

    //angular will call these endpoints expected responseentities

    @PostMapping(path = "/api/creation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> creation(@RequestBody String payload){
        //expecting username, email, password
        JsonObject req = Utils.strToJson(payload); 

        String name = req.getString("name");
        String email = req.getString("email");
        String pw = req.getString("password");

        //check whether database already contains details
        if (loginService.getAllEmails().contains(email)) {
            // return to angular, cant use this email
            JsonObject resp = Json.createObjectBuilder()
                .add("name", name)
                .add("message", "You cannot use this email, account already exists")
                .build();
            return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED).body(resp.toString());
        }

        //checks for confirm password matching is done client side

        //create account to store in database
        loginService.createNewAccount(name, email, pw);
        //send welcome email
        emailService.welcomeEmail(email, name);

        JsonObject resp = Json.createObjectBuilder()
            .add("name", name)
            .add("message", "Account Created!")
            .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp.toString());
    }


    @PostMapping(path = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody String payload){
        JsonObject req = Utils.strToJson(payload); 

        String email = req.getString("email");
        String pw = req.getString("password");

        //check whether email exists in database
        if (!loginService.getAllEmails().contains(email)) {
            // return to angular, login used does not exist
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Invalid email")
                .build();
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED).body(resp.toString());
        }

        //check payload password against database
        //issue new fresh token every login
        String storedPw = loginService.getPasswordByEmail(email);
        if(storedPw !=null && storedPw.equals(pw)){
            // telegramService.sendMessage("You logged in mate"); //testing tele**************************8
            String token = Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)                
                .compact();
            JsonObject resp = Json.createObjectBuilder()
                .add("token", token)
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(resp.toString());
        }else{
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Invalid password")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
        }
    }


    @GetMapping(path = "/api/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(){
        System.out.println("You reached logout...");
        //angular http interceptor sends over a token which basically contains the email's hash
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            System.out.println("triggered logout next");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Logged out successfully")
                .build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp.toString());
        }else{
            System.out.println("triggered logout error");
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Please login first")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
            }
    }
    




}
