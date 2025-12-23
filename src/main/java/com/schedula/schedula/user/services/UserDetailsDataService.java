package com.schedula.schedula.user.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.UserRepository;

@Service
public class UserDetailsDataService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).get();
        if (user.getActive() == false) {
            user = null;
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoles().get(0)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    /*
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
     */
}
