package com.schedula.schedula;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
        // Only ADMIN can access /api/v1/users
        mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnOkWhenAdminAccessesAdminEndpoint() throws Exception {
        // ADMIN should be able to at least pass authorization (might be 404 or 200 depending on actual data, but not 401/403)
        // Here we just test if it's NOT forbidden/unauthorized. 
        // 200 OK is expected based on the implementation of getAllUsers.
        mockMvc.perform(get("/api/v1/users"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void shouldAllowClientToAccessProviderEndpoint() throws Exception {
        // /api/v1/providers can be accessed by CLIENT
        mockMvc.perform(get("/api/v1/providers"))
               .andExpect(status().isOk());
    }
}
