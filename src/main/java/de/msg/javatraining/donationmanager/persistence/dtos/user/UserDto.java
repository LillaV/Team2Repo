package de.msg.javatraining.donationmanager.persistence.dtos.user;

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
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;
    private String email;
    private String mobileNumber;
    private Set<Role> roles = new HashSet<>();
    private Set<Campaign> campaigns;
}
