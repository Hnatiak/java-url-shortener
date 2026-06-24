package org.example.urlshortener.auth.controller;

import java.util.UUID;

import org.example.urlshortener.AbstractIntegrationTest;
import org.example.urlshortener.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
class AuthControllerTest extends AbstractIntegrationTest {

    static {
        System.out.println("ABSTRACT LOADED");
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        String username = "user_" + UUID.randomUUID();

        String body = """
        {
            "username":"%s",
            "password":"Password123"
        }
        """.formatted(username);

        mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
    }

    @Test
    void shouldLoginUser() throws Exception {
        String username = "user_" + UUID.randomUUID();

        String register = """
        {
            "username":"%s",
            "password":"Password123"
        }
        """.formatted(username);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(register))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(register))
                .andExpect(status().isOk());
    }

    @Test
    void noJwtShouldReturn401() throws Exception {
        mockMvc.perform(post("/api/v1/urls"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void duplicateUsernameShouldReturn409() throws Exception {
        String username = "user_" + UUID.randomUUID();

        String body = """
        {
            "username":"%s",
            "password":"Password123"
        }
        """.formatted(username);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void invalidCredentialsShouldReturn401() throws Exception {
        String username = "user_" + UUID.randomUUID();

        String register = """
        {
            "username":"%s",
            "password":"Password123"
        }
        """.formatted(username);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(register))
                .andExpect(status().isCreated());

        String badLogin = """
        {
            "username":"%s",
            "password":"WrongPassword"
        }
        """.formatted(username);;

        mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(badLogin))
            .andDo(print())
            .andReturn();
    }

    @Test
    void invalidJwtShouldReturn401() throws Exception {
        mockMvc.perform(post("/api/v1/urls")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "originalUrl":"https://google.com",
                    "expiresAt":"2027-12-31T23:59:59"
                }
                """))
                .andExpect(status().isUnauthorized());
    }
}