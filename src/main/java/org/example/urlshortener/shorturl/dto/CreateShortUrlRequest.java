package org.example.urlshortener.shorturl.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateShortUrlRequest(

        @NotBlank
        String originalUrl,

        @NotNull
        LocalDateTime expiresAt
) {
}