package com.schedula.schedula.servicesProviders.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.services.ServicesProvidersServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servicesProviders")
@RequiredArgsConstructor
public class ServicesProvidersController {
    private final ServicesProvidersServices servicesProvidersServices;

    @GetMapping
    public List<ServicesProvidersDTO> getAllServicesProviders(
            @RequestParam String id) {
        ProvidersDTO providersDTO = new ProvidersDTO();
        providersDTO.setId(UUID.fromString(id));
        return servicesProvidersServices.getAllServicesProviders(providersDTO);
    }

    @GetMapping("/providers")
    public ServicesProvidersDTO getServicesProvidersById(@RequestBody String id) {
        return servicesProvidersServices.getServicesProvidersById(UUID.fromString(id));
    }

    @PostMapping
    public ServicesProvidersDTO createServicesProviders(@Valid @RequestBody ServicesProvidersDTO servicesProvidersDTO) {
        return servicesProvidersServices.createServicesProviders(servicesProvidersDTO);
    }

    @PutMapping
    public ServicesProvidersDTO updateServicesProviders(@Valid @RequestBody ServicesProvidersDTO servicesProvidersDTO) {
        return servicesProvidersServices.updateServicesProviders(servicesProvidersDTO);
    }

    @DeleteMapping
    public void deleteServicesProviders(@RequestBody String id) {
        servicesProvidersServices.deleteServicesProviders(UUID.fromString(id));
    }
}
