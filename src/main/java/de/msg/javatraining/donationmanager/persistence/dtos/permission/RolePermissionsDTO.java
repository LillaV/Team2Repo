package de.msg.javatraining.donationmanager.persistence.dtos.permission;



import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RolePermissionsDTO {
    private Set<PermissionDTO> acquiredPermissions;
    private Set<PermissionDTO> missingPermissions;
    private ERole role;
    private Integer id;
}
