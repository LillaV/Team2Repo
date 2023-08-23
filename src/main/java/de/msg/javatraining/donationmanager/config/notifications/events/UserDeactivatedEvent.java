package de.msg.javatraining.donationmanager.config.notifications.events;

import de.msg.javatraining.donationmanager.persistence.model.User;
import lombok.Getter;

@Getter
public class UserDeactivatedEvent extends BaseEvent{
    User user;
    public UserDeactivatedEvent(User user){
        super(EventType.USER_DEACTIVATED);
        this.user = user;

    }
}
