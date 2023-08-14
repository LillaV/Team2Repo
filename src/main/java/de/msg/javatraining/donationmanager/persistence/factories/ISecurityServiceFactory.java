package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.RefreshTokenRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;

public interface ISecurityServiceFactory {
    UserRepository getUserRepository();
    RefreshTokenRepository getRefreshTokenRepository();

}
