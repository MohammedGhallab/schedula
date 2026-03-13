package com.schedula.schedula.payment.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.user.models.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "بيانات الدفع")
public class PaymentDTO {
    
    @Schema(description = "معرف عملية الدفع", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotNull(message = "الموعد مطلوب")
    @Schema(description = "الموعد المرتبط بعملية الدفع")
    private AppointmentDTO appointment;

    @NotNull(message = "المستخدم مطلوب")
    @Schema(description = "المستخدم الذي قام بعملية الدفع")
    private UserDTO user;

    @NotNull(message = "المبلغ مطلوب")
    @Positive(message = "المبلغ يجب أن يكون قيمة موجبة")
    @Schema(description = "مبلغ العملية", example = "150.00")
    private BigDecimal amount;

    @NotBlank(message = "طريقة الدفع مطلوبة")
    @Schema(description = "طريقة الدفع (مثل: CASH, CARD, ONLINE)", example = "CARD")
    private String method;

    @Schema(description = "حالة الدفع (مثل: PENDING, PAID, CANCELLED)", example = "PAID")
    private String status;

    @Schema(description = "مرجع العملية من بوابة الدفع", example = "TRX-123456789")
    private String transactionRef;

    @Schema(description = "تاريخ ووقت الدفع", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime paidAt;
}
