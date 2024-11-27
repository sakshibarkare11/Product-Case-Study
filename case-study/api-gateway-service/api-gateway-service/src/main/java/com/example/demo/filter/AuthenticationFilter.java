package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.example.demo.util.JwtUtil;

import java.util.Set;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Check if the route requires security
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the Authorization header is present
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7); // Extract token part
                }

                try {
                    // Extract roles from the token
                    Set<String> roles = jwtUtil.extractRoles(authHeader);
                    
                    // Define your role-based access control logic
                    if (!hasRequiredRole(roles, exchange)) {
                        throw new RuntimeException("Unauthorized access: Insufficient role");
                    }

                    // Optionally, validate the token here
                    String username = jwtUtil.extractUserName(authHeader);
                    jwtUtil.validateToken(authHeader, username);

                } catch (Exception e) {
                    throw new RuntimeException("Unauthorized access to application: " + e.getMessage());
                }
            }
            return chain.filter(exchange);
        };
    }

    // Method to check if the user has the required role for accessing the route
    private boolean hasRequiredRole(Set<String> userRoles, org.springframework.web.server.ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();

        // Example: restrict access to /products/addProduct, /products/updateProduct, /products/deleteById to ADMIN role
        if (path.contains("/products/addProduct") || 
            path.contains("/products/updateProduct") ||
            path.contains("/products/deleteById")) {
            return userRoles.contains("ADMIN");
        }

        // Example: allow access to /products/** for USERS and higher roles
        if (path.contains("/products/")) {
            return userRoles.contains("USER") || userRoles.contains("ADMIN");
        }
        
        if (path.contains("/cart/")) {
            return userRoles.contains("USER");
        }
        // Allow access to other paths (public or other roles)
        return true;
    }

    public static class Config {
        // Configuration if needed
    }
}
