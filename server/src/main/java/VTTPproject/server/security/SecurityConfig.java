package VTTPproject.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;


//define access rules and integrate JWT
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)throws Exception{
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .anonymous(anonymous -> anonymous.disable())
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/api/creation", "/api/login", "/api/logout", "/api/search", "/api/save", "/api/portfolio", "/api/delete", "/api/donate").permitAll()
                        // actuator/prometeus for admin only
                        .requestMatchers("/actuator/prometheus").access((authentication, context) -> {
                            Authentication auth = authentication.get();
                            if (auth == null || !auth.isAuthenticated()) {
                                System.out.println("something failed ");
                                return new AuthorizationDecision(false);
                            }
                            String email = auth.getName();
                            System.out.println("made it here" + email);
                            return new AuthorizationDecision("samuel.phuar@gmail.com".equals(email));
                        })
                        .anyRequest()
                        .authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(httpBasic -> httpBasic.realmName("Prometheus Metrics"))
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
    

    
}