package de.msg.javatraining.donationmanager.persistence.dtos.user;

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
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Set<Long> rolesIDs = new HashSet<>();
    private Set<Long> campaignIDs = new HashSet<>();

}