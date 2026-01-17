package com.schedula.schedula.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.config.JWT.JWTService;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.LoginResponse;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.services.UserServices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServices userServices;
    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequset data, HttpServletResponse response) {
        LoginResponse login = userServices.login(data);
        String token = jwtService.generateToken(login.getEmail(), false);
        System.out.println("the token it : " + token);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS only
        cookie.setPath("/");
        if (data.getRememberMe())
            cookie.setMaxAge(60 * 60 * 24 * 7); // 1 week
        else
            cookie.setMaxAge(60 * 60); // 1 hour

        response.addCookie(cookie);
        login.setPassword("Pass Is Hash");
        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO data, HttpServletResponse response) {
        data = userServices.saveUser(data);
        if (data.getId() != null) {
            String token = jwtService.generateToken(data.getEmail(), false);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS only
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 hour
            response.addCookie(cookie);
        }
        data.setPassword("Pass Is Hash");
        return ResponseEntity.ok(data);
    }
}
