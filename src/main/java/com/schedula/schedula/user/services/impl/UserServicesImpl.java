package com.schedula.schedula.user.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.services.UserServices;

public class UserServicesImpl implements UserServices {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDTO getUserById(Long id, String details) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserDTO createUser(UserDTO user) {
       UserDTO createdUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(user)));
       return createdUser;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO entity) {
        UserDTO updatedUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(entity)));
        return updatedUser;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(Long.parseLong(id));
    }

}
