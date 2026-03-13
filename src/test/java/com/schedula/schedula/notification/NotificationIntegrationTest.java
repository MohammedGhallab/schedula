package com.schedula.schedula.notification;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

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
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "CLIENT")
    void testGetNotifications() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/notifications/user/" + userId))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 200 && status != 404) {
                        throw new AssertionError("Status expected:<200> or <404> but was:<" + status + ">");
                    }
                }); 
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testMarkAsRead() throws Exception {
        UUID notificationId = UUID.randomUUID();
        mockMvc.perform(put("/api/v1/notifications/" + notificationId + "/read"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 200 && status != 404) {
                        throw new AssertionError("Status expected:<200> or <404> but was:<" + status + ">");
                    }
                });
    }
}
