package com.schedula.schedula.user.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.user.models.OnCreate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    @Schema(description = "معرف المستخدم قيمة فريدة", example = "105", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "اسم المستخدم الكامل", example = "أحمد محمد")
    @NotBlank(message = "الاسم لا يمكن أن يكون فارغاً")
    @Size(min = 3, max = 50, message = "الاسم يجب أن يكون بين 3 و 50 حرف")
    private String name;

    @Schema(description = "البريد الإلكتروني", example = "ahmed@example.com")
    @Email(message = "يرجى إدخال بريد إلكتروني صحيح")
    @NotBlank(message = "البريد الإلكتروني مطلوب")
    private String email;

    // يعني تظهر عند الإرسال فقط ولا تعود في الاستجابة
    @Schema(description = "كلمة المرور", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "كلمة المرور مطلوبة", groups = OnCreate.class)
    // @Pattern(regexp =
    // "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message =
    // "كلمة المرور ضعيفة! يجب أن تحتوي على 6 خانات على الأقل، تشمل أحرفاً وأرقاماً
    // ورموزاً خاصة")
    private String password;

    @Pattern(regexp = "^\\d{10}$", message = "رقم الهاتف يجب أن يتكون من 10 أرقام")
    @Schema(description = "رقم الجوال", example = "0123456789")
    @NotBlank(message = "رقم الجوال مطلوب", groups = OnCreate.class)
    private String phone;

    @Schema(description = "الصلاحيات الممنوحة للمستخدم", example = "ADMIN, CLIENT, PROVIDER")
    @NotBlank(message = "نوع المستخدم مطلوب")
    @Pattern(regexp = "^(ADMIN|CLIENT|PROVIDER)$", message = "يجب أن يكون النوع أحد القيم التالية قيمة واحدة فقط: ADMIN, CLIENT, PROVIDER")
    private String role;
    private Boolean active;

    private List<Providers> providers = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
}