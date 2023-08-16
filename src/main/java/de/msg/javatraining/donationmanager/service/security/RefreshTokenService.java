package de.msg.javatraining.donationmanager.service.security;

import de.msg.javatraining.donationmanager.config.exception.InvalidRefreshTokenException;
import de.msg.javatraining.donationmanager.config.security.JwtUtils;
import de.msg.javatraining.donationmanager.persistence.factories.ISecurityServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.RefreshToken;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Autowired
    ISecurityServiceFactory factory;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public void createRefreshToken(String uuid,Long userId){
        Optional<User> user = factory.getUserRepository().findById(userId);
        if(user.isPresent()) {
            RefreshToken refreshToken = new RefreshToken(uuid,user.get(), Instant.now().plusSeconds(84000));
            factory.getRefreshTokenRepository().save(refreshToken);
        }
    }

    public String exchangeRefreshToken(String refreshToken){
        Optional<RefreshToken> token = factory.getRefreshTokenRepository().findById(refreshToken);
        if(!token.isPresent()){
            throw new InvalidRefreshTokenException("The refresh token is inexistent");
        }
        if(token.get().getExpiryDate().isBefore(Instant.now())){
            throw new InvalidRefreshTokenException("The refresh token is expired");
        }
        return jwtUtils.generateJwtToken(userDetailsService.loadUserByUsername(token.get().getUser().getUsername()));
    }

    public void deleteRefreshTokenForUser(Long userId) {
        factory.getRefreshTokenRepository().deleteRefreshTokenByUser(userId);
    }

}
