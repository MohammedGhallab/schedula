package com.schedula.schedula.user.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.LoginResponse;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.repositories.Projection.UserLoginProjection;
import com.schedula.schedula.user.services.UserServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authManager;

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public UserDTO getUserById(UUID id, String details) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public UserDTO saveUser(UserDTO user) {
        user.setPassword(encoder.encode(user.getPassword()));
        UserDTO createdUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(user)));
        return createdUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(UserDTO userDto) {
        // 1. جلب المستخدم الحالي من قاعدة البيانات باستخدام المعرف (ID)
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("المستخدم غير موجود"));
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setRole(userDto.getRole());
        existingUser.setActive(userDto.getActive());

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public LoginResponse login(LoginRequset data) {
        UserLoginProjection userProjection = userRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> new RuntimeException("UserNotFound"));
        System.out.println("print err : " + data.getPassword());
        // cacheManager.getCache("findByUsername").clear();
        if (userProjection != null) {
            if (userProjection.getActive() == true) {
                Authentication authentication = authManager
                        .authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
                if (authentication.isAuthenticated()) {
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setPassword(userProjection.getPassword());
                    loginResponse.setName(userProjection.getName());
                    loginResponse.setEmail(userProjection.getEmail());
                    loginResponse.setRole(userProjection.getRole());
                    loginResponse.setActive(userProjection.getActive());
                    return loginResponse;
                    // String token = jwtService.generateToken(user.getUsername(),
                    // user.isRememberMe());
                    // return new ResponseEntity<String>(
                    // token, HttpStatus.OK);
                } else {
                    throw new RuntimeException("InvalidPassword");
                }
            } else {
                throw new RuntimeException("UserNotActive");
            }
        } else {
            throw new RuntimeException("UserNotFound");
        }

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndActive(email, true).get();
    }

}
