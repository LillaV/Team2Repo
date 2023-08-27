package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DonationValidator {
    private void isValidAmount(float amount) {
        if (amount <= 0){
            throw new InvalidRequestException("Invalid amount");
        }
    }
    public void isValidCurrency(String input) {
        Pattern pattern = Pattern.compile("^[A-Z]{3}$");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidRequestException("Invalid currency format");
        }
    }

    private void existsCampaign(Campaign campaign) {
        if (campaign == null){
            throw new InvalidRequestException("Campaign is required");
        }
    }

    private void existsTechnicalUser(User user) {
        if (user == null){
            throw new InvalidRequestException("Technical user is required");
        }
    }

    private void existsCreationDate(LocalDate createdDate){
        if (createdDate == null){
            throw new InvalidRequestException("Creation date is missing");
        }
    }

    private void existsBenefactor(Donator donator) {
        if (donator == null){
            throw new InvalidRequestException("Benefactor is required");
        }
    }

    public void validate(Donation donation) {
        isValidCurrency(donation.getCurrency());
        isValidAmount(donation.getAmount());
        existsCampaign(donation.getCampaign());
        existsTechnicalUser(donation.getCreatedBy());
        existsCreationDate(donation.getCreateDate());
        existsBenefactor(donation.getBenefactor());
    }
}
