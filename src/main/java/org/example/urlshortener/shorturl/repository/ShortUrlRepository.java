package org.example.urlshortener.shorturl.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository
        extends JpaRepository<ShortUrl, Long> {

        Optional<ShortUrl> findByShortCode(
                String shortCode
        );

        List<ShortUrl> findAllByUser(
                User user
        );

        List<ShortUrl> findAllByUserAndExpiresAtAfter(
                User user,
                LocalDateTime now
        );
}