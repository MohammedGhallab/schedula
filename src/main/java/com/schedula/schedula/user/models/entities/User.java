package com.schedula.schedula.user.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.notification.models.entities.Notification;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
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
