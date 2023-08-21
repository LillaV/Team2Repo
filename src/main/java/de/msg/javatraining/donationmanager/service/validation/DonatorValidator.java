package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DonatorValidator {
    private boolean containsOnlyLetters(String input) {
        // Define a regular expression pattern that matches only letters (A-Z, a-z)
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(input);
        System.out.println("For lastname and firstName the matcher returns" + matcher.matches());
        return matcher.matches();
    }

    public boolean validate(Donator donator) {
        return containsOnlyLetters(donator.getFirstName()) && containsOnlyLetters(donator.getLastName());
    }
}
