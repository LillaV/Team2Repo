package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonatorServiceFactory implements IDonatorServiceFactory{
    @Autowired
    DonatorRepository donatorRepository;
    @Autowired
    DonationRepository donationRepository;
    @Override
    public DonatorRepository getDonatorRepository() {
        return donatorRepository;
    }
    @Override
    public DonationRepository getDonationRepository() { return donationRepository; }
}
