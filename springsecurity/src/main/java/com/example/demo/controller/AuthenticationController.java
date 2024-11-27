package com.example.demo.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.SigninRequest;
import com.example.demo.entities.User;
import com.example.demo.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	
	private final AuthenticationService authenticationService;
	
	@PostMapping("/signup")
	public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest)
	{
		return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
	}
	
//	@PostMapping("/signin")
//	public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest)
//	{
//		return ResponseEntity.ok(authenticationService.signin(signinRequest));
//	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest)
	{
		return ResponseEntity.ok(authenticationService.signin(signinRequest));
	}
	
	@GetMapping("/get")
	public String get()
	{
		return "This is get mapping";
	}
	
	@PostMapping("/post")
	public String post()
	{
		return "This is post mapping";
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest)
	{
		return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
	}
	
	@GetMapping("/admin")
	public ResponseEntity<String> sayHelloAdmin()
	{
		return ResponseEntity.ok("Hi Admin");
	}
	
	@GetMapping("/user")
	public ResponseEntity<String> sayHelloUser()
	{
		return ResponseEntity.ok("Hi User");
	}
	
	@GetMapping("/validate")
	public String validate(@RequestParam("token") String token)
	{
		authenticationService.validateToken(token);
		return "Token is valid";
	}
}
