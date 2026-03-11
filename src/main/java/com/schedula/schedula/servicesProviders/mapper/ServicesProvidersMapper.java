package com.schedula.schedula.servicesProviders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;

@Mapper(componentModel = "spring")
public interface ServicesProvidersMapper {

    @Mapping(target = "providers", ignore = true) // نمنع تحويل الـ providers هنا
    ServicesProvidersDTO toDTO(ServicesProviders servicesProviders);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "providers", ignore = true) // نمنع تحويل الـ providers هنا
    ServicesProviders toEntity(ServicesProvidersDTO servicesProvidersDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "providers", ignore = true)
    void updateEntityFromDto(ServicesProvidersDTO dto, @MappingTarget ServicesProviders entity);
}
