package de.msg.javatraining.donationmanager.persistence.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NotificationPaginationDTO {
    private List<NotificationDTO> notifications;
    private Long maxPages;

}
