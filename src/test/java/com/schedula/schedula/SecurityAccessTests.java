package com.schedula.schedula;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void shouldReturnForbiddenWhenClientAccessesAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnOkWhenAdminAccessesAdminEndpoint() throws Exception {
      mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void shouldAllowClientToAccessProviderEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/providers"))
               .andExpect(status().isOk());
    }
}
