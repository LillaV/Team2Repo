package de.msg.javatraining.donationmanager.persistence.dtos;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoCreate {
    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String email;

    private Set<Role> roles = new HashSet<>();
    private Set<Campaign> campaigns = new HashSet<>();

}
