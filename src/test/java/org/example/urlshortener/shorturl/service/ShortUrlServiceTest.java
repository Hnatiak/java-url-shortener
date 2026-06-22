package org.example.urlshortener.shorturl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.auth.repository.UserRepository;
import org.example.urlshortener.shorturl.dto.CreateShortUrlRequest;
import org.example.urlshortener.shorturl.dto.ShortUrlResponse;
import org.example.urlshortener.shorturl.dto.UpdateShortUrlRequest;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.urlshortener.common.exception.NotFoundException;
import org.example.urlshortener.common.exception.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShortUrlServiceImpl shortUrlService;

    @Test
    void shouldCreateShortUrl() {
        User user = new User();
        user.setId(1L);
        user.setUsername("roman");

        CreateShortUrlRequest request =
                new CreateShortUrlRequest(
                        "https://youtube.com",
                        LocalDateTime.now().plusDays(1)
                );

        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(user));

        ShortUrlResponse response =
                shortUrlService.createShortUrl(
                        request,
                        "roman"
                );

        assertNotNull(response);
        assertNotNull(response.shortCode());
        assertTrue(
                response.shortUrl()
                        .contains("http://localhost:8080/")
        );

        verify(shortUrlRepository, times(1))
                .save(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        CreateShortUrlRequest request =
                new CreateShortUrlRequest(
                        "https://youtube.com",
                        LocalDateTime.now().plusDays(1)
                );
            
        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.empty());
            
        assertThrows(
                NotFoundException.class,
                () -> shortUrlService.createShortUrl(
                        request,
                        "roman"
                )
        );
    
        verify(shortUrlRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenDeletingForeignUrl() {

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("owner");

        User attacker = new User();
        attacker.setId(2L);
        attacker.setUsername("roman");

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(100L);
        shortUrl.setUser(owner);

        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(attacker));

        when(shortUrlRepository.findById(100L))
                .thenReturn(Optional.of(shortUrl));

        assertThrows(
                AccessDeniedException.class,
                () -> shortUrlService.deleteUrl(
                        100L,
                        "roman"
                )
        );

        verify(shortUrlRepository, never())
                .delete(any());
    }

    @Test
    void shouldDeleteUrl() {

        User user = new User();
        user.setId(1L);
        user.setUsername("roman");

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(100L);
        shortUrl.setUser(user);

        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(user));

        when(shortUrlRepository.findById(100L))
                .thenReturn(Optional.of(shortUrl));

        shortUrlService.deleteUrl(
                100L,
                "roman"
        );

        verify(shortUrlRepository)
                .delete(shortUrl);
    }

    @Test
    void shouldUpdateUrl() {
        User user = new User();
        user.setId(1L);

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(100L);
        shortUrl.setUser(user);
        shortUrl.setOriginalUrl("https://old.com");

        UpdateShortUrlRequest request =
                new UpdateShortUrlRequest(
                        "https://new.com",
                        LocalDateTime.now().plusDays(10)
                );

        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(user));

        when(shortUrlRepository.findById(100L))
                .thenReturn(Optional.of(shortUrl));

        shortUrlService.updateUrl(
                100L,
                request,
                "roman"
        );

        assertEquals(
                "https://new.com",
                shortUrl.getOriginalUrl()
        );

        verify(shortUrlRepository)
                .save(shortUrl);
    }


    @Test
    void shouldThrowWhenUpdatingForeignUrl() {
        User owner = new User();
        owner.setId(1L);
    
        User attacker = new User();
        attacker.setId(2L);
    
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUser(owner);
    
        UpdateShortUrlRequest request =
                new UpdateShortUrlRequest(
                        "https://new.com",
                        null
                );
            
        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(attacker));
            
        when(shortUrlRepository.findById(1L))
                .thenReturn(Optional.of(shortUrl));
            
        assertThrows(
                AccessDeniedException.class,
                () -> shortUrlService.updateUrl(
                        1L,
                        request,
                        "roman"
                )
        );
    }

    @Test
    void shouldThrowWhenDeletingMissingUrl() {
        User user = new User();
        user.setId(1L);
    
        when(userRepository.findByUsername("roman"))
                .thenReturn(Optional.of(user));
    
        when(shortUrlRepository.findById(999L))
                .thenReturn(Optional.empty());
    
        assertThrows(
                NotFoundException.class,
                () -> shortUrlService.deleteUrl(999L, "roman")
        );
    }
}