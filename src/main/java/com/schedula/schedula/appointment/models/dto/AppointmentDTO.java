package com.schedula.schedula.appointment.models.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.payment.models.entities.Payment;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.user.models.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AppointmentDTO {
    @Schema(description = "معرف الموعد قيمة فريدة", example = "205", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "المستخدم مطلوب")
    @Schema(description = "المستخدم", example = "بيانات المستخدم", accessMode = Schema.AccessMode.READ_ONLY)
    private UserDTO user;

    @Schema(description = "معرف المزود قيمة فريدة", example = "55")
    private ProvidersDTO provider;

    @Schema(description = "تاريخ الموعد بالصيغة YYYY-MM-DD", example = "2024-12-31")
    @Future(message = "يجب أن يكون التاريخ في المستقبل")
    @NotNull(message = "التاريخ مطلوب")
    private LocalDate date;

    @Schema(description = "وقت الموعد بالصيغة HH:MM", example = "14:30")
    @Future(message = "يجب أن يكون الوقت في المستقبل")
    @NotNull(message = "الوقت مطلوبة")
    private LocalTime time;

    @Schema(description = "ملاحظات إضافية حول الموعد", example = "يرجى الحضور قبل الموعد بعشر دقائق")
    private String note;
    private Payment payment;
    @Schema(description = "حالة الموعد (مثل: PENDING, CONFIRMED, CANCELLED)", example = "PENDING")
    @NotNull(message = "الحالة مطلوب")
    // @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELLED)$", message = "يجب أن يكون
    // النوع أحد القيم التالية: PENDING, CONFIRMED, CANCELLED")
    private AppointmentStatus status;

    List<Notification> notifications = new ArrayList<>();
}
