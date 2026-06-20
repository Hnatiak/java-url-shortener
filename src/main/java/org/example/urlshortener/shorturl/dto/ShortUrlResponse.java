package org.example.urlshortener.shorturl.dto;

public record ShortUrlResponse(
        String shortCode,
        String shortUrl
) {
}