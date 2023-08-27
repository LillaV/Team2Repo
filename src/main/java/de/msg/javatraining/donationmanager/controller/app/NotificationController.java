package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.notification.NotificationPaginationDTO;
import de.msg.javatraining.donationmanager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    NotificationService service;

    @GetMapping("/{userId}")
    public List<NotificationDTO> myNotifications(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return service.getNotificationsWithPagination(userId, offset, pageSize);
        } else {
            return service.recentNotifications(userId);
        }
    }

    @GetMapping("/size/{userId}")
    public long getSize(@PathVariable("userId") Long userId) {
        return service.getSize(userId);
    }
}
