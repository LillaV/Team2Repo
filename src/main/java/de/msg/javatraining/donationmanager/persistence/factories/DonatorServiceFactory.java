package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonatorServiceFactory implements IDonatorServiceFactory{
    @Autowired
    DonatorRepository donatorRepository;
    @Override
    public DonatorRepository getDonatorRepository() {
        return donatorRepository;
    }
}
