package com.schedula.schedula.providers.models.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schedula.schedula.appointment.models.entities.Appointment;

import lombok.Data;

@Data
public class ProvidersDTO {
    private UUID id;
    private String name;
    private String specialty;
    private BigDecimal price;
    private Boolean active = true;
    private List<Appointment> appointments = new ArrayList<>();
}
