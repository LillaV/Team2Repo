package de.msg.javatraining.donationmanager.config.notifications.events;


import de.msg.javatraining.donationmanager.persistence.model.User;
import lombok.Getter;


@Getter
public class NewUserEvent extends BaseEvent{
    private  final User user;
    public NewUserEvent(User user){
        super(EventType.WELCOME_NEW_USER);
        this.user = user;
    }
}
