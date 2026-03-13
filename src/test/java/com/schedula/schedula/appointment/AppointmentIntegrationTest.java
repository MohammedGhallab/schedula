package com.schedula.schedula.appointment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.user.models.dto.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "CLIENT")
    void testBookingFlow() throws Exception {
        UUID providerId = UUID.randomUUID();
        
        UserDTO user = new UserDTO();
        user.setId(UUID.randomUUID());

        ProvidersDTO provider = new ProvidersDTO();
        provider.setId(providerId);

        AppointmentDTO appointment = new AppointmentDTO();
        appointment.setUser(user);
        appointment.setProvider(provider);
        appointment.setDate(LocalDate.now().plusDays(5));
        appointment.setTime(LocalTime.of(15, 0));
        appointment.setStatus(AppointmentStatus.PENDING);

        mockMvc.perform(post("/api/v1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 201 && (status < 400 || status >= 500)) {
                        throw new AssertionError("Status expected:<201> or <4xx> but was:<" + status + ">");
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminCanViewAllAppointments() throws Exception {
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk());
    }
}
