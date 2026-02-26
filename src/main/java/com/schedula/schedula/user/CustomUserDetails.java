package com.schedula.schedula.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {
    private final UUID id;
    private final String username;
    private final String password;
    private final String name;
    private final boolean active;
    private final String role;
    private final String email;

    // قمنا بإزالة authorities من الـ Fields لأنه يسبب مشاكل في Redis
    // وسنقوم بتوليده لحظياً من الـ role

    @JsonCreator
    public CustomUserDetails(
            @JsonProperty("id") UUID id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("active") boolean active,
            @JsonProperty("role") String role,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    @Override
    @JsonIgnore // نمنع Jackson من محاولة قراءة/كتابة هذا الحقل في Redis
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // يتم بناء الصلاحية من نص الـ role المخزن ببساطة
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore // أضف هذا
    public boolean isEnabled() {
        return active;
    }

    @Override
    @JsonIgnore // أضف هذا
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore // أضف هذا
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore // أضف هذا
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}