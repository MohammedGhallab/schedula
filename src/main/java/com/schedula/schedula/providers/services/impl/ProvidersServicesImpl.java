package com.schedula.schedula.providers.services.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.providers.mapper.ProvidersMapper;
import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.providers.repositories.ProvidersRepository;
import com.schedula.schedula.providers.services.ProvidersServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvidersServicesImpl implements ProvidersServices {

    private final ProvidersMapper providersMapper;
    private final ProvidersRepository providersRepository;

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public ProvidersDTO saveProviders(ProvidersDTO providersDTO) {
      Providers providers = providersMapper.toEntity(providersDTO);
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
    public ProvidersDTO getProviderById(Long id) {
        return providersMapper.toDTO(providersRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public ProvidersDTO updateProvider(ProvidersDTO providersDTO) {
        Providers providers = providersRepository.findById(providersDTO.getId()).orElse(null);
        providersMapper.toEntity(providersDTO);
        return providersMapper.toDTO(providersRepository.save(providers));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public void deleteProvider(ProvidersDTO data) {
        providersRepository.deleteById(data.getId());
    }

}