package com.example.demo.services.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.SigninRequest;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists with this email: " + signUpRequest.getEmail());
        }

        // Create a new user
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setSecondName(signUpRequest.getLastName());
        user.setRole(Role.USER);  // Default role, could be made more flexible
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));  // Encrypt password

        // Save user to the database
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        // Authenticate user with AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
        );

        // Retrieve the user after successful authentication
        var user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Generate JWT token
        var jwt = jwtService.generateToken(user);

        // Generate refresh token
        var refreshToken = jwtService.genrateRefreshToken(new HashMap<>(), user);

        // Create and return the response with JWT and refresh token
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }
    
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
    {
    	String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());
    	User user=userRepository.findByEmail(userEmail).orElseThrow();
    	if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user))
    	{
    		var jwt=jwtService.generateToken(user);
    		
    		JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
    	}
    	return null;
    }

	@Override
	public void validateToken(String token) {
		jwtService.validateToken(token);
		
	}
}
