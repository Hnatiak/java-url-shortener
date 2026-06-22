package org.example.urlshortener.shorturl.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;

public record UpdateShortUrlRequest(

        @Pattern(
                regexp = "^(https?://).+",
                message = "Invalid URL"
        )
        String originalUrl,

        LocalDateTime expiresAt
) {}