package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DonatorValidator {
    private boolean containsOnlyLetters(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean validate(Donator donator) {
        return containsOnlyLetters(donator.getFirstName()) && containsOnlyLetters(donator.getLastName());
    }
}
