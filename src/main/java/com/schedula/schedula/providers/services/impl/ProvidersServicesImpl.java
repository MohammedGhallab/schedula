package com.schedula.schedula.providers.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.providers.mapper.ProvidersMapper;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.providers.repositories.ProvidersRepository;
import com.schedula.schedula.providers.services.ProvidersServices;
import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.models.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvidersServicesImpl implements ProvidersServices {

    private final ProvidersMapper providersMapper;
    private final ProvidersRepository providersRepository;

    @Override
    @Transactional
    @CacheEvict(value = "getAllProvidersByUserCache", keyGenerator = "userKeyGenerator")
    public ProvidersDTO saveProviders(ProvidersDTO providersDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UUID userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        User user = new User();
        user.setId(userId);

        Providers providers = providersMapper.toEntity(providersDTO);
        providers.setUser(user);
        providersRepository.save(providers);

        return providersMapper.toDTO(providers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvidersDTO> getAllProviders(Pageable page) {
        return providersMapper.toDTOList(providersRepository.findAll(page).getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public ProvidersDTO getProviderById(UUID id) {
        return providersMapper.toDTO(providersRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    @CacheEvict(value = "getAllProvidersByUserCache", keyGenerator = "userKeyGenerator")
    public ProvidersDTO updateProvider(ProvidersDTO providersDTO) {
        Providers providers = providersRepository.findById(providersDTO.getId()).orElse(null);
        providers.setName(providersDTO.getName());
        providers.setPrice(providersDTO.getPrice());
        providers.setSpecialty(providersDTO.getSpecialty());
        providersRepository.save(providers);
        return providersDTO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "getAllProvidersByUserCache", keyGenerator = "userKeyGenerator")
    public void deleteProvider(UUID data) {
        providersRepository.deleteById(data);
    }

    @Override
    @Cacheable(value = "getAllProvidersByUserCache", keyGenerator = "userKeyGenerator")
    public List<ProvidersDTO> getAllProvidersByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UUID userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        User user = new User();
        user.setId(userId);
        // TODO العمل على التحجيم
        // Pageable page = PageRequest.of(0, 10);
        return providersMapper.toDTOList(providersRepository.findAllByUser(user));
    }

}