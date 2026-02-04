package com.schedula.schedula.user.services;

import java.util.List;
import java.util.UUID;

import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.LoginResponse;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;

public interface UserServices {
    UserDTO getUserById(UUID id, String details);

    UserDTO saveUser(UserDTO user);

    UserDTO updateUser(UserDTO entity);

    void deleteUser(UUID entity);

    List<UserDTO> getAllUsers();

    LoginResponse login(LoginRequset data);

    User getUserByEmail(String email);
}
