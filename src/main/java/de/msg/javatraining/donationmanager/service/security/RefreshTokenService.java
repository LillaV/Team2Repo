package de.msg.javatraining.donationmanager.service.security;

import de.msg.javatraining.donationmanager.config.exception.InvalidRefreshTokenException;
import de.msg.javatraining.donationmanager.config.security.JwtUtils;
import de.msg.javatraining.donationmanager.persistence.model.RefreshToken;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.RefreshTokenRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public void createRefreshToken(String uuid, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            RefreshToken refreshToken = new RefreshToken(user.get(), uuid, Instant.now().plusSeconds(84000));
            refreshTokenRepository.save(refreshToken);
        }
    }

    public String exchangeRefreshToken(String refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepository.findById(refreshToken);
        if (!token.isPresent()) {
            throw new InvalidRefreshTokenException("The refresh token is inexistent");
        }
        if (token.get().getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("The refresh token is expired");
        }
        return jwtUtils.generateJwtToken(userDetailsService.loadUserByUsername(token.get().getUser().getUsername()));
    }

    public void deleteRefreshTokenForUser(Long userId) {
        refreshTokenRepository.deleteRefreshTokenByUser(userId);
    }

}
