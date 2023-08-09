package de.msg.javatraining.donationmanager.persistence.dtos;

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
public class UserDto {

    private Long id;


    private String username;


    private String email;


    private String password;

    private Set<Role> roles = new HashSet<>();

}
