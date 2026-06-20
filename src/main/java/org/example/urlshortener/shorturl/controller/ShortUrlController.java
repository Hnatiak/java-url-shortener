package org.example.urlshortener.shorturl.controller;

import java.util.List;

import org.example.urlshortener.shorturl.dto.CreateShortUrlRequest;
import org.example.urlshortener.shorturl.dto.ShortUrlDetailsResponse;
import org.example.urlshortener.shorturl.dto.ShortUrlResponse;
import org.example.urlshortener.shorturl.service.ShortUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    public ShortUrlResponse createUrl(
            @RequestBody
            CreateShortUrlRequest request,

            Authentication authentication
    ) {

        return shortUrlService.createShortUrl(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<ShortUrlDetailsResponse> getAllUrls(
            Authentication authentication
    ) {

        return shortUrlService.getAllUrls(
                authentication.getName()
        );
    }


    @GetMapping("/active")
    public List<ShortUrlDetailsResponse> getActiveUrls(
            Authentication authentication
    ) {
    
        return shortUrlService.getActiveUrls(
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable Long id,
            Authentication authentication
    ) {
    
        shortUrlService.deleteUrl(
                id,
                authentication.getName()
        );
    
        return ResponseEntity.noContent()
                .build();
    }
}