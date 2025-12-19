package com.schedula.schedula.appointment.models.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.user.models.entities.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;  

    private LocalDate date;

    private LocalTime time;

    @Column(length = 300)
    private String note;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
      // Many Appointments → One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // One Appointment → Many Notifications
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

}
