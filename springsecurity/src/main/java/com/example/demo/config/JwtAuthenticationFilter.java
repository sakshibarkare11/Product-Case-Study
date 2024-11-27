package com.example.demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.services.JWTService;
import com.example.demo.services.UserService;

import org.apache.commons.lang3.StringUtils; // Use StringUtils from Apache Commons

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JWTService jwtService;
	private final UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		
		// Check if the Authorization header is empty or doesn't start with "Bearer "
		if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);  // Continue the filter chain if no token is provided
			return;
		}

		// Extract the JWT from the Authorization header
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUserName(jwt);  // Extract the username (email) from the token
		
		// Proceed if the user email is valid and no existing authentication is set in SecurityContext
		if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
			
			// Validate the token
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken token = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				// Set authentication details, including remote address, session info, etc.
				token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication object into the SecurityContext
				SecurityContextHolder.getContext().setAuthentication(token);
			}
		}

		// Continue with the filter chain
		filterChain.doFilter(request, response);
	}
}
