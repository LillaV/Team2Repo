package de.msg.javatraining.donationmanager.config.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@Slf4j
public class FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public void sendNotificationByToken(NotificationMessage message){
        Notification notification = Notification.builder()
                .setTitle(message.getTitle())
                .setBody(message.getBody())
                .setImage(message.getImage())
                .build();
        Message msg = Message.builder()
                .setTopic(message.getTopic())
                .setNotification(notification)
                .putAllData(message.getData())
                .build();
        firebaseMessaging.sendAsync(msg);
    }

    public void  subscribeToTopic(SubscriptionRequest subscriptionRequest){
        try {
            firebaseMessaging.subscribeToTopic(Collections.singletonList(subscriptionRequest.getSubscriber()),subscriptionRequest.getTopic());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void  unsubscribeFromTopic(SubscriptionRequest subscriptionRequest){
        try {
            firebaseMessaging.unsubscribeFromTopic(Collections.singletonList(subscriptionRequest.getSubscriber()),subscriptionRequest.getTopic());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
