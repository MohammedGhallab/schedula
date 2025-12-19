package com.schedula.schedula.user.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.user.models.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "إدارة المستخدمين", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المستخدمين")
@RestController()
public class UserController {
    @GetMapping("/users/{id}")
    @Operation(summary = "جلب بيانات مستخدم", description = "يقوم بإرجاع مستخدم بناءً على الرقم التعريفي")
    public String getUserById(
            // TODO: process GET request USER BY ID
            // 1. توثيق Path Variable (جزء من الرابط)
            @Parameter(description = "الرقم التعريفي للمستخدم (يجب أن يكون رقمًا صحيحًا)", example = "105", required = true) @PathVariable Long id,

            // 2. توثيق Query Parameter (اختياري بعد علامة ؟)
            @Parameter(description = "تحديد مستوى التفاصيل (simple أو full)", example = "full") @RequestParam(defaultValue = "simple") String details) {
        return "User ID: " + id + ", Details: " + details;
    }

    @PostMapping("/users")
    @Operation(summary = "إنشاء مستخدم جديد")
    public String createUser(@Valid @RequestBody UserDTO user) {
        // TODO: process POST request USER
        return "تم إنشاء المستخدم: " + user.getName();
    }

    @PutMapping("users/{id}")
    public UserDTO updateUser(@Valid @RequestBody UserDTO entity) {
        // TODO: process PUT request USER
        return entity;
    }

    @DeleteMapping("users/{id}")
    public String deleteUser(@PathVariable String id) {
        // TODO: process DELETE USER
        return "تم حذف المستخدم: " + id;
    }
}