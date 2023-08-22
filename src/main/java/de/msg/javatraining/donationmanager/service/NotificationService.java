package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.DeletedUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.NewUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.UpdatedUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.UserDeactivatedEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.NotificationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.model.Notification;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import de.msg.javatraining.donationmanager.persistence.repository.NotificationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate ;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    NotificationMapper mapper;

    @EventListener(NewUserEvent.class)
    public void handleNewUserEvent(NewUserEvent event){
        Notification notification = new Notification();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Welcome ");
        messageBuilder.append(event.getUser().getFirstName());
        messageBuilder.append(" ");
        messageBuilder.append(event.getUser().getLastName());
        messageBuilder.append(".");
        notification.setText(messageBuilder.toString());
        notification.setUsers(new HashSet<>(Collections.singletonList(event.getUser())));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
    }

    @EventListener(UpdatedUserEvent.class)
    public  void handleUpdatedUserEvent(UpdatedUserEvent event){
        Notification notification = new Notification();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("User ");
        messageBuilder.append(event.getUsername());
        messageBuilder.append(".\n Previous data:\n");
        messageBuilder.append(event.getUser());
        messageBuilder.append("\nCurrent data:\n");
        messageBuilder.append(event.getUpdatedUser());
        notification.setText(messageBuilder.toString());
        notification.setDate(LocalDate.now());
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userUpdated",mapper.notificationToNotificationDTO(notification));
    }

    @EventListener(UserDeactivatedEvent.class)
    public void handleUserDeactivatedEvent(UserDeactivatedEvent event){
        Notification notification = new Notification();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("User with username ");
        messageBuilder.append(event.getUser().getUsername());
        messageBuilder.append(" was deactivated  because of 5  failed attempts to login.\n");
        notification.setText(messageBuilder.toString());
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userDeactivated",mapper.notificationToNotificationDTO(notification));
    }

    @EventListener(DeletedUserEvent.class)
    public void handleUserDeletedEvent(DeletedUserEvent event){
        Notification notification = new Notification();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("User with username ");
        messageBuilder.append(event.getUser().getUsername());
        messageBuilder.append(" was deleted\n");
        notification.setText(messageBuilder.toString());
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userDeleted",mapper.notificationToNotificationDTO(notification));
    }


    public List<NotificationDTO> getNotifications(int load, int number){
        Page<Notification> notifications = notificationRepository.findAll(PageRequest.of(load, number));
        return notifications.stream().map(notification -> mapper.notificationToNotificationDTO(notification)).collect(Collectors.toList());
    }
}
