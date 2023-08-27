package de.msg.javatraining.donationmanager.persistence.dtos.role;

import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Integer id;
    private ERole name;
    private Set<PermissionDTO> permissions = new HashSet<>();
}
