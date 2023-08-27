package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationPaginationDTO;
import de.msg.javatraining.donationmanager.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {
    @Mock
    NotificationService notificationService;

    @InjectMocks
    NotificationController notificationController;

    private List<NotificationDTO> generateDtos(){
        List<NotificationDTO> list=new ArrayList<>();
        NotificationDTO notifDto=new NotificationDTO("Success", LocalDate.now());
        list.add(notifDto);
        return list;
    }

    @Test
    public void myNotifications_returnsPaginationDto_inAllCases(){
        List<NotificationDTO> dtos=generateDtos();
        NotificationPaginationDTO paginationDTO=new NotificationPaginationDTO(dtos, 5L);

        when(notificationService.getNotifications(5,3,1L)).thenReturn(paginationDTO);

        NotificationPaginationDTO res=notificationController.myNotifications(5,3,1L);

        verify(notificationService).getNotifications(5,3,1L);
        assertEquals(paginationDTO,res);
    }

    @Test
    public void myNotifications_returnsList_whenOffsetAndSizeNotNull(){
        List<NotificationDTO> dtos=generateDtos();

        when(notificationService.getNotificationsWithPagination(1L,0,5)).thenReturn(dtos);

        List<NotificationDTO> res=notificationController.myNotifications(1L,0,5);

        verify(notificationService).getNotificationsWithPagination(1L,0,5);
        assertEquals(dtos,res);
    }

    @Test
    public void myNotifications_returnsList_whenOffsetAndSizeNull(){
        List<NotificationDTO> dtos=generateDtos();

        when(notificationService.getNotificationsWithPagination(1L,0,3)).thenReturn(dtos);

        List<NotificationDTO> res=notificationController.myNotifications(1L,null,null);

        verify(notificationService).getNotificationsWithPagination(1L,0,3);
        assertEquals(dtos,res);
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(notificationService.getSize(1L)).thenReturn(5L);
        Long res=notificationController.getSize(1L);
        verify(notificationService).getSize(1L);
        assertEquals(5L,res);
    }
}