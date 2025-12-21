package com.schedula.schedula.providers.services;

import java.util.List;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;

public interface ProvidersServices {
    ProvidersDTO saveProviders(ProvidersDTO providersDTO);
    List<ProvidersDTO> getAllProviders();
    ProvidersDTO getProviderById(Long id);
    ProvidersDTO updateProvider(Long id, ProvidersDTO providersDTO);
    void deleteProvider(Long id);
}
