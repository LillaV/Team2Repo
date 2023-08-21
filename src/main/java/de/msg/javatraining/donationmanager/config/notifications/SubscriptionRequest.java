package de.msg.javatraining.donationmanager.config.notifications;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String subscriber;
    private String topic;
}
