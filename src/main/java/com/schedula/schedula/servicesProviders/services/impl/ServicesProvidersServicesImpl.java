package com.schedula.schedula.servicesProviders.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.schedula.schedula.servicesProviders.mapper.ServicesProvidersMapper;
import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;
import com.schedula.schedula.servicesProviders.repositories.ServicesProvidersRepository;
import com.schedula.schedula.servicesProviders.services.ServicesProvidersServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicesProvidersServicesImpl implements ServicesProvidersServices {

    private final ServicesProvidersRepository repository;
    private final ServicesProvidersMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicesProvidersDTO> getAllServicesProviders() {

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public ServicesProvidersDTO createServicesProviders(ServicesProvidersDTO dto) {
        ServicesProviders entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    @Transactional
    public ServicesProvidersDTO updateServicesProviders(UUID id, ServicesProvidersDTO dto) {

        ServicesProviders existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("المزود غير موجود"));

        mapper.updateEntityFromDto(dto, existingEntity);

        return mapper.toDTO(repository.save(existingEntity));
    }

    @Override
    @Transactional
    public void deleteServicesProviders(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("المزود غير موجود");
        }
        repository.deleteById(id);
    }

    @Override
    public List<ServicesProvidersDTO> getServicesProvidersById(UUID id) {
        return repository.findByProvidersId(id)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}