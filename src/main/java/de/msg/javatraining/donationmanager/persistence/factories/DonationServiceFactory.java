package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonationServiceFactory implements IDonationServiceFactory{
    @Autowired
    DonationRepository donationRepository;

    @Override
    public DonationRepository getDonationRepository() { return donationRepository; }

}
