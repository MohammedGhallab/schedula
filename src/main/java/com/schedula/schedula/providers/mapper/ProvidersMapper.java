package com.schedula.schedula.providers.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.models.entities.Providers;

@Mapper(componentModel = "spring")
public interface ProvidersMapper {
    Providers toEntity(ProvidersDTO provider);
    ProvidersDTO toDTO(Providers provider);
    List<ProvidersDTO> toDTOList(List<Providers> list);
}
