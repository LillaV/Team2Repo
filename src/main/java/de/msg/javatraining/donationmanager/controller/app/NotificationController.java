package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationPaginationDTO;
import de.msg.javatraining.donationmanager.persistence.model.Notification;
import de.msg.javatraining.donationmanager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    NotificationService service;
    @GetMapping("/{load}/{number}/{userId}")
    public NotificationPaginationDTO myNotifications(@PathVariable("load") int load, @PathVariable("number") int number, @PathVariable("userId") long userId){
        return service.getNotifications(load,number,userId);
    }

//    @GetMapping("/{userId}")
//    public List<NotificationDTO> myNotifications(@PathVariable("userId") Long userId){
//        //return service.recentNotifications(userId);
//        return service.getNotificationsWithPagination(userId, 0, 3);
//    }
//
    @GetMapping("/{userId}")
    public List<NotificationDTO> myNotifications(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "pageSize", required = false) Integer pageSize){
        if (offset != null && pageSize != null) {
            return service.getNotificationsWithPagination(userId,offset,pageSize);
        } else {
            return service.getNotificationsWithPagination(userId, 0, 3);
        }
    }

    @GetMapping("/size/{userId}")
    public long getSize(@PathVariable("userId") Long userId) { return service.getSize(userId);}
}
