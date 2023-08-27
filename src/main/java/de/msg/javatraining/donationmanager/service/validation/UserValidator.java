package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private void containsOnlyLetters(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(input);
        if(!matcher.matches()){
            throw new InvalidRequestException("Names shouldn't contain numbers");
        }
    }

    private void validPhoneNumber(String input) {
        Pattern pattern = Pattern.compile("^(00407|07|\\+407)\\d{8}$");
        Matcher matcher = pattern.matcher(input);
        if(!matcher.matches()){
            throw new InvalidRequestException("Invalid mobile number");
        }
    }

    private void isValidEmail(String email) {
        String regex="^[A-Za-z0-9+_.-]+@[^@]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new InvalidRequestException("Invalid email!");
        }
    }

    public void validate(User user) {
         containsOnlyLetters(user.getFirstName());
         containsOnlyLetters(user.getLastName());
         isValidEmail(user.getEmail());
         validPhoneNumber(user.getMobileNumber());
    }

}
