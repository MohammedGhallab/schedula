package com.schedula.schedula.notification.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.notification.models.entities.Notification;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.appointment.models.entities.Appointment;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);

    List<Notification> findByAppointment(Appointment appointment);

    List<Notification> findByUserAndAppointment(User user, Appointment appointment);
}
