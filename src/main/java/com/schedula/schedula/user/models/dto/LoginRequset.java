package com.schedula.schedula.user.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequset {
    @Schema(description = "البريد الإلكتروني", example = "ahmed@example.com")
    @Email(message = "يرجى إدخال بريد إلكتروني صحيح")
    @NotEmpty(message = "البريد الإلكتروني مطلوب")
    private String email;
    @Schema(description = "كلمة المرور", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "كلمة المرور مطلوبة")
    private String password;
    @NotEmpty
    private String captcha;
    private Boolean rememberMe;
}
