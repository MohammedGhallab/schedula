package com.schedula.schedula.user.services;

import java.util.UUID;

import com.schedula.schedula.user.models.dto.UserDTO;

public interface UserServices {
    UserDTO getUserById(UUID id, String details);
    UserDTO saveUser(UserDTO user);
    UserDTO updateUser(UserDTO entity);
    void deleteUser(UserDTO entity);
    
}
