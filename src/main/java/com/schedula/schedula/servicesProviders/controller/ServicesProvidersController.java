package com.schedula.schedula.servicesProviders.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;
import com.schedula.schedula.servicesProviders.services.ServicesProvidersServices;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/service-providers") // مسار معياري واحترافي
@RequiredArgsConstructor
@Tag(name = "Service Providers", description = "إدارة مزودي الخدمات")
public class ServicesProvidersController {

    private final ServicesProvidersServices servicesProvidersServices;

    // جلب الكل - جعلنا المعرف اختيارياً أو أزلناه ليكون منطقياً
    @GetMapping
    public ResponseEntity<List<ServicesProvidersDTO>> getServicesProvidersById(@RequestParam(required = true) String filter) {
        return ResponseEntity.ok(servicesProvidersServices.getServicesProvidersById(UUID.fromString(filter)));
    }

    // جلب واحد - استخدام PathVariable بدلاً من Body
    @GetMapping("/{id}")
    public ResponseEntity<List<ServicesProvidersDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(servicesProvidersServices.getServicesProvidersById(id));
    }

    @PostMapping
    public ResponseEntity<ServicesProvidersDTO> create(@Valid @RequestBody ServicesProvidersDTO dto) {
        ServicesProvidersDTO created = servicesProvidersServices.createServicesProviders(dto);
        // الحالة 201 تعني "تم الإنشاء بنجاح"
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicesProvidersDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ServicesProvidersDTO dto) {
        // نمرر الـ ID من المسار والبيانات من الـ Body لضمان الأمان
        return ResponseEntity.ok(servicesProvidersServices.updateServicesProviders(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        servicesProvidersServices.deleteServicesProviders(id);
        // الحالة 204 تعني "تم التنفيذ بنجاح ولا يوجد محتوى للإرجاع"
        return ResponseEntity.noContent().build();
    }
}