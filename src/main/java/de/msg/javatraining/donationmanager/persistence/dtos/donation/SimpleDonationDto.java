package de.msg.javatraining.donationmanager.persistence.dtos.donation;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDonationDto {
    private String currency;
    private Float amount;
    private LocalDate createDate;
    private Boolean approved;
    private LocalDate approvedDate;
    private String notes;
    private User createdBy;
    private User approvedBy;
    private Campaign campaign;
    private Donator benefactor;
}
