package com.example.demo.services;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entities.User;

public interface JWTService {
	
	String extractUserName(String token);
	String generateToken(UserDetails userDetails);
	
	boolean isTokenValid(String token,UserDetails userDetails);
	String genrateRefreshToken(Map<String, Object> extraClaims,UserDetails userDetails);
	
	void validateToken(final String token);
}
