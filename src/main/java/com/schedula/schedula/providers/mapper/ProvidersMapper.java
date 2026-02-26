package com.schedula.schedula.providers.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.models.entities.Providers;

@Mapper(componentModel = "spring")
public interface ProvidersMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Providers toEntity(ProvidersDTO provider);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    ProvidersDTO toDTO(Providers provider);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    List<ProvidersDTO> toDTOList(List<Providers> list);
}
