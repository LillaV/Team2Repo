package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.model.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {
    NotificationDTO notificationToNotificationDTO(Notification notification);
}
