package com.schedula.schedula.appointment.services.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.appointment.mapper.AppointmentMapper;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.appointment.repositories.AppointmentRepository;
import com.schedula.schedula.appointment.services.AppointmentServices;
import com.schedula.schedula.enums.AppointmentStatus;
import com.schedula.schedula.user.models.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServicesImpl implements AppointmentServices {
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public void deleteAppointment(AppointmentDTO id) {
        appointmentRepository.deleteById(id.getId());
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public AppointmentDTO getAppointmentById(AppointmentDTO id) {
        return appointmentMapper.toDTO(appointmentRepository.findById(id.getId()).orElse(null));
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<AppointmentDTO> getAllAppointments(Pageable page) {
        List<Appointment> appointments = appointmentRepository.findAll(page).getContent();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<AppointmentDTO> getAppointmentsByUserId(Pageable page, User user) {
        List<Appointment> appointments = appointmentRepository.findByUserId(user.getId()).findAll();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<AppointmentDTO> getAppointmentsByProviderId(Pageable page, AppointmentDTO providerId) {
        List<Appointment> appointments = appointmentRepository.findByProviderId(providerId.getId()).findAll();
        return appointmentMapper.toDTOList(appointments);
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<AppointmentDTO> getAppointmentsByStatus(Pageable page, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status).findAll();
        return appointmentMapper.toDTOList(appointments);
    }

}
