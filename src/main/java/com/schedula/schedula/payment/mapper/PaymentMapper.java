package com.schedula.schedula.payment.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.schedula.schedula.payment.models.dto.PaymentDTO;
import com.schedula.schedula.payment.models.entities.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(Payment payment);

    List<Payment> toEntityList(List<Payment> payments);

    List<PaymentDTO> toDTOList(List<Payment> list);
}
