package com.schedula.schedula.appointment.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.appointment.mapper.AppointmentMapper;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.appointment.repositories.AppointmentRepository;
import com.schedula.schedula.appointment.services.AppointmentServices;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.notification.services.NotificationService;
import com.schedula.schedula.user.models.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServicesImpl implements AppointmentServices {
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        // Conflict Detection
        boolean hasConflict = appointmentRepository.existsByProviderIdAndDateAndTime(
            appointmentDTO.getProvider().getId(), 
            appointmentDTO.getDate(), 
            appointmentDTO.getTime()
        );
        
        if (hasConflict) {
            throw new RuntimeException("Provider already has an appointment at this time");
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        Appointment saved = appointmentRepository.save(appointment);
        
        // Trigger Notification
        notificationService.sendNotification(
            appointmentDTO.getUser().getId(),
            "موعد جديد",
            "تم حجز موعدك بنجاح بتاريخ " + appointmentDTO.getDate()
        );
        
        return appointmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void deleteAppointment(AppointmentDTO id) {
        appointmentRepository.deleteById(id.getId());
    }

    @Override
    @Transactional(readOnly = true) 
    public AppointmentDTO getAppointmentById(UUID id) {
        return appointmentMapper.toDTO(appointmentRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments(Pageable page) {
        List<Appointment> appointments = appointmentRepository.findAll(page).getContent();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) 
    public List<AppointmentDTO> getAppointmentsByUserId(Pageable page, User user) {
        List<Appointment> appointments = appointmentRepository.findByUserId(user.getId());
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) 
    public List<AppointmentDTO> getAppointmentsByProviderId(Pageable page, AppointmentDTO providerId) {
        List<Appointment> appointments = appointmentRepository.findByProviderId(providerId.getId());
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) 
    public List<AppointmentDTO> getAppointmentsByStatus(Pageable page, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return appointmentMapper.toDTOList(appointments);
    }

}
