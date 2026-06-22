package org.example.urlshortener.shorturl.controller;

import java.util.List;
import java.util.UUID;

import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

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

    private Long createUrl(String token) throws Exception {

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

        return urls.get(urls.size() - 1).getId();
    }

    @Test
    void shouldCreateShortUrl() throws Exception {

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
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllUrls() throws Exception {

        String token = getToken();

        mockMvc.perform(
                get("/api/v1/urls")
                        .header("Authorization", "Bearer " + token)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUrl() throws Exception {

        String token = getToken();
        Long id = createUrl(token);

        String body = """
        {
            "originalUrl":"https://google.com",
            "expiresAt":"2028-12-31T23:59:59"
        }
        """;

        mockMvc.perform(
                put("/api/v1/urls/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUrl() throws Exception {

        String token = getToken();
        Long id = createUrl(token);

        mockMvc.perform(
                delete("/api/v1/urls/" + id)
                        .header("Authorization", "Bearer " + token)
        )
        .andExpect(status().isNoContent());
    }
}