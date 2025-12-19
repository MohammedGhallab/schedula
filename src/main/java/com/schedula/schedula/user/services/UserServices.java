package com.schedula.schedula.user.services;

import com.schedula.schedula.user.models.dto.UserDTO;

public interface UserServices {
    UserDTO getUserById(Long id, String details);
    UserDTO createUser(UserDTO user);
    UserDTO updateUser(String id, UserDTO entity);
    void deleteUser(String id);
    
}
