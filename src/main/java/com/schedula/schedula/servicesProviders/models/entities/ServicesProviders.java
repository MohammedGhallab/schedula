package com.schedula.schedula.servicesProviders.models.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.schedula.schedula.providers.models.entities.Providers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "services_providers")
@Getter
@Setter
public class ServicesProviders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
     private double price;
    @Column(nullable = false)
    private String duration;
    @Column(nullable = false)
    private boolean active;

    // علاقة "متعدد إلى واحد"
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false) // هذا هو العمود الذي سيربط الجدولين في DB
    @JsonBackReference
    private Providers providers;

    private String createdAt;
    private String updatedAt;
}
