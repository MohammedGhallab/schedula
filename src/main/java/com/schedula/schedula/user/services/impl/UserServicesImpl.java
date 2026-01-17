package com.schedula.schedula.user.services.impl;

// import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.LoginResponse;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.repositories.Projection.UserLoginProjection;
import com.schedula.schedula.user.services.UserServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public UserDTO getUserById(UUID id, String details) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public UserDTO saveUser(UserDTO user) {
        user.setPassword(encoder.encode(user.getPassword()));
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

    @Override
    public LoginResponse login(LoginRequset data) {
        UserLoginProjection userProjection = userRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setPassword(userProjection.getPassword());
        loginResponse.setName(userProjection.getName());
        loginResponse.setEmail(userProjection.getEmail());
        loginResponse.setRole(userProjection.getRole());
        loginResponse.setActive(userProjection.getActive());
        return loginResponse;

    }

}
