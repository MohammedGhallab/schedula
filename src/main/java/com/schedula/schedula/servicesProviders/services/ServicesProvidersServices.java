package com.schedula.schedula.servicesProviders.services;

import java.util.List;
import java.util.UUID;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;

public interface ServicesProvidersServices {
    List<ServicesProvidersDTO> getAllServicesProviders(ProvidersDTO data);

    ServicesProvidersDTO getServicesProvidersById(UUID id);

    ServicesProvidersDTO createServicesProviders(ServicesProvidersDTO servicesProvidersDTO);

    ServicesProvidersDTO updateServicesProviders(ServicesProvidersDTO servicesProvidersDTO);

    void deleteServicesProviders(UUID id);
}
