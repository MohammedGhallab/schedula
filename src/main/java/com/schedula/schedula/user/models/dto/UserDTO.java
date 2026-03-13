package com.schedula.schedula.user.models.dto;

import java.util.UUID;
import com.schedula.schedula.user.models.OnCreate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Schema(description = "كلمة المرور", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "كلمة المرور مطلوبة", groups = OnCreate.class)
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
}