package de.msg.javatraining.donationmanager.service.utils;

import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
public class UserServiceUtils {

    @Autowired
    private static JavaMailSender emailSender ;

    private boolean isUsernameTaken(String username, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public String generateUsername(User user, List<User> users) {
        String username = user.getLastName().substring(0, Math.min(5, user.getLastName().length())) + user.getFirstName().charAt(0);
        String originalUsername = username;

        int number = 1;
        while (isUsernameTaken(username,users)) {
            username = originalUsername + number;
            number++;
        }
        return username;
    }

    public static String generateUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }

    public static String sendSimpleMessage(User user, String passwd){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("iana.baltes@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Activate account");
        message.setText("Hello " + user.getUsername() +
                "!\nThank you for registering! For your first time logging in, " +
                "you will have to use the following password:\n"
                +passwd);
        emailSender.send(message);
        return passwd;
    }
}
