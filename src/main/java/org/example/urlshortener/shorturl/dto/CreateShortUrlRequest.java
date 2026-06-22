package org.example.urlshortener.shorturl.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateShortUrlRequest(

    @NotBlank
    @Pattern(
        regexp = "^(https?://).+",
        message = "Invalid URL"
    )
    String originalUrl,

    @NotNull
    LocalDateTime expiresAt
) {}