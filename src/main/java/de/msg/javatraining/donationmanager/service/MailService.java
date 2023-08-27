package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    public String sendSimpleMessage(User user, String passwd) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("iana.baltes@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Activate account");
        message.setText("Hello " + user.getUsername() + "!\nThank you for registering! For your first time logging in, " + "you will have to use the following password:\n" + passwd);
        emailSender.send(message);
        return passwd;
    }
}
