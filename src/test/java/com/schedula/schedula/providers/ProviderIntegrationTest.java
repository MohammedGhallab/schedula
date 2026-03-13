package com.schedula.schedula.providers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProviderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllProviders() throws Exception {
        mockMvc.perform(get("/api/v1/providers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminAccessToProviders() throws Exception {
        mockMvc.perform(get("/api/v1/providers"))
                .andExpect(status().isOk());
    }
}
