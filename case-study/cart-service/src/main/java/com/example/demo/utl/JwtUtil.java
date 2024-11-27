package com.example.demo.utl;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {

	public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Parse and retrieve all claims from the JWT token
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    private Key getSignKey() {
        // Use the base64-decoded secret key for HMAC algorithm
        byte[] key = Decoders.BASE64.decode("413F442847284862506553685660597033733676397924422645294840406351");  // Make sure the key is securely stored
        return Keys.hmacShaKeyFor(key);
    }


   

    private boolean isTokenExpired(String token) {
        // Check if the token has expired
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
