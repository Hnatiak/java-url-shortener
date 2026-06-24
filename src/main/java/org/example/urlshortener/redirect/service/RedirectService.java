package org.example.urlshortener.redirect.service;

import java.time.LocalDateTime;

import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import org.example.urlshortener.common.exception.LinkExpiredException;
import org.example.urlshortener.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository shortUrlRepository;

    public String redirect(String shortCode) {

        ShortUrl shortUrl =
                shortUrlRepository
                        .findByShortCode(shortCode)
                        .orElseThrow(() -> new NotFoundException("Short URL not found"));

        if (shortUrl.getExpiresAt().isBefore(LocalDateTime.now()) || shortUrl.getExpiresAt().isEqual(LocalDateTime.now())) {
            throw new LinkExpiredException("Link expired");
        }

        int updated =
        shortUrlRepository.incrementClickCount(
                shortCode
        );

        if (updated == 0) {
            throw new IllegalStateException("Counter update failed");
        }

        return shortUrl.getOriginalUrl();
    }
}