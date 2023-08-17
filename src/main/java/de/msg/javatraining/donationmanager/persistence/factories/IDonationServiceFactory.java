package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;

public interface IDonationServiceFactory {
    DonationRepository getDonationRepository();
}
