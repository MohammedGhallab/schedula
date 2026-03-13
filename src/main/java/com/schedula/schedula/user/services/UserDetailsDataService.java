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
        UserLoginProjection userProjection = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(
                userProjection.getId(),
                userProjection.getEmail(),
                userProjection.getPassword(),
                userProjection.getActive(),
                userProjection.getRole(),
                userProjection.getName(),
                userProjection.getEmail());
    }
}