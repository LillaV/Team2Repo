package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;

public interface IDonatorServiceFactory {
    DonatorRepository getDonatorRepository();
}
