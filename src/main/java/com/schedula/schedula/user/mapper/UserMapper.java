package com.schedula.schedula.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);

    List<UserDTO> toDTOList(List<User> users);
}
