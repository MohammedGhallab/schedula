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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;
    @Column(nullable = false)
    @NotBlank(message = "Duration is required")
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
