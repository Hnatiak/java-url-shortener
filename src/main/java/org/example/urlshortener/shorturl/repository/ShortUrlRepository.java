package org.example.urlshortener.shorturl.repository;

import java.util.Optional;

import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository
        extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(
            String shortCode
    );
}