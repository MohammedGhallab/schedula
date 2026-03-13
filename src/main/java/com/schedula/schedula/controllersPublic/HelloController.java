package com.schedula.schedula.controllersPublic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "تجربة النظام", description = "واجهة خاصة لتجربة الـ Hello World")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "ترحيب بالمستخدم", description = "تعيد رسالة ترحيبية بسيطة")
    @ApiResponse(responseCode = "200", description = "تمت العملية بنجاح")
    public String sayHello() {
        return "Hello via Swagger!";
    }
}