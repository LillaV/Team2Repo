package de.msg.javatraining.donationmanager.config.notifications.events;

import de.msg.javatraining.donationmanager.persistence.model.User;
import lombok.Getter;

@Getter
public class DeletedUserEvent extends BaseEvent{
    private User user;
    public DeletedUserEvent(User user) {
        super(EventType.USER_DELETED);
        this.user = user;
    }
}
