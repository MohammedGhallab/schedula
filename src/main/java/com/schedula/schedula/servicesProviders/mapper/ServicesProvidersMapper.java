package com.schedula.schedula.servicesProviders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;

@Mapper(componentModel = "spring")
public interface ServicesProvidersMapper {
    ServicesProvidersDTO toDTO(ServicesProviders servicesProviders);

    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServicesProviders toEntity(ServicesProvidersDTO servicesProvidersDTO);
}
