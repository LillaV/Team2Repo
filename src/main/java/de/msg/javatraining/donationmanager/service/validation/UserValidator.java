package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private boolean containsOnlyLetters(String input) {
        // Define a regular expression pattern that matches only letters (A-Z, a-z)
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean validPhoneNumber(String input) {
        Pattern pattern = Pattern.compile("^(00407|07|\\+407)\\d{8}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex="^[A-Za-z0-9+_.-]+@[^@]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validate(User user) {
        return containsOnlyLetters(user.getFirstName()) && containsOnlyLetters(user.getLastName())
                && isValidEmail(user.getEmail()) && validPhoneNumber(user.getMobileNumber());
    }

}
