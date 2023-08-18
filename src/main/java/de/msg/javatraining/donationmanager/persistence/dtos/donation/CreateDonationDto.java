package de.msg.javatraining.donationmanager.persistence.dtos.donation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDonationDto {
    private String currency;
    private float amount;
    private boolean approved;
    private String notes;
    private User createdBy;
    private Campaign campaign;
    private Donator benefactor;
}
