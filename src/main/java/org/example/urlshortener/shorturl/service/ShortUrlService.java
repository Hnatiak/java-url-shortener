package org.example.urlshortener.shorturl.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.auth.repository.UserRepository;
import org.example.urlshortener.shorturl.dto.CreateShortUrlRequest;
import org.example.urlshortener.shorturl.dto.ShortUrlDetailsResponse;
import org.example.urlshortener.shorturl.dto.ShortUrlResponse;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    private final UserRepository userRepository;

    public ShortUrlResponse createShortUrl(
            CreateShortUrlRequest request,
            String username
    ) {

        User user =
                userRepository.findByUsername(
                        username
                ).orElseThrow();

        String code =
                generateCode();

        ShortUrl shortUrl =
                new ShortUrl();

        shortUrl.setOriginalUrl(
                request.originalUrl()
        );

        shortUrl.setShortCode(
                code
        );

        shortUrl.setClickCount(
                0L
        );

        shortUrl.setCreatedAt(
                LocalDateTime.now()
        );

        shortUrl.setExpiresAt(
                request.expiresAt()
        );

        shortUrl.setUser(
                user
        );

        shortUrlRepository.save(
                shortUrl
        );

        return new ShortUrlResponse(
                code,
                "http://localhost:8080/" + code
        );
    }

    private String generateCode() {

        String chars =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random =
                new Random();

        StringBuilder code =
                new StringBuilder();

        for (int i = 0; i < 8; i++) {

            code.append(
                    chars.charAt(
                            random.nextInt(
                                    chars.length()
                            )
                    )
            );
        }

        return code.toString();
    }

    public List<ShortUrlDetailsResponse> getAllUrls(
                String username
        ) {

            User user =
                    userRepository.findByUsername(
                            username
                    ).orElseThrow();

            return shortUrlRepository
                    .findAllByUser(user)
                    .stream()
                    .map(url ->
                            new ShortUrlDetailsResponse(
                                    url.getId(),
                                    url.getShortCode(),
                                    url.getOriginalUrl(),
                                    url.getClickCount(),
                                    url.getExpiresAt()
                            )
                    )
                    .toList();
        }


        public List<ShortUrlDetailsResponse> getActiveUrls(
                String username
        ) {

            User user =
                    userRepository.findByUsername(
                            username
                    ).orElseThrow();

            return shortUrlRepository
                    .findAllByUserAndExpiresAtAfter(
                            user,
                            LocalDateTime.now()
                    )
                    .stream()
                    .map(url ->
                            new ShortUrlDetailsResponse(
                                    url.getId(),
                                    url.getShortCode(),
                                    url.getOriginalUrl(),
                                    url.getClickCount(),
                                    url.getExpiresAt()
                            )
                    )
                    .toList();
        }

        public void deleteUrl(
                Long id,
                String username
        ) {
        
            User user =
                    userRepository.findByUsername(
                            username
                    ).orElseThrow();
            
            ShortUrl shortUrl =
                    shortUrlRepository.findById(id)
                            .orElseThrow();
            
            if (!shortUrl.getUser()
                    .getId()
                    .equals(user.getId())) {
                
                throw new RuntimeException(
                        "Access denied"
                );
            }
    
            shortUrlRepository.delete(
                    shortUrl
            );
        }
}