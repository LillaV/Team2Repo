package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.*;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.NotificationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationPaginationDTO;
import de.msg.javatraining.donationmanager.persistence.model.Notification;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import de.msg.javatraining.donationmanager.persistence.repository.NotificationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class NotificationService {
    @Autowired
    NotificationMapper mapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @EventListener(NewUserEvent.class)
    public void handleNewUserEvent(NewUserEvent event) {
        Notification notification = new Notification();
        String messageBuilder = "Welcome " + event.getUser().getFirstName() + " " + event.getUser().getLastName() + ".";
        notification.setText(messageBuilder);
        notification.setUsers(new HashSet<>(Collections.singletonList(event.getUser())));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
    }

    @EventListener(UpdatedUserEvent.class)
    public void handleUpdatedUserEvent(UpdatedUserEvent event) {
        Notification notification = new Notification();
        String messageBuilder = "User " + event.getUsername() + " was updated" + ".\nPrevious data:\n" + event.getUser() + "\nCurrent data:\n" + event.getUpdatedUser();
        notification.setText(messageBuilder);
        notification.setDate(LocalDate.now());
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userManagement", mapper.notificationToNotificationDTO(notification));
    }

    @EventListener(UserDeactivatedEvent.class)
    public void handleUserDeactivatedEvent(UserDeactivatedEvent event) {
        Notification notification = new Notification();
        String messageBuilder = "User with username " + event.getUser().getUsername() + " was deactivated  because of 5  failed attempts to login.\n";
        notification.setText(messageBuilder);
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userManagement", mapper.notificationToNotificationDTO(notification));
    }

    @EventListener(DeletedUserEvent.class)
    public void handleUserDeletedEvent(DeletedUserEvent event) {
        Notification notification = new Notification();
        String messageBuilder = "User with username " + event.getUser().getUsername() + " was deleted\n";
        notification.setText(messageBuilder);
        List<User> users = userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT);
        notification.setUsers(new HashSet<>(users));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/userManagement", mapper.notificationToNotificationDTO(notification));
    }

    @EventListener(DonationApprovedEvent.class)
    public void handleDonationApproved(DonationApprovedEvent event) {
        Notification notification = new Notification();
        String messageBuilder = "Your donation with id  " + event.getDonation().getId() + " created on " + event.getDonation().getCreateDate() + " was approved.";
        notification.setText(messageBuilder);
        notification.setUsers(new HashSet<>(Collections.singletonList(event.getDonation().getCreatedBy())));
        notification.setDate(LocalDate.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/" + event.getDonation().getCreatedBy().getUsername(), mapper.notificationToNotificationDTO(notification));
    }

    public List<NotificationDTO> recentNotifications(Long userId) {
        Page<Notification> notifications = notificationRepository.recentNotifications(userId, PageRequest.of(0, 3));
        return notifications.stream().map(mapper::notificationToNotificationDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsWithPagination(Long userId, int offset, int pageSize) {
        Page<Notification> notifications = notificationRepository.getUserNotifications(userId, PageRequest.of(offset, pageSize));
        return notifications.stream().map(mapper::notificationToNotificationDTO).collect(Collectors.toList());
    }

    public long getSize(Long userId) {
        return notificationRepository.getAllUserNotifications(userId).size();
    }
}
