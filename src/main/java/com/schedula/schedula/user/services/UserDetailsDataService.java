package com.schedula.schedula.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.repositories.UserRepository;
import com.schedula.schedula.user.repositories.Projection.UserLoginProjection;

@Service
public class UserDetailsDataService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. جلب البيانات باستخدام الـ Projection (استعلام واحد فقط)
        UserLoginProjection userProjection = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. تحويل الدور إلى Authority
        // SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userProjection.getRole());

        // 3. إرجاع الكائن المخصص الذي يحمل "الاسم" و "الحالة"
        return new CustomUserDetails(
                userProjection.getId(),
                userProjection.getEmail(),
                userProjection.getPassword(),
                userProjection.getActive(), // سيتم فحصها تلقائياً بواسطة Spring Security
                userProjection.getRole(),
                userProjection.getName(),
                userProjection.getEmail());
    }
}
/*
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.security.core.GrantedAuthority;
 * import org.springframework.security.core.userdetails.UserDetails;
 * import org.springframework.security.core.userdetails.UserDetailsService;
 * import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.stereotype.Service;
 * 
 * import com.schedula.schedula.user.models.dto.LoginResponse;
 * import com.schedula.schedula.user.repositories.UserRepository;
 * import
 * com.schedula.schedula.user.repositories.Projection.UserLoginProjection;
 * 
 * @Service
 * public class UserDetailsDataService implements UserDetailsService {
 * 
 * @Autowired
 * private UserRepository userRepository;
 * 
 * @Override
 * public UserDetails loadUserByUsername(String email) throws
 * UsernameNotFoundException {
 * UserLoginProjection userProjection = userRepository.findByEmail(email)
 * .orElseThrow(() -> new RuntimeException("User Not Found in Load"));
 * LoginResponse user = new LoginResponse();
 * user.setPassword(userProjection.getPassword());
 * user.setName(userProjection.getName());
 * user.setEmail(userProjection.getEmail());
 * user.setRole(userProjection.getRole());
 * user.setActive(userProjection.getActive());
 * if (user.getActive() == false) {
 * user = null;
 * }
 * if (user == null) {
 * throw new UsernameNotFoundException("User not found");
 * }
 * 
 * List<GrantedAuthority> authorities = new java.util.ArrayList<>();
 * // authorities.add(new SimpleGrantedAuthority("ROLE_" +
 * // user.getRoles().get(0)));
 * return new
 * org.springframework.security.core.userdetails.User(user.getEmail(),
 * user.getPassword(),
 * authorities);
 * }
 * 
 * 
 * @Bean
 * private Collection<? extends GrantedAuthority> getAuthorities(UserModel user)
 * {
 * List<SimpleGrantedAuthority> authorities = new ArrayList<>();
 * for (UserRole role : user.getRoles()) {
 * authorities.add(new SimpleGrantedAuthority(role.getName()));
 * }
 * return authorities;
 * 
 * }
 * 
 * }
 */