package VTTPproject.server.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//filter will run once per request
//validate tokens in incoming requests
//extrract user information and set authentication

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Value("${jwt.secret.key}")
    private String secretKey;

    //filter logic, checks for valid token in auth http req header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        String header = request.getHeader("Authorization");        
        if(header == null || !header.startsWith("Bearer")){
            System.out.println("No Bearer token found, REJECTING REQUEST"); //Debug
            chain.doFilter(request, response);
            return; //continue without auth
        }

        //extract token after "bearer"
        String token = header.substring(7);
        try{
            Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token);

            //retrieves emails from token
            String email = claims.getPayload().getSubject();
            System.out.println("Token validated, email: " + email); // Debug success
            //new auth object without roles
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(JwtException e){
            System.out.println("Token validation failed: " + e.getMessage()); // Debug failure
            response.sendError(401, "Invalid token");
            return;
        }
        //pass http request to next filter after auth
        chain.doFilter(request, response);

    }

}
