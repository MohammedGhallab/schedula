package com.schedula.schedula.notification.models.dto;

import java.time.LocalDateTime;

import com.schedula.schedula.appointment.models.entities.Appointment;
import com.schedula.schedula.user.models.dto.UserDTO;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private String channel;
    private String message;
    private LocalDateTime sentAt;
    private UserDTO user;
    private Appointment appointment;
}
