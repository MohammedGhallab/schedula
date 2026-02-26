package com.schedula.schedula.user.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.services.UserServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public UserDTO getUserById(UUID id, String details) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
    public List<UserDTO> getAllUsers(Pageable page) {
        long start = System.currentTimeMillis();
        Page<User> data = userRepository.findAll(page);

        List<UserDTO> users = userMapper.toDTOList(data.getContent());

        if (!users.isEmpty())
            users.get(0).setCountAll(data.getTotalElements());
        long end = System.currentTimeMillis();
        System.out.println(TimeUnit.SECONDS.toMillis(1) +
                " Process took: " + TimeUnit.MILLISECONDS.toMinutes((end - start)) + " m : " + (end - start) + " ms");
        return users;
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
    @Transactional(readOnly = true)
    public CustomUserDetails login(LoginRequset data) {
        // 1. المصادقة أولاً: Spring Security سيقوم بجلب المستخدم والتحقق من كلمة المرور
        // والحالة (Active) في خطوة واحدة
        // ملاحظة: يجب أن يكون الـ UserDetailsService لديك مهيأ ليرفض المستخدم غير
        // المفعل
        long start = System.currentTimeMillis();
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
        long end = System.currentTimeMillis();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        System.out.println(TimeUnit.SECONDS.toMillis(1) +
                " Process took: " + TimeUnit.MILLISECONDS.toSeconds((end - start)) + " m : " + (end - start) + " ms");
        return userDetails;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndActive(email, true).get();
    }

}
