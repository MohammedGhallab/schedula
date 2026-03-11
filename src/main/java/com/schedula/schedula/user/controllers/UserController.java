package com.schedula.schedula.user.controllers;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.user.models.OnCreate;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.services.UserServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/users") // استخدام إصدار للـ API
@RequiredArgsConstructor
@Tag(name = "إدارة المستخدمين")
public class UserController {

    private final UserServices userServices;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id,
            @RequestParam(defaultValue = "simple") String details) {
        return ResponseEntity.ok(userServices.getUserById(id, details));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "جلب المستخدمين بصفحات")
    public ResponseEntity<List<UserDTO>> getAllUsers(@ParameterObject Pageable pageable) {
        // @ParameterObject تجعل Swagger يعرض خيارات الـ Page و Size و Sort بشكل جميل
        return ResponseEntity.ok(userServices.getAllUsers(pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Validated(OnCreate.class) @RequestBody UserDTO user) {
        return new ResponseEntity<>(userServices.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user) {
        return ResponseEntity.ok(userServices.updateUser(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "حذف مستخدم بواسطة المعرف")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userServices.deleteUser(id);
        return ResponseEntity.noContent().build(); // إرجاع 204 No Content
    }
}