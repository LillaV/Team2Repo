package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailUserService {

    @Autowired
    private JavaMailSender emailSender ;

    public String generateUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }
    public String sendSimpleMessage(CreateUserDto userDto){
        SimpleMailMessage message= new SimpleMailMessage();
        String passwd=generateUUID();
        message.setFrom("iana.baltes@gmail.com");
        message.setTo(userDto.getEmail());
        message.setSubject("Activate account");
        message.setText("Thank you for registering! For your first time logging in, you will have to use the following password: "+passwd);
        emailSender.send(message);
        return passwd;
    }
}
