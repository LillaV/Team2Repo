package de.msg.javatraining.donationmanager.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return  DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return  encodedPassword.equals(rawPassword.toString());
    }
}
