package com.schedula.schedula.appointment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.models.entities.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(target = "user.providers", ignore = true)
    Appointment toEntity(AppointmentDTO dto);

    List<AppointmentDTO> toDTOList(List<Appointment> appointments);
}
