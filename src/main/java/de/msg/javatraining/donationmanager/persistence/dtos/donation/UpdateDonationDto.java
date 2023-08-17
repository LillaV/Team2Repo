package de.msg.javatraining.donationmanager.persistence.dtos.donation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDonationDto {
    private String currency;
    private float amount;
    private String notes;
    private Campaign campaign;
    private Donator benefactor;
    private boolean approved;
}
