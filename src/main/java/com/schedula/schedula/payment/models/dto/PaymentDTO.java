package com.schedula.schedula.payment.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.user.models.entities.User;
import lombok.Data;

@Data
public class PaymentDTO {
    private UUID id;
    private Appointment appointment;
    private User user;
    private BigDecimal amount;
    private String method; // CARD, CASH, WALLET
    private String status; // PENDING, PAID, FAILED
    private String transactionRef;
    private LocalDateTime paidAt;
}
