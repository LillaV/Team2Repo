package de.msg.javatraining.donationmanager.service.validation;

import de.msg.javatraining.donationmanager.config.exception.InvalidDonationException;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonationValidator {
    private static void isValidAmount(float amount) {
        if (amount <= 0){
            throw new InvalidDonationException("Invalid amount");
        }
    }
    public static void isValidCurrency(String input) {
        Pattern pattern = Pattern.compile("^[A-Z]{3}$");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidDonationException("Invalid currency format");
        }
    }

    private static void existsCampaign(Campaign campaign) {
        if (campaign == null){
            throw new InvalidDonationException("Campaign is required");
        }
    }

    private static void existsTechnicalUser(User user) {
        if (user == null){
            throw new InvalidDonationException("Technical user is required");
        }
    }

    private static void existsCreationDate(LocalDate createdDate){
        if (createdDate == null){
            throw new InvalidDonationException("Creation date is missing");
        }
    }

    private static void existsBenefactor(Donator donator) {
        if (donator == null){
            throw new InvalidDonationException("Benefactor is required");
        }
    }

    public static void donationValidation(Donation donation) {
        isValidCurrency(donation.getCurrency());
        isValidAmount(donation.getAmount());
        existsCampaign(donation.getCampaign());
        existsTechnicalUser(donation.getCreatedBy());
        existsCreationDate(donation.getCreateDate());
        existsBenefactor(donation.getBenefactor());
    }
}
