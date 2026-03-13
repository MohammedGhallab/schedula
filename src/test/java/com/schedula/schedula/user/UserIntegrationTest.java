package com.schedula.schedula.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedula.schedula.user.models.dto.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserLifecycle() throws Exception {
        // 1. Create User
        UserDTO user = new UserDTO();
        user.setName("Test User");
        user.setEmail("user@test.com");
        user.setPassword("password123");
        user.setPhone("0123456789");
        user.setRole("CLIENT");
        user.setActive(true);

        String response = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(response, UserDTO.class);
        UUID userId = createdUser.getId();

        // 2. Get User
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"));

        // 3. Update User
        createdUser.setName("Updated Name");
        mockMvc.perform(put("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));

        // 4. Delete User
        mockMvc.perform(delete("/api/v1/users/" + userId))
                .andExpect(status().isNoContent());

        // 5. Verify Not Found
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testClientCannotCreateUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("New Admin");
        user.setEmail("admin@test.com");
        user.setPassword("pass");
        user.setRole("ADMIN");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden());
    }
}
