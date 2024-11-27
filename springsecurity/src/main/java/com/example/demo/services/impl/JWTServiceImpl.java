package com.example.demo.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {



    // Refresh token expiration time (7 days)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    @Override
    public String generateToken(UserDetails userDetails) {
        // Generate JWT with subject (username) and issued/expiration dates
    	 String roles = userDetails.getAuthorities().stream()
    	            .map(grantedAuthority -> grantedAuthority.getAuthority()) // Get role as string
    	            .collect(Collectors.joining(","));

    	    // Generate JWT with subject (username), roles, issued/expiration dates
    	    return Jwts.builder()
    	            .setSubject(userDetails.getUsername())
    	            .claim("roles", roles)  // Add roles as a claim
    	            .setIssuedAt(new Date(System.currentTimeMillis()))
    	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 hours expiration
    	            .signWith(getSignKey(), SignatureAlgorithm.HS256)
    	            .compact();
    }

    @Override
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

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        // Check if the token has expired
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public String genrateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Generate a refresh token with additional claims
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
