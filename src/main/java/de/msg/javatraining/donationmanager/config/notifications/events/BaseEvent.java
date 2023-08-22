package de.msg.javatraining.donationmanager.config.notifications.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseEvent {
    private EventType type;
}
