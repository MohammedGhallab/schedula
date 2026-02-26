package com.schedula.schedula.appointment.models.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.payment.models.entities.Payment;
import com.schedula.schedula.providers.models.entities.Providers;
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
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id", nullable = false)
  private Providers provider;

  private LocalDate date;
  private LocalTime time;
  private String note;
  @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
  private Payment payment;

  @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Notification> notifications = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private AppointmentStatus status;

}
