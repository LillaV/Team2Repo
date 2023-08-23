package de.msg.javatraining.donationmanager.config.notifications.events;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import lombok.Getter;

@Getter
public class DonationApprovedEvent extends BaseEvent{
    private Donation donation;
    public DonationApprovedEvent(Donation donation) {
        super(EventType.DONATION_APPROVED);
        this.donation = donation;
    }
}
