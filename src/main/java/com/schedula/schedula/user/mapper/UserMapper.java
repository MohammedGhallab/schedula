package com.schedula.schedula.user.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(target = "appointments",ignore = true)
    @Mapping(target = "notifications",ignore = true)
    @Mapping(target = "providers",ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    List<UserDTO> toDTOList(List<User> users);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}
