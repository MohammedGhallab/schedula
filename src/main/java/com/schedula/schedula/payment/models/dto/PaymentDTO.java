package com.schedula.schedula.payment.models.dto;

import java.time.LocalDateTime;
import com.schedula.schedula.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private Long userId;
    private Double amount;
    private String currency;
    private LocalDateTime paidAt;
    private PaymentStatus status;
}
