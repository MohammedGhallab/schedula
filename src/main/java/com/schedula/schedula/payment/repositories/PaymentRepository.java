package com.schedula.schedula.payment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.payment.models.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
