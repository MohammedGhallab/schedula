package com.schedula.schedula.servicesProviders.services;

import java.util.List;
import java.util.UUID;

import com.schedula.schedula.servicesProviders.models.dto.ServicesProvidersDTO;

public interface ServicesProvidersServices {
    // جلب الكل بناءً على المعرف (أو بدون فلتر)
    List<ServicesProvidersDTO> getAllServicesProviders();

    List<ServicesProvidersDTO> getServicesProvidersById(UUID id);

    ServicesProvidersDTO createServicesProviders(ServicesProvidersDTO dto);

    // تحديث يستقبل المعرف بشكل منفصل لضمان عدم التلاعب بالبيانات
    ServicesProvidersDTO updateServicesProviders(UUID id, ServicesProvidersDTO dto);

    void deleteServicesProviders(UUID id);
}
