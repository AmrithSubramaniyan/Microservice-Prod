package com.authservice.controller;

import com.authservice.dto.AuthRequest;
import com.authservice.entity.UserCredential;
import com.authservice.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        log.info("Received request to register new user with name: {}", user.getName());
        return service.saveUser(user);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        if (username == null || username.isBlank() || authRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }
        log.info("Received token request for user: {}", username);
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword())
            );
            if (authenticate.isAuthenticated()) {
                log.info("User {} successfully authenticated", username);
                return ResponseEntity.ok(service.generateToken(username));
            } else {
                log.warn("Invalid access attempt for user: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            log.warn("Authentication failed for user {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
        } else if (tokenParam != null && !tokenParam.isBlank()) {
            token = tokenParam.trim();
        }

        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Token is required. Provide it as a Bearer token in the Authorization header or as '?token=' query parameter.");
        }

        log.info("Received request to validate token");
        try {
            service.validateToken(token);
            log.info("Token validation successful");
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token: " + e.getMessage());
        }
    }
}
