package de.msg.javatraining.donationmanager.persistence.dtos.user;

import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
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
    private String email;
    private String mobileNumber;
    private Set<CreateRoleDto> roles = new HashSet<>();
}
