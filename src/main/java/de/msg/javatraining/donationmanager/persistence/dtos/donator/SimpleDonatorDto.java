package de.msg.javatraining.donationmanager.persistence.dtos.donator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDonatorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String additionalName;
    private String maidenName;
}
