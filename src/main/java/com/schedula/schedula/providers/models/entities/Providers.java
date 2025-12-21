package com.schedula.schedula.providers.models.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.schedula.schedula.appointment.models.entities.Appointment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "providers")
@Data
public class Providers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();
}
