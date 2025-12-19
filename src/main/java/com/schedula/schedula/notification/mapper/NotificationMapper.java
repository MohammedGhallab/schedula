package com.schedula.schedula.notification.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schedula.schedula.notification.models.dto.NotificationDTO;
import com.schedula.schedula.notification.models.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "userId", source = "user.id")
    // NotificationDTO toDTO(Notification notification);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    Notification toEntity(NotificationDTO dto);

    List<NotificationDTO> toDTOList(List<Notification> list);
}
