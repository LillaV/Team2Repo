package de.msg.javatraining.donationmanager.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public String encode(CharSequence rawPassword) {
       return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
