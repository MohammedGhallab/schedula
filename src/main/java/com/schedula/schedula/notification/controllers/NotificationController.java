package com.schedula.schedula.notification.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.schedula.schedula.notification.models.dto.NotificationDTO;
import com.schedula.schedula.notification.services.NotificationService;
import com.schedula.schedula.core.DynamicResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Endpoints for managing user notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get user notifications", description = "Retrieves all notifications for a specific user")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'PROVIDER')")
    public ResponseEntity<?> getNotificationsByUserId(@PathVariable UUID userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", notifications);
        response.put("message", "تم جلب الإشعارات بنجاح");
        return new DynamicResponseEntity(HttpStatus.OK, null, response);
    }

    @Operation(summary = "Mark notification as read", description = "Sets the read status of a notification to true")
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'PROVIDER')")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "تم تحديث حالة الإشعار بنجاح");
        return new DynamicResponseEntity(HttpStatus.OK, null, response);
    }
}
