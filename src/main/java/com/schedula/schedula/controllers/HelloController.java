package com.schedula.schedula.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 1. @Tag: لتسمية ووصف مجموعة الـ API هذه (مثل: إدارة المستخدمين)
@Tag(name = "تجربة النظام", description = "واجهة خاصة لتجربة الـ Hello World")
public class HelloController {

    @GetMapping("/hello")
    // 2. @Operation: لوصف ماذا تفعل هذه الدالة بالتحديد
    @Operation(summary = "ترحيب بالمستخدم", description = "تعيد رسالة ترحيبية بسيطة")
    // 3. @ApiResponse: لوصف الاستجابة المتوقعة (مثل 200 OK)
    @ApiResponse(responseCode = "200", description = "تمت العملية بنجاح")
    public String sayHello() {
        return "Hello via Swagger!";
    }
}