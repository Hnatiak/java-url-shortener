package org.example.urlshortener.redirect.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.example.urlshortener.AbstractIntegrationTest;
import org.example.urlshortener.auth.entity.User;
import org.example.urlshortener.auth.repository.UserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class RedirectControllerTest extends AbstractIntegrationTest {
    @BeforeEach
    void clean() {
        shortUrlRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UserRepository userRepository;

    private String getToken() throws Exception {

        String username = "user_" + UUID.randomUUID();
        String password = "Password123";

        String registerBody = """
        {
            "username":"%s",
            "password":"%s"
        }
        """.formatted(username, password);

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody)
        ).andExpect(status().isCreated());

        String loginBody = """
        {
            "username":"%s",
            "password":"%s"
        }
        """.formatted(username, password);

        String response =
                mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response
                .replace("{\"token\":\"", "")
                .replace("\"}", "");
    }

    private String createUrlAndGetCode() throws Exception {

        String token = getToken();

        String body = """
        {
            "originalUrl":"https://youtube.com",
            "expiresAt":"2027-12-31T23:59:59"
        }
        """;

        mockMvc.perform(
                post("/api/v1/urls")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isOk());

        List<ShortUrl> urls = shortUrlRepository.findAll();

        return urls.get(urls.size() - 1).getShortCode();
    }

    private String createExpiredUrlAndGetCode() throws Exception {
        String token = getToken();

        String body = """
        {
            "originalUrl":"https://youtube.com",
            "expiresAt":"2020-01-01T00:00:00"
        }
        """;

        mockMvc.perform(
                post("/api/v1/urls")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        List<ShortUrl> urls = shortUrlRepository.findAll();

        return urls.get(urls.size() - 1).getShortCode();
    }

    @Test
    void shouldRedirect() throws Exception {

        String shortCode = createUrlAndGetCode();

        mockMvc.perform(
                get("/r/" + shortCode)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "https://youtube.com"));
    }

    @Test
    void expiredRedirectShouldReturn410() throws Exception {

        User user = new User();
        user.setUsername("expired_user");
        user.setPassword("pass");
        user.setCreatedAt(LocalDateTime.now());
        

        userRepository.save(user);

        ShortUrl url = new ShortUrl();
        url.setShortCode("expired1");
        url.setOriginalUrl("https://youtube.com");
        url.setClickCount(0L);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().minusDays(1));
        url.setUser(user);   // <-- ОЦЕ бракувало

        shortUrlRepository.save(url);

        mockMvc.perform(get("/r/expired1"))
                .andDo(print())
                .andExpect(status().isGone());
    }
}