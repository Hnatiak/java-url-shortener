package org.example.urlshortener.redirect.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.example.urlshortener.common.exception.BadRequestException;
import org.example.urlshortener.common.exception.NotFoundException;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RedirectServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private RedirectService redirectService;

    @Test
    void shouldRedirectSuccessfully() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode("abc123");
        shortUrl.setOriginalUrl("https://youtube.com");
        shortUrl.setExpiresAt(
                LocalDateTime.now().plusDays(1)
        );

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(shortUrl));

        when(shortUrlRepository.incrementClickCount("abc123"))
                .thenReturn(1);

        String result =
                redirectService.redirect("abc123");

        assertEquals(
                "https://youtube.com",
                result
        );

        verify(shortUrlRepository)
                .incrementClickCount("abc123");
    }

    @Test
    void shouldThrowWhenLinkExpired() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode("abc123");
        shortUrl.setOriginalUrl("https://youtube.com");
        shortUrl.setExpiresAt(
                LocalDateTime.now().minusDays(1)
        );

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(shortUrl));

        assertThrows(
                BadRequestException.class,
                () -> redirectService.redirect("abc123")
        );

        verify(shortUrlRepository, never())
                .incrementClickCount(any());
    }

    @Test
    void shouldThrowWhenShortUrlNotFound() {

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> redirectService.redirect("abc123")
        );

        verify(shortUrlRepository, never())
                .incrementClickCount(any());
    }

    @Test
    void shouldThrowWhenCounterUpdateFails() {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode("abc123");
        shortUrl.setOriginalUrl("https://youtube.com");
        shortUrl.setExpiresAt(LocalDateTime.now().plusDays(1));
    
        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(shortUrl));
    
        when(shortUrlRepository.incrementClickCount("abc123"))
                .thenReturn(0);
    
        assertThrows(
                RuntimeException.class,
                () -> redirectService.redirect("abc123")
        );
    }
}
