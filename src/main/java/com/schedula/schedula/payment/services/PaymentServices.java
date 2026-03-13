package com.schedula.schedula.payment.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.schedula.schedula.payment.models.dto.PaymentDTO;

public interface PaymentServices {
    PaymentDTO getPaymentById(UUID id);

    List<PaymentDTO> getAllPayments(Pageable pageable);

    PaymentDTO createPayment(PaymentDTO dto);

    PaymentDTO updatePayment(PaymentDTO dto);

    void deletePayment(UUID id);

    List<PaymentDTO> getPaymentsByUserId(UUID userId, Pageable pageable);
}
