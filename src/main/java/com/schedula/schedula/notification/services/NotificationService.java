package com.schedula.schedula.notification.services;

import java.util.List;
import java.util.UUID;

import com.schedula.schedula.notification.models.dto.NotificationDTO;

public interface NotificationService {
    void sendNotification(UUID userId, String title, String message);
    void sendEmail(String to, String subject, String body);
    List<NotificationDTO> getNotificationsByUserId(UUID userId);
    void markAsRead(UUID notificationId);
}
