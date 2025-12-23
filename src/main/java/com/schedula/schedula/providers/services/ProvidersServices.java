package com.schedula.schedula.providers.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;

public interface ProvidersServices {
    ProvidersDTO saveProviders(ProvidersDTO providersDTO);
    List<ProvidersDTO> getAllProviders(Pageable page);
    ProvidersDTO getProviderById(UUID id);
    ProvidersDTO updateProvider(ProvidersDTO providersDTO);
    void deleteProvider(ProvidersDTO id);
}
