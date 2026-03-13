package com.schedula.schedula.notification.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schedula.schedula.notification.models.dto.NotificationDTO;
import com.schedula.schedula.notification.models.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    Notification toEntity(NotificationDTO dto);

    NotificationDTO toDTO(Notification notification);

    List<NotificationDTO> toDTOList(List<Notification> list);
}
