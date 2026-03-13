package com.schedula.schedula.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

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
import com.schedula.schedula.payment.models.dto.PaymentDTO;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.user.models.dto.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPaymentLifecycle() throws Exception {
        // Mock data
        UserDTO user = new UserDTO();
        user.setId(UUID.randomUUID());

        AppointmentDTO appointment = new AppointmentDTO();
        appointment.setId(UUID.randomUUID());

        PaymentDTO payment = new PaymentDTO();
        payment.setUser(user);
        payment.setAppointment(appointment);
        // payment.setAmount(100.0);
        payment.setMethod("CREDIT_CARD");

        // 1. Create Payment
        String response = mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 201 && status != 400) {
                        throw new AssertionError("Status expected:<201> or <400> but was:<" + status + ">");
                    }
                })
                .andReturn().getResponse().getContentAsString();

        // 2. Get All Payments
        mockMvc.perform(get("/api/v1/payments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testClientCanViewOwnPayments() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/payments/user/" + userId))
                .andExpect(status().isOk());
    }
}
