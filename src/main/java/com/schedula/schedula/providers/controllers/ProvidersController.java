package com.schedula.schedula.providers.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/providers")
@Tag(name = "إدارة المزودين", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المزودين")
@RequiredArgsConstructor
public class ProvidersController {

    private final ProvidersServices providerServices;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'PROVIDER')")
    @Operation(summary = "جلب بيانات جميع المزودين", description = "يقوم بإرجاع جميع المزودين بناءً على الرقم التعريفي")
    public List<ProvidersDTO> getAllByUser() {
        return providerServices.getAllProvidersByUser();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'PROVIDER')")
    @Operation(summary = "جلب بيانات مزود", description = "يقوم بإرجاع مزود بناءً على الرقم التعريفي")
    public ProvidersDTO getProviderById(@PathVariable String id) {
        return providerServices.getProviderById(UUID.fromString(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "إنشاء مزود جديد", description = "يقوم بإنشاء مزود جديد بناءً على البيانات المقدمة")
    @com.schedula.schedula.audit.annotations.Auditable(action = "PROVIDER_CREATED")
    public ProvidersDTO createProvider(@Valid @RequestBody ProvidersDTO provider) {
        return providerServices.saveProviders(provider);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    @Operation(summary = "تحديث مزود", description = "يقوم بتحديث مزود بناءً على البيانات المقدمة")
    @com.schedula.schedula.audit.annotations.Auditable(action = "PROVIDER_UPDATED")
    public ProvidersDTO updateProvider(@Valid @RequestBody ProvidersDTO provider) {
        return providerServices.updateProvider(provider);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "حذف مزود", description = "يقوم بحذف مزود بناءً على الرقم التعريفي")
    @com.schedula.schedula.audit.annotations.Auditable(action = "PROVIDER_DELETED")
    public void deleteProvider(@RequestBody UUID provider) {
        providerServices.deleteProvider(provider);
    }
}
