package org.example.urlshortener.auth.service;

import java.time.LocalDateTime;

import org.example.urlshortener.auth.dto.LoginRequest;
import org.example.urlshortener.auth.dto.RegisterRequest;
import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.auth.repository.UserRepository;
import org.example.urlshortener.common.exception.ConflictException;
import org.example.urlshortener.common.exception.UnauthorizedException;
import org.example.urlshortener.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.username()
        )) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();

        user.setUsername(
                request.username()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        user.setCreatedAt(
                LocalDateTime.now()
        );

        userRepository.save(user);
    }


    public String login(LoginRequest request) {

            User user = userRepository
                    .findByUsername(request.username())
                    .orElseThrow(() ->
                            new UnauthorizedException("Invalid credentials"));
        
            if (!passwordEncoder.matches(
                    request.password(),
                    user.getPassword()
            )) {
                throw new UnauthorizedException("Invalid credentials");
            }
    
            return jwtService.generateToken(
                    user.getUsername()
            );
        }
}