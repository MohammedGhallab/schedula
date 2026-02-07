package com.schedula.schedula.servicesProviders.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.providers.mapper.ProvidersMapper;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.servicesProviders.mapper.ServicesProvidersMapper;
import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;
import com.schedula.schedula.servicesProviders.repositories.ServicesProvidersRepository;
import com.schedula.schedula.servicesProviders.services.ServicesProvidersServices;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicesProvidersServicesImpl implements ServicesProvidersServices {
    private final ServicesProvidersMapper servicesProvidersMapper;
    private final ProvidersMapper providersMapper;
    private final ServicesProvidersRepository servicesProvidersRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServicesProvidersDTO> getAllServicesProviders(ProvidersDTO data) {
        return servicesProvidersRepository.findByProviders(providersMapper.toEntity(data)).stream()
                .map(servicesProvidersMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicesProvidersDTO getServicesProvidersById(UUID id) {
        return servicesProvidersRepository.findById(id).map(servicesProvidersMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("ServicesProviders not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServicesProvidersDTO createServicesProviders(ServicesProvidersDTO servicesProvidersDTO) {
        return servicesProvidersMapper
                .toDTO(servicesProvidersRepository.save(servicesProvidersMapper.toEntity(servicesProvidersDTO)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServicesProvidersDTO updateServicesProviders(ServicesProvidersDTO servicesProvidersDTO) {
        ServicesProviders servicesProviders = servicesProvidersRepository.findById(servicesProvidersDTO.getId())
                .orElseThrow(() -> new RuntimeException("ServicesProviders not found"));
        servicesProviders.setActive(servicesProvidersDTO.isActive());
        servicesProviders.setDescription(servicesProvidersDTO.getDescription());
        servicesProviders.setDuration(servicesProvidersDTO.getDuration());
        servicesProviders.setPrice(servicesProvidersDTO.getPrice());
        return servicesProvidersMapper.toDTO(servicesProvidersRepository.save(servicesProviders));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteServicesProviders(UUID id) {
        servicesProvidersRepository.deleteById(id);
    }

}
