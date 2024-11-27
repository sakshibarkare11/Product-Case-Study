package com.example.demo.services;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.SigninRequest;
import com.example.demo.entities.User;

public interface AuthenticationService {

	User signUp(SignUpRequest signUpRequest);
	
	JwtAuthenticationResponse signin(SigninRequest signinRequest);
	
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
	
	void validateToken(final String token);
}
