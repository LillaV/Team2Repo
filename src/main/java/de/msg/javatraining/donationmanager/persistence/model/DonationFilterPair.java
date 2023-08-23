package de.msg.javatraining.donationmanager.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DonationFilterPair {
    private List<Donation> donations;
    private Integer totalItems;
}
