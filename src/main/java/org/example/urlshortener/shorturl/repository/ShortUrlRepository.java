package org.example.urlshortener.shorturl.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ShortUrlRepository
        extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(
            String shortCode
    );

    List<ShortUrl> findAllByUser(User user);

    List<ShortUrl> findAllByUserAndExpiresAtAfter(
            User user,
            LocalDateTime time
    );

    @Modifying
    @Transactional
    @Query("""
        update ShortUrl s
        set s.clickCount = s.clickCount + 1
        where s.shortCode = :shortCode
    """)
    int incrementClickCount(String shortCode);
}