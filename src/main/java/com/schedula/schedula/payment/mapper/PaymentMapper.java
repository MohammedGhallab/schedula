package com.schedula.schedula.payment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.schedula.schedula.appointment.mapper.AppointmentMapper;
import com.schedula.schedula.payment.models.dto.PaymentDTO;
import com.schedula.schedula.payment.models.entities.Payment;
import com.schedula.schedula.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class, AppointmentMapper.class })
public interface PaymentMapper {
    Payment toEntity(PaymentDTO dto);

    PaymentDTO toDTO(Payment entity);

    List<Payment> toEntityList(List<PaymentDTO> dtoList);

    List<PaymentDTO> toDTOList(List<Payment> list);

    void updateEntityFromDto(PaymentDTO dto, @MappingTarget Payment entity);
}
