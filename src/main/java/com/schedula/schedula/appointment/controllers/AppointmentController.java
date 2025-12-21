package com.schedula.schedula.appointment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.services.AppointmentServices;
import com.schedula.schedula.core.DynamicResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "إدارة المواعيد", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المواعيد")
public class AppointmentController {
    @Autowired
    private AppointmentServices appointmentService;

    @GetMapping("/appointments/{id}")
    @Operation(summary = "جلب بيانات موعد", description = "يقوم بإرجاع موعد بناءً على الرقم التعريفي")
    public DynamicResponseEntity getAppointmentById(
            // TODO: process GET request USER BY ID
            // 1. توثيق Path Variable (جزء من الرابط)
            @Parameter(description = "الرقم التعريفي للموعد (يجب أن يكون رقمًا صحيحًا)", example = "105", required = true) @PathVariable Long id,

            // 2. توثيق Query Parameter (اختياري بعد علامة ؟)
            @Parameter(description = "تحديد مستوى التفاصيل (simple أو full)", example = "full") @RequestParam(defaultValue = "simple") String details) {
        try {
            return new DynamicResponseEntity(HttpStatus.OK,null,appointmentService.getAppointmentById(id));
        } catch (Exception e) {
            return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error retrieving appointment");
        }
    }

    @PostMapping("/appointments")
    @Operation(summary = "إنشاء موعد جديد")
    public DynamicResponseEntity createAppointment(@Valid @RequestBody AppointmentDTO appointment) {
        try {
            return new DynamicResponseEntity(HttpStatus.OK,null,appointmentService.saveAppointment(appointment));
        } catch (Exception e) {
            return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error creating appointment");
        }
        
    }

    @PutMapping("appointments")
    public DynamicResponseEntity updateAppointment(@Valid @RequestBody AppointmentDTO entity) {
        try {
            return new DynamicResponseEntity(HttpStatus.OK,null,appointmentService.updateAppointment(entity));
        } catch (Exception e) {
            return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error updating appointment");
        }

    }

    @DeleteMapping("appointments/{id}")
    public DynamicResponseEntity deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return new DynamicResponseEntity(HttpStatus.OK,null,"Appointment deleted successfully");
        } catch (Exception e) {
            return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error deleting appointment");
        }
    }
}
