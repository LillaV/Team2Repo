package de.msg.javatraining.donationmanager.persistence.dtos.donation;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
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
