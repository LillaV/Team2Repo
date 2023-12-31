package de.msg.javatraining.donationmanager.service.utils;

import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
public class UserServiceHelper {

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
        while (isUsernameTaken(username, users)) {
            username = originalUsername + number;
            number++;
        }
        return username;
    }

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
