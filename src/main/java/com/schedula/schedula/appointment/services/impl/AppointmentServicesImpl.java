package com.schedula.schedula.appointment.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.schedula.schedula.appointment.mapper.AppointmentMapper;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.appointment.repositories.AppointmentRepository;
import com.schedula.schedula.appointment.services.AppointmentServices;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.user.models.entities.User;

@Service
public class AppointmentServicesImpl implements AppointmentServices {
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentMapper.toDTO(appointmentRepository.findById(id).orElse(null));
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(Pageable page) {
        List<Appointment> appointments = appointmentRepository.findAll(page).getContent();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByUserId(User user) {
       List<Appointment> appointments = appointmentRepository.findByUserId(user.getId()).findAll();
       return appointmentMapper.toDTOList(appointments);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByProviderId(Long providerId) {
        List<Appointment> appointments = appointmentRepository.findByProviderId(providerId).findAll();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status).findAll();
        return appointmentMapper.toDTOList(appointments);
    }

}
