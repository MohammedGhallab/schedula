package com.schedula.schedula.payment.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.appointment.repositories.AppointmentRepository;
import com.schedula.schedula.payment.mapper.PaymentMapper;
import com.schedula.schedula.payment.models.dto.PaymentDTO;
import com.schedula.schedula.payment.models.entities.Payment;
import com.schedula.schedula.payment.repositories.PaymentRepository;
import com.schedula.schedula.payment.services.PaymentServices;
import com.schedula.schedula.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServicesImpl implements PaymentServices {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(UUID id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null");
        return paymentRepository.findById(id)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments(Pageable pageable) {
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        return paymentMapper.toDTOList(paymentPage.getContent());
    }

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO dto) {
        if (dto.getUser() == null || dto.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID is required for payment");
        }
        if (dto.getAppointment() == null || dto.getAppointment().getId() == null) {
            throw new IllegalArgumentException("Appointment ID is required for payment");
        }

        // Validate existence
        if (!userRepository.existsById(dto.getUser().getId())) {
            throw new EntityNotFoundException("User not found: " + dto.getUser().getId());
        }
        if (!appointmentRepository.existsById(dto.getAppointment().getId())) {
            throw new EntityNotFoundException("Appointment not found: " + dto.getAppointment().getId());
        }

        Payment payment = paymentMapper.toEntity(dto);
        payment.setId(null); 
        payment.setPaidAt(LocalDateTime.now());
        
        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            payment.setStatus("PENDING");
        }
        
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDTO(savedPayment);
    }

    @Override
    @Transactional
    public PaymentDTO updatePayment(PaymentDTO dto) {
        if (dto.getId() == null) throw new IllegalArgumentException("Payment ID is required for update");
        
        Payment existingPayment = paymentRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Update failed, Payment not found"));

        paymentMapper.updateEntityFromDto(dto, existingPayment);
        
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return paymentMapper.toDTO(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(UUID id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null");
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Delete failed, Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByUserId(UUID userId, Pageable pageable) {
        if (userId == null) throw new IllegalArgumentException("UserId cannot be null");
        if (pageable == null) throw new IllegalArgumentException("Pageable cannot be null");
        Page<Payment> paymentPage = paymentRepository.findByUserId(userId, pageable);
        return paymentMapper.toDTOList(paymentPage.getContent());
    }
}
