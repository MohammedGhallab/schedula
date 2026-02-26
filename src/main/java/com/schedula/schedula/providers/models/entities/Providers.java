package com.schedula.schedula.providers.models.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;
import com.schedula.schedula.user.models.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "providers")
@Getter
@Setter
public class Providers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean active = true;

    // علاقة "واحد إلى متعدد"
    // mappedBy تشير إلى اسم الحقل في كلاس Service
    @OneToMany(mappedBy = "providers", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // الطرف القائد
    private List<ServicesProviders> services;
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
