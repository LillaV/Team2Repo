package de.msg.javatraining.donationmanager.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailUserDto {

    private String username;


    private String email;

    private String password;

    
}
