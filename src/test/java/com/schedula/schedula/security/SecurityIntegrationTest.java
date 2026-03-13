package com.schedula.schedula.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedula.schedula.user.models.dto.LoginRequset;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRateLimitingOnLogin() throws Exception {
        LoginRequset request = new LoginRequset();
        request.setEmail("test@example.com");
        request.setPassword("password");

        // Fire more than 10 requests rapidly (LOGIN_CAPACITY is 10)
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        // The 11th request should be rate limited
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void testBruteForceProtection() throws Exception {
        String email = "brute@example.com";
        LoginRequset request = new LoginRequset();
        request.setEmail(email);
        request.setPassword("wrong-password");

        // MAX_ATTEMPTS is 5
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().is4xxClientError());
        }

        // The 6th attempt should be blocked with a specific error message logic (if integrated in service)
        // Note: The specific response code might be 403 or 400 depending on GlobalExceptionHandler
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Based on handleRuntimeException in GlobalExceptionHandler throwing blocked message
    }
}
