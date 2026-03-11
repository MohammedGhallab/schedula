package com.schedula.schedula.user.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.providers.models.entities.Providers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // توليد الـ Getters فقط
@Setter // توليد الـ Setters فقط
@NoArgsConstructor // مشيد فارغ ضروري لـ JPA
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "email_index", columnList = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role; // ADMIN, CLIENT, PROVIDER
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, unique = true)
    private String phone;

    // User 1 → Many Appointments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    // User 1 → Many Notifications
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // الطرف القائد
    @JsonIgnore // هذا يخبر Jackson: لا تقترب من هذا الحقل نهائياً عند إرسال الرد
    private List<Providers> providers = new ArrayList<>();
    @PrePersist
    private void defaultActive() {
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User user))
            return false;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // استبعد العلاقات من الـ toString لتجنب الـ Recursion أثناء الـ Logging
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + '}';
    }
}
