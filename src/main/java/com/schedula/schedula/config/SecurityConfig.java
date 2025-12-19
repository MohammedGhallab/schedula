package com.schedula.schedula.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 1. إيقاف حماية CSRF (مطلوب عند تعطيل الأمن مؤقتًا)
        http.csrf(csrf -> csrf.disable());

        // 2. السماح لجميع الطلبات (All Requests) بالمرور
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }
}
