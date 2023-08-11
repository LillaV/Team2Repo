package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.RefreshTokenRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityServiceFactory implements ISecurityServiceFactory {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public RefreshTokenRepository getRefreshTokenRepository() {
        return refreshTokenRepository;
    }
}
