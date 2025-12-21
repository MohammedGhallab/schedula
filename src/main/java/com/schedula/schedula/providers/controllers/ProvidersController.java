package com.schedula.schedula.providers.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.providers.models.dto.ProvidersDTO;
import com.schedula.schedula.providers.services.ProvidersServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/providers")
@Tag(name = "إدارة المزودين", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المزودين")
@RequiredArgsConstructor
public class ProvidersController {

    private final ProvidersServices providerServices;

    @GetMapping("/{id}")
    @Operation(summary = "جلب بيانات مزود", description = "يقوم بإرجاع مزود بناءً على الرقم التعريفي")
    public ProvidersDTO getProviderById(@PathVariable Long id) {
        return providerServices.getProviderById(id);
    }

    @PostMapping
    @Operation(summary = "إنشاء مزود جديد", description = "يقوم بإنشاء مزود جديد بناءً على البيانات المقدمة")
    public ProvidersDTO createProvider(@RequestBody ProvidersDTO provider) {
        return providerServices.saveProviders(provider);
    }

    @PutMapping
    @Operation(summary = "تحديث مزود", description = "يقوم بتحديث مزود بناءً على البيانات المقدمة")
    public ProvidersDTO updateProvider(@RequestBody ProvidersDTO provider) {
        return providerServices.updateProvider(provider);
    }

    @DeleteMapping
    @Operation(summary = "حذف مزود", description = "يقوم بحذف مزود بناءً على الرقم التعريفي")
    public void deleteProvider(@RequestBody ProvidersDTO provider) {
        providerServices.deleteProvider(provider);
    }
}
