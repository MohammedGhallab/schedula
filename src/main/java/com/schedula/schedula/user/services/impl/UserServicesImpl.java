package com.schedula.schedula.user.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.services.UserServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public UserDTO getUserById(UUID id, String details) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public UserDTO saveUser(UserDTO user) {
       UserDTO createdUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(user)));
       return createdUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public UserDTO updateUser(UserDTO entity) {
        UserDTO updatedUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(entity)));
        return updatedUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public void deleteUser(UserDTO id) {
        userRepository.deleteById(id.getId());
    }

}
