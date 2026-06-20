package org.example.urlshortener.shorturl.dto;

import java.time.LocalDateTime;

public record ShortUrlDetailsResponse(

        Long id,

        String shortCode,

        String originalUrl,

        Long clickCount,

        LocalDateTime expiresAt
) {
}