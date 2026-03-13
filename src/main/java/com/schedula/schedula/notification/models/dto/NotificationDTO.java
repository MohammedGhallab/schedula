package com.schedula.schedula.notification.models.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.user.models.dto.UserDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private UUID id;
    private String title;
    private String message;
    private String channel;
    private boolean isRead;
    private LocalDateTime sentAt;
    private UserDTO user;
    private Appointment appointment;
}
