package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.*;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.NotificationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import de.msg.javatraining.donationmanager.persistence.repository.NotificationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<User> users=new HashSet<>();
        List<User> userList=new ArrayList<>();
        User user=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        users.add(user);
        userList.add(user);
        UpdatedUserEvent event=new UpdatedUserEvent("user1","user1","andrban1");
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());

        when(userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT)).thenReturn(userList);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.notificationToNotificationDTO(notification)).thenReturn(notifDto);
        doNothing().when(messagingTemplate).convertAndSend("/topic/userManagement",notifDto);

        notificationService.handleUpdatedUserEvent(event);

        verify(notificationRepository.save(notification));
    }

    @Test
    public void handleUserDeactivatedEvent_savesNotification_whenGivenEvent(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<User> users=new HashSet<>();
        List<User> userList=new ArrayList<>();
        User user=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        users.add(user);
        userList.add(user);
        UserDeactivatedEvent event=new UserDeactivatedEvent(user);
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());

        when(userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT)).thenReturn(userList);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.notificationToNotificationDTO(notification)).thenReturn(notifDto);
        doNothing().when(messagingTemplate).convertAndSend("/topic/userManagement",notifDto);

        notificationService.handleUserDeactivatedEvent(event);

        verify(notificationRepository.save(notification));
    }

    @Test
    public void handleUserDeletedEvent_savesNotification_whenGivenEvent(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<User> users=new HashSet<>();
        List<User> userList=new ArrayList<>();
        User user=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        users.add(user);
        userList.add(user);
        DeletedUserEvent event=new DeletedUserEvent(user);
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());

        when(userRepository.findAllByPermissions(EPermission.AUTHORITY_USER_MANAGEMENT)).thenReturn(userList);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.notificationToNotificationDTO(notification)).thenReturn(notifDto);
        doNothing().when(messagingTemplate).convertAndSend("/topic/userManagement",notifDto);

        notificationService.handleUserDeletedEvent(event);

        verify(notificationRepository.save(notification));
    }

    @Test
    public void handleDonationApproved_savesNotification_whenGivenEvent(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<Donation> donations=new HashSet<>();
        Set<User> users=new HashSet<>();
        User user1=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        Campaign campaign=new Campaign(1L,"Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        Donation donation=new Donation(1L,"EUR",1000,LocalDate.now(),true,LocalDate.now(),"",user1,user2,campaign,donator);
        users.add(user1);
        DonationApprovedEvent event=new DonationApprovedEvent(donation);
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());

        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.notificationToNotificationDTO(notification)).thenReturn(notifDto);
        doNothing().when(messagingTemplate).convertAndSend("/topic/"+event.getDonation().getCreatedBy().getUsername(),notifDto);

        notificationService.handleDonationApproved(event);

        verify(notificationRepository.save(notification));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<User> users=new HashSet<>();
        List<Notification> notifications=new ArrayList<>();
        User user=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        users.add(user);
        Notification notification=new Notification(1L,"Success", LocalDate.now(),users);
        notifications.add(notification);

        when(notificationRepository.getAllUserNotifications(1L)).thenReturn(notifications);
        Long size=notificationService.getSize(1L);
        verify(notificationRepository).getAllUserNotifications(1L);
        assertEquals(1L,size);
    }
}