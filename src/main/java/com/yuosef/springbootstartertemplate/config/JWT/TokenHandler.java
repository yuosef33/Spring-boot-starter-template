package com.yuosef.springbootstartertemplate.config.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import org.slf4j.Logger;

@Component
public class TokenHandler {

    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

       @Value("${application.security.jwt.secret-key}")
       private String secret ;
       @Value("${application.security.jwt.expiration}")
       private Duration duration;
        private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(UserDetails user){
              Date issued =new Date();
              Date expiration=Date.from(issued.toInstant().plus(duration));
              return Jwts.builder().setSubject(user.getUsername())
                      .setIssuedAt(issued)
                      .setExpiration(expiration)
                      .signWith(key)
                      .claim("roles",user.getAuthorities()).compact();
       }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractEmail(String token) {
            return extractAllClaims(token).getSubject();
        }

        public String getSubject(String token){
            return extractAllClaims(token).getSubject();
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }



}



