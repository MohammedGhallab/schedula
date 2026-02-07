package com.schedula.schedula.servicesProviders.models.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServicesProvidersDTO {
    private UUID id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;
    @NotBlank(message = "Duration is required")
    private String duration;
    @NotNull(message = "Active is required")
    private boolean active;
}
