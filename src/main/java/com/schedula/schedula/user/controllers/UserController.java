package com.schedula.schedula.user.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.services.UserServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "إدارة المستخدمين", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المستخدمين")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @GetMapping("/{id}")
    @Operation(summary = "جلب بيانات مستخدم", description = "يقوم بإرجاع مستخدم بناءً على الرقم التعريفي")
    public UserDTO getUserById(
            @Parameter(description = "الرقم التعريفي للمستخدم (يجب أن يكون رقمًا صحيحًا)", example = "105", required = true) @PathVariable UUID id,
            @Parameter(description = "تحديد مستوى التفاصيل (simple أو full)", example = "full") @RequestParam(defaultValue = "simple") String details) {
        return userServices.getUserById(id, details);
    }

    @PostMapping
    @Operation(summary = "إنشاء مستخدم جديد")
    public UserDTO createUser(@Valid @RequestBody UserDTO user) {
        return userServices.saveUser(user);
    }

    @PutMapping
    @Operation(summary = "تحديث بيانات المستخدم")
    public UserDTO updateUser(@Valid @RequestBody UserDTO entity) {
        return userServices.updateUser(entity);
    }

    @DeleteMapping
    @Operation(summary = "حذف المستخدم")
    public void deleteUser(@RequestBody UserDTO id) {
        userServices.deleteUser(id);
    }
}