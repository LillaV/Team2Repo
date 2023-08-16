package de.msg.javatraining.donationmanager.persistence.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {

    private Long id;
    private String name;
    private String purpose;
}
