package com.team29.backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team29.backend.config.JwtService;
import com.team29.backend.model.User;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"}, allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token,
            @AuthenticationPrincipal User user) {
        try {
            Boolean isValidToken = jwt.isTokenValid(token, user);
            return ResponseEntity.ok(isValidToken);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.ok(false);
        }
    } 
    
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.login(request));      
    }   


}
