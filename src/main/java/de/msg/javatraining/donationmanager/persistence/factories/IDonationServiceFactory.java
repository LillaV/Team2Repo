package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;

public interface IDonationServiceFactory {
    DonationRepository getDonationRepository();

    CampaignRepository getCampaignRepository();

    UserRepository getUserRepository();
}
