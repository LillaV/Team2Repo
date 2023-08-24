package de.msg.javatraining.donationmanager.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.rmi.server.LogStream.log;


@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {
    private MessageDigest messageDigest;

    public CustomPasswordEncoder(){
        try {
            this.messageDigest= MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log("Error in the setup of the  db.Could not get  instance of MD5 algorithm");
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] messageDigest = this.messageDigest.digest(rawPassword.toString().getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        return no.toString(16);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return  encodedPassword.equals(rawPassword.toString());
    }
}
