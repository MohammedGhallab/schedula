package com.schedula.schedula.appointment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.schedula.schedula.appointment.models.dto.AppointmentDTO;
import com.schedula.schedula.appointment.services.AppointmentServices;
import com.schedula.schedula.core.DynamicResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@Tag(name = "إدارة المواعيد", description = "عمليات إنشاء، تحديث، حذف، وجلب بيانات المواعيد")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentServices appointmentService;

    @GetMapping
    @Operation(summary = "جلب بيانات موعد", description = "يقوم بإرجاع موعد بناءً على الرقم التعريفي")
    public DynamicResponseEntity getAppointmentById(@RequestBody AppointmentDTO id) {
        return new DynamicResponseEntity(HttpStatus.OK, null, appointmentService.getAppointmentById(id));
    }

    @PostMapping
    @Operation(summary = "إنشاء موعد جديد")
    public DynamicResponseEntity createAppointment(@Valid @RequestBody AppointmentDTO appointment) {
        return new DynamicResponseEntity(HttpStatus.OK, null, appointmentService.saveAppointment(appointment));
    }

    @PutMapping
    public DynamicResponseEntity updateAppointment(@Valid @RequestBody AppointmentDTO entity) {
        return new DynamicResponseEntity(HttpStatus.OK, null, appointmentService.updateAppointment(entity));
    }

    @DeleteMapping
    public DynamicResponseEntity deleteAppointment(@RequestBody AppointmentDTO id) {
        appointmentService.deleteAppointment(id);
        return new DynamicResponseEntity(HttpStatus.OK, null, "Appointment deleted successfully");
    }
}
