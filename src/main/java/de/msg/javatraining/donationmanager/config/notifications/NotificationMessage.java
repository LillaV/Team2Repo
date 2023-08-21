package de.msg.javatraining.donationmanager.config.notifications;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationMessage {
    private String topic;
    private String title;
    private String body;
    private String image;
    private Map<String,String> data;
}
