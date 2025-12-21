package com.schedula.schedula.appointment.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.user.models.entities.User;

public interface AppointmentServices {
    AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO);
    void deleteAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO getAppointmentById(AppointmentDTO id);
    List<AppointmentDTO> getAllAppointments(Pageable page);
    List<AppointmentDTO> getAppointmentsByUserId(Pageable page,User user);
    List<AppointmentDTO> getAppointmentsByProviderId(Pageable page,AppointmentDTO providerId);
    List<AppointmentDTO> getAppointmentsByStatus(Pageable page,AppointmentStatus status);

}
