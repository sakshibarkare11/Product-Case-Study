package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

   // private static final String SECRET_KEY = "413F442847284862506553685660597033733676397924422645294840406351";  // Store this securely

    private Key getSignKey() {
        // Use the base64-decoded secret key for HMAC algorithm
        byte[] key = Decoders.BASE64.decode("413F442847284862506553685660597033733676397924422645294840406351");  // Make sure the key is securely stored
        return Keys.hmacShaKeyFor(key);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return Set.of(claims.get("roles", String.class).split(","));
    }

    private Claims extractAllClaims(String token) {
        // Parse and retrieve all claims from the JWT token
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUserName(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        // Check if the token has expired
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}



