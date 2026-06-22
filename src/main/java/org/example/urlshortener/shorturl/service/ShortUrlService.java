package org.example.urlshortener.shorturl.service;

import java.util.List;

import org.example.urlshortener.shorturl.dto.CreateShortUrlRequest;
import org.example.urlshortener.shorturl.dto.ShortUrlDetailsResponse;
import org.example.urlshortener.shorturl.dto.ShortUrlResponse;
import org.example.urlshortener.shorturl.dto.UpdateShortUrlRequest;

public interface ShortUrlService {

    ShortUrlResponse createShortUrl(
            CreateShortUrlRequest request,
            String username
    );

    List<ShortUrlDetailsResponse> getAllUrls(
            String username
    );

    List<ShortUrlDetailsResponse> getActiveUrls(
            String username
    );

    void deleteUrl(
            Long id,
            String username
    );

    void updateUrl(
            Long id,
            UpdateShortUrlRequest request,
            String username
    );
}