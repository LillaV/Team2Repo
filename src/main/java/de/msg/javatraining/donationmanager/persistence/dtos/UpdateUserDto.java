package de.msg.javatraining.donationmanager.persistence.dtos;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import jakarta.persistence.*;
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
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private boolean active;
    private boolean newUser;
    private String email;
    private String phoneNumber;
    private Set<Campaign> campaigns = new HashSet<>();
    private String password;
    private Set<Role> roles = new HashSet<>();
}
