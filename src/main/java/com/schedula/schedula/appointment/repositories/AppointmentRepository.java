package com.schedula.schedula.appointment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    JpaRepository<Appointment, UUID> findByUserId(UUID userId);
    JpaRepository<Appointment, UUID> findByProviderId(UUID providerId);
    JpaRepository<Appointment, UUID> findByStatus(AppointmentStatus status);
}
