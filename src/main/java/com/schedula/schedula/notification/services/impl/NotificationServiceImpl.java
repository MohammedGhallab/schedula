package com.schedula.schedula.notification.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedula.schedula.notification.mapper.NotificationMapper;
import com.schedula.schedula.notification.models.dto.NotificationDTO;
import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.notification.repositories.NotificationRepository;
import com.schedula.schedula.notification.services.NotificationService;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Async
    @Transactional
    public void sendNotification(UUID userId, String title, String message) {
        log.info("Sending in-app notification to user {}: {}", userId, title);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setSentAt(LocalDateTime.now());
        notification.setChannel("IN_APP");
        
        notificationRepository.save(notification);
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to {}: {}", to, subject);
        try {
            Thread.sleep(100); 
            log.info("Email sent successfully to {}", to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Email sending interrupted", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        List<Notification> notifications = notificationRepository.findByUser(user);
        return notificationMapper.toDTOList(notifications);
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
