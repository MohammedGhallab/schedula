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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j // للمراقبة والـ Logging الاحترافي
public class UserServicesImpl implements UserServices {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id, String details) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("المستخدم غير موجود بالمعرف: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(Pageable pageable) {
        StopWatch stopWatch = new StopWatch(); // أداة احترافية لحساب الوقت
        stopWatch.start();

        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> users = userMapper.toDTOList(userPage.getContent());

        // if (!users.isEmpty()) {
        //     users.get(0).setCountAll(userPage.getTotalElements());
        // }

        stopWatch.stop();
        log.info("جلب المستخدمين استغرق: {} ms", stopWatch.getTotalTimeMillis());
        return users;
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setId(null); // ضمان الإنشاء
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO dto) {
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("لا يمكن التحديث، المستخدم غير موجود"));

        // استخدام MapStruct للتحديث التلقائي بدلاً من الـ Set اليدوي
        userMapper.updateEntityFromDto(dto, existingUser);

        return userMapper.toDTO(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("لا يمكن الحذف، المستخدم غير موجود");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails login(LoginRequset data) {
        log.info("محاولة تسجيل دخول للمستخدم: {}", data.getEmail());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // 1. المصادقة باستخدام AuthenticationManager
            // سيقوم Spring Security بمقارنة كلمة المرور المشفرة والتأكد من حالة الحساب
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));

            stopWatch.stop();
            log.info("تمت المصادقة بنجاح في {} ms", stopWatch.getTotalTimeMillis());

            // 2. استخراج تفاصيل المستخدم من Principal
            return (CustomUserDetails) authentication.getPrincipal();

        } catch (BadCredentialsException e) {
            log.warn("فشل تسجيل الدخول: كلمة المرور خاطئة للمستخدم {}", data.getEmail());
            throw new RuntimeException("البريد الإلكتروني أو كلمة المرور غير صحيحة");
        } catch (DisabledException e) {
            log.warn("حساب المستخدم {} معطل", data.getEmail());
            throw new RuntimeException("هذا الحساب معطل، يرجى التواصل مع الإدارة");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        // استخدام Optional للتعامل مع الحالة التي قد لا يوجد فيها المستخدم
        return userRepository.findByEmailAndActive(email, true)
                .orElseThrow(() -> {
                    log.error("لم يتم العثور على مستخدم نشط بالبريد: {}", email);
                    return new EntityNotFoundException("المستخدم غير موجود أو غير مفعل");
                });
    }
}
/*
 * @Service
 * 
 * @RequiredArgsConstructor
 * public class UserServicesImpl implements UserServices {
 * private final UserMapper userMapper;
 * private final UserRepository userRepository;
 * private final BCryptPasswordEncoder encoder;
 * private final AuthenticationManager authManager;
 * 
 * @Override
 * 
 * @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
 * public UserDTO getUserById(UUID id, String details) {
 * return userMapper.toDTO(userRepository.findById(id).orElse(null));
 * }
 * 
 * @Override
 * 
 * @Transactional(readOnly = true) // للبحث فقط، أسرع وأخف على قاعدة البيانات
 * public List<UserDTO> getAllUsers(Pageable page) {
 * long start = System.currentTimeMillis();
 * Page<User> data = userRepository.findAll(page);
 * 
 * List<UserDTO> users = userMapper.toDTOList(data.getContent());
 * 
 * if (!users.isEmpty())
 * users.get(0).setCountAll(data.getTotalElements());
 * long end = System.currentTimeMillis();
 * System.out.println(TimeUnit.SECONDS.toMillis(1) +
 * " Process took: " + TimeUnit.MILLISECONDS.toMinutes((end - start)) + " m : "
 * + (end - start) + " ms");
 * return users;
 * }
 * 
 * @Override
 * 
 * @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
 * public UserDTO saveUser(UserDTO user) {
 * user.setPassword(encoder.encode(user.getPassword()));
 * UserDTO createdUser =
 * userMapper.toDTO(userRepository.save(userMapper.toEntity(user)));
 * return createdUser;
 * }
 * 
 * @Override
 * 
 * @Transactional(rollbackFor = Exception.class)
 * public UserDTO updateUser(UserDTO userDto) {
 * // 1. جلب المستخدم الحالي من قاعدة البيانات باستخدام المعرف (ID)
 * User existingUser = userRepository.findById(userDto.getId())
 * .orElseThrow(() -> new EntityNotFoundException("المستخدم غير موجود"));
 * existingUser.setName(userDto.getName());
 * existingUser.setEmail(userDto.getEmail());
 * existingUser.setRole(userDto.getRole());
 * existingUser.setActive(userDto.getActive());
 * 
 * User updatedUser = userRepository.save(existingUser);
 * 
 * return userMapper.toDTO(updatedUser);
 * }
 * 
 * @Override
 * 
 * @Transactional(rollbackFor = Exception.class) // تراجع في حال حدوث أي خطأ
 * public void deleteUser(UUID id) {
 * userRepository.deleteById(id);
 * }
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * public CustomUserDetails login(LoginRequset data) {
 * // 1. المصادقة أولاً: Spring Security سيقوم بجلب المستخدم والتحقق من كلمة
 * المرور
 * // والحالة (Active) في خطوة واحدة
 * // ملاحظة: يجب أن يكون الـ UserDetailsService لديك مهيأ ليرفض المستخدم غير
 * // المفعل
 * long start = System.currentTimeMillis();
 * Authentication authentication = authManager.authenticate(
 * new UsernamePasswordAuthenticationToken(data.getEmail(),
 * data.getPassword()));
 * long end = System.currentTimeMillis();
 * CustomUserDetails userDetails = (CustomUserDetails)
 * authentication.getPrincipal();
 * 
 * System.out.println(TimeUnit.SECONDS.toMillis(1) +
 * " Process took: " + TimeUnit.MILLISECONDS.toSeconds((end - start)) + " m : "
 * + (end - start) + " ms");
 * return userDetails;
 * }
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * public User getUserByEmail(String email) {
 * return userRepository.findByEmailAndActive(email, true).get();
 * }
 * 
 * }
 */