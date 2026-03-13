package com.schedula.schedula.user.services.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.mapper.UserMapper;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.services.UserServices;

import com.schedula.schedula.config.Security.LoginAttemptService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final LoginAttemptService loginAttemptService;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id, String details) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Not found user by : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(Pageable pageable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> users = userMapper.toDTOList(userPage.getContent());

        stopWatch.stop();
        log.info("time get user: {} ms", stopWatch.getTotalTimeMillis());
        return users;
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setId(null);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO dto) {
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Update filed, User not exit"));

        userMapper.updateEntityFromDto(dto, existingUser);

        return userMapper.toDTO(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Delete filed, User not exit");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails login(LoginRequset data) {
        log.info("User try login: {}", data.getEmail());

        if (loginAttemptService.isBlocked(data.getEmail())) {
            log.warn("تم حظر حساب المستخدم {} بسبب كثرة المحاولات الفاشلة", data.getEmail());
            throw new RuntimeException(
                    "تم حظر الحساب مؤقتاً بسبب كثرة محاولات الدخول الفاشلة. حاول مرة أخرى بعد 15 دقيقة.");
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));

            stopWatch.stop();
            log.info("login succsfull in {} ms", stopWatch.getTotalTimeMillis());

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            loginAttemptService.loginSucceeded(data.getEmail());

            return userDetails;

        } catch (BadCredentialsException e) {
            log.warn("فشل تسجيل الدخول: كلمة المرور خاطئة للمستخدم {}", data.getEmail());
            loginAttemptService.loginFailed(data.getEmail());
            throw new RuntimeException("البريد الإلكتروني أو كلمة المرور غير صحيحة");
        } catch (DisabledException e) {
            log.warn("حساب المستخدم {} معطل", data.getEmail());
            throw new RuntimeException("هذا الحساب معطل، يرجى التواصل مع الإدارة");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndActive(email, true)
                .orElseThrow(() -> {
                    log.error("not found user by email: {}", email);
                    return new EntityNotFoundException("not found user or is not active");
                });
    }
}