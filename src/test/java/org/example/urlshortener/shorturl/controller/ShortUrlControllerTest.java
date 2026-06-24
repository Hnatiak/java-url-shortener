package org.example.urlshortener.shorturl.controller;

import java.util.List;
import java.util.UUID;

import org.example.urlshortener.AbstractIntegrationTest;
import org.example.urlshortener.auth.repository.UserRepository;
import org.example.urlshortener.shorturl.entity.ShortUrl;
import org.example.urlshortener.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
class ShortUrlControllerTest extends AbstractIntegrationTest {


    @Autowired UserRepository userRepository;

    @BeforeEach
    void clean() {
        shortUrlRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    void invalidJwtShouldReturn401() throws Exception {

        mockMvc.perform(
                get("/api/v1/urls")
                        .header("Authorization", "Bearer invalid-token")
        )
        .andDo(print())
        .andExpect(status().isUnauthorized());
    }

    @Test
    void expiredDateShouldReturn400() throws Exception {
    
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
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
    }
}