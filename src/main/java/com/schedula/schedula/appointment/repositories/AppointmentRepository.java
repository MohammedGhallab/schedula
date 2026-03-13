package com.schedula.schedula.appointment.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByUserId(UUID userId);
    List<Appointment> findByProviderId(UUID providerId);
    List<Appointment> findByStatus(AppointmentStatus status);
    
    boolean existsByProviderIdAndDateAndTime(UUID providerId, LocalDate date, LocalTime time);
}
