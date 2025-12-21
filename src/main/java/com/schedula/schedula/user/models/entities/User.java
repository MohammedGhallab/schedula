package com.schedula.schedula.user.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.notification.models.entities.Notification;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "الاسم لا يمكن أن يكون فارغاً")
    @Size(min = 3, max = 50, message = "الاسم يجب أن يكون بين 3 و 50 حرف")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "يرجى إدخال بريد إلكتروني صحيح")
    @NotEmpty(message = "البريد الإلكتروني مطلوب")
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "كلمة المرور مطلوبة")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "كلمة المرور ضعيفة! يجب أن تحتوي على 6 خانات على الأقل، تشمل أحرفاً وأرقاماً ورموزاً خاصة")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "الصلاحيات الممنوحة للمستخدم مطلوبة")
    @Pattern(regexp = "^(ADMIN|CLIENT|PROVIDER)$", message = "يجب أن يكون النوع أحد القيم التالية قيمة واحدة فقط: ADMIN, CLIENT, PROVIDER")
    private String role; // ADMIN, CLIENT, PROVIDER
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d{10}$", message = "رقم الهاتف يجب أن يحتوي على 10 أرقام")
    private String phone;
    // User 1 → Many Appointments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    // User 1 → Many Notifications
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @PrePersist
    private void defaultActive() {
        this.active = true;
    }
}
