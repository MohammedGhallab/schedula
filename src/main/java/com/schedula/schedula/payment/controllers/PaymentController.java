package com.schedula.schedula.payment.controllers;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.audit.annotations.Auditable;
import com.schedula.schedula.payment.models.dto.PaymentDTO;
import com.schedula.schedula.payment.services.PaymentServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "إدارة المدفوعات", description = "العمليات البرمجية لإدارة عمليات الدفع")
public class PaymentController {

    private final PaymentServices paymentServices;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'PROVIDER')")
    @Operation(summary = "جلب تفاصيل عملية دفع بواسطة المعرف")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentServices.getPaymentById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "جلب جميع عمليات الدفع (للأدمن)")
    public ResponseEntity<List<PaymentDTO>> getAllPayments(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(paymentServices.getAllPayments(pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(summary = "جلب عمليات الدفع لمستخدم معين")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(@PathVariable UUID userId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(paymentServices.getPaymentsByUserId(userId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Auditable(action = "PAYMENT_CREATED")
    @Operation(summary = "إنشاء عملية دفع جديدة")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO dto) {
        return new ResponseEntity<>(paymentServices.createPayment(dto), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "PAYMENT_UPDATED")
    @Operation(summary = "تحديث بيانات عملية دفع")
    public ResponseEntity<PaymentDTO> updatePayment(@Valid @RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(paymentServices.updatePayment(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "PAYMENT_DELETED")
    @Operation(summary = "حذف عملية دفع")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        paymentServices.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
