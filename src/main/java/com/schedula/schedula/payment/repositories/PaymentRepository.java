package com.schedula.schedula.payment.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.payment.models.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    @Override
    @EntityGraph(attributePaths = {"user", "appointment"})
    Optional<Payment> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"user", "appointment"})
    Page<Payment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "appointment"})
    Page<Payment> findByUserId(UUID userId, Pageable pageable);
}
