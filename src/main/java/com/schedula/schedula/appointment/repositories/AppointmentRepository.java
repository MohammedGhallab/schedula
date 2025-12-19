package com.schedula.schedula.appointment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    JpaRepository<Appointment, Long> findByUserId(Long userId);
    JpaRepository<Appointment, Long> findByProviderId(Long providerId);
    JpaRepository<Appointment, Long> findByStatus(AppointmentStatus status);
}
