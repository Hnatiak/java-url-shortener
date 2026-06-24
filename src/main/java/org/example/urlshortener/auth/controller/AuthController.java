package org.example.urlshortener.auth.controller;

import org.example.urlshortener.auth.dto.AuthResponse;
import org.example.urlshortener.auth.dto.LoginRequest;
import org.example.urlshortener.auth.dto.RegisterRequest;
import org.example.urlshortener.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        authService.register(request);

        return new AuthResponse(
                "User successfully registered"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
    
        String token =
                authService.login(request);
    
        return ResponseEntity.ok(
                new AuthResponse(token)
        );
    }
}