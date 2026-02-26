package com.schedula.schedula.user.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;

public interface UserServices {
    UserDTO getUserById(UUID id, String details);

    UserDTO saveUser(UserDTO user);

    UserDTO updateUser(UserDTO entity);

    void deleteUser(UUID entity);

    List<UserDTO> getAllUsers(Pageable page);

    CustomUserDetails login(LoginRequset data);

    User getUserByEmail(String email);
}
