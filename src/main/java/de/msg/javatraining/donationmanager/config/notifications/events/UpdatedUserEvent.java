package de.msg.javatraining.donationmanager.config.notifications.events;

import lombok.Getter;


@Getter
public class UpdatedUserEvent extends BaseEvent{
    private String updatedUser;
    private String user;
    private String username;
    public UpdatedUserEvent(String updatedUser, String user,String username) {
        super(EventType.USER_UPDATED);
        this.updatedUser=updatedUser;
        this.username = username;
        this.user = user;
    }
}
