package com.schedula.schedula.providers.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.providers.mapper.ProvidersMapper;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.providers.repositories.ProvidersRepository;
import com.schedula.schedula.providers.services.ProvidersServices;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.services.UserServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvidersServicesImpl implements ProvidersServices {

    private final ProvidersMapper providersMapper;
    private final ProvidersRepository providersRepository;
    private final UserServices userServices;

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public ProvidersDTO saveProviders(ProvidersDTO providersDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userServices.getUserByEmail(currentUserName);
            System.out.println("currentUserName : " + currentUserName);
        }
        Providers providers = providersMapper.toEntity(providersDTO);
        providers.setUser(user);
        providersRepository.save(providers);
        return providersMapper.toDTO(providers);
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<ProvidersDTO> getAllProviders(Pageable page) {
        return providersMapper.toDTOList(providersRepository.findAll(page).getContent());
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public ProvidersDTO getProviderById(UUID id) {
        return providersMapper.toDTO(providersRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public ProvidersDTO updateProvider(ProvidersDTO providersDTO) {
        Providers providers = providersRepository.findById(providersDTO.getId()).orElse(null);
        providers.setName(providersDTO.getName());
        providers.setPrice(providersDTO.getPrice());
        providers.setSpecialty(providersDTO.getSpecialty());
        providersRepository.save(providers);
        System.out.println("providersDTO : " + providersDTO);
        return providersDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public void deleteProvider(UUID data) {
        providersRepository.deleteById(data);
    }

    @Override
    public List<ProvidersDTO> getAllProvidersByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userServices.getUserByEmail(currentUserName);
            System.out.println("currentUserName : " + currentUserName);
        }
        return providersMapper.toDTOList(providersRepository.findAllByUser(user));
    }

}