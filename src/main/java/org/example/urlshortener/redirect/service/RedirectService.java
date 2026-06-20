package org.example.urlshortener.redirect.service;

import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository shortUrlRepository;

    public String redirect(String shortCode) {

        ShortUrl shortUrl =
                shortUrlRepository
                        .findByShortCode(shortCode)
                        .orElseThrow();

        shortUrl.setClickCount(
                shortUrl.getClickCount() + 1
        );

        shortUrlRepository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }
}