package de.msg.javatraining.donationmanager.controller.notification;

import de.msg.javatraining.donationmanager.config.notifications.FirebaseMessagingService;
import de.msg.javatraining.donationmanager.config.notifications.NotificationMessage;
import de.msg.javatraining.donationmanager.config.notifications.SubscriptionRequest;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    FirebaseMessagingService service;

    @PostMapping("/subscribe")
    public ResponseEntity<TextResponse> subscribeToTopic(@RequestBody SubscriptionRequest request){
        this.service.subscribeToTopic(request);
        return new ResponseEntity<>(new TextResponse("Subscribed"), HttpStatus.OK);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<TextResponse> unsubscribeToTopic(@RequestBody SubscriptionRequest request){
        this.service.subscribeToTopic(request);
        return new ResponseEntity<>(new TextResponse("Unsubscribed"), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TextResponse> sendMessage(@RequestBody NotificationMessage notificationMessage){
        return new ResponseEntity<>(new TextResponse("SENT"), HttpStatus.OK);
    }
}
