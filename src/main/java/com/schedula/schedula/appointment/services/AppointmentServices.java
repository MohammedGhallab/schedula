package com.schedula.schedula.appointment.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.user.models.entities.User;

public interface AppointmentServices {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
    AppointmentDTO getAppointmentById(Long id);
    List<AppointmentDTO> getAllAppointments(Pageable page);
    List<AppointmentDTO> getAppointmentsByUserId(User user);
    List<AppointmentDTO> getAppointmentsByProviderId(Long providerId);
    List<AppointmentDTO> getAppointmentsByStatus(AppointmentStatus status);

}
