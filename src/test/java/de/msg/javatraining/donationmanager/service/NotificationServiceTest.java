package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.NewUserEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.NotificationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Notification;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.NotificationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    NotificationMapper notificationMapper;

    @InjectMocks
    NotificationService notificationService;

    @Test
    public void handleNewUserEvent_savesNotification_whenGivenEvent(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<User> users=new HashSet<>();
        User user=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        users.add(user);
        NewUserEvent event=new NewUserEvent(user);
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);

        when(notificationRepository.save(notification)).thenReturn(notification);

        notificationService.handleNewUserEvent(event);

        verify(notificationRepository.save(notification));
    }

    @Test
    public void handleUpdatedUserEvent_savesNotification_whenGivenEvent(){
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());

    }
}