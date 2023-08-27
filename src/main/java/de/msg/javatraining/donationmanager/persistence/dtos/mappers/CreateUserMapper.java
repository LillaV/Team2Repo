package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface CreateUserMapper {

    Set<Permission> defaultPermissionsForADM = new HashSet<>(Arrays.asList(new Permission(EPermission.AUTHORITY_PERMISSION_MANAGEMENT), new Permission(EPermission.AUTHORITY_USER_MANAGEMENT)));
    Set<Permission> defaultPermissionsForMGN = new HashSet<>(Arrays.asList(new Permission(EPermission.AUTHORITY_CAMP_MANAGEMENT), new Permission(EPermission.AUTHORITY_BENEF_MANAGEMENT),
            new Permission(EPermission.AUTHORITY_DONATION_MANAGEMENT), new Permission(EPermission.AUTHORITY_DONATION_APPROVE), new Permission(EPermission.AUTHORITY_DONATION_REPORTING),
            new Permission(EPermission.AUTHORITY_CAMP_REPORTING), new Permission(EPermission.AUTHORITY_CAMP_IMPORT)));
    Set<Permission> defaultPermissionForCEN = new HashSet<>(Arrays.asList(new Permission(EPermission.AUTHORITY_BENEF_MANAGEMENT), new Permission(EPermission.AUTHORITY_DONATION_MANAGEMENT),
            new Permission(EPermission.AUTHORITY_DONATION_REPORTING), new Permission(EPermission.AUTHORITY_CAMP_REPORTING)));
    Set<Permission> defaultPermissionsForREP = new HashSet<>(List.of(new Permission(EPermission.AUTHORITY_CAMP_REPORTING_RESTRICTED)));

    static User createUserDtoToUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setActive(true);
        user.setFailedLoginAttempts(0);
        user.setNewUser(true);
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setEmail(createUserDto.getEmail());
        user.setMobileNumber(createUserDto.getMobileNumber());
        Set<Role> roles = new HashSet<>();
        for (CreateRoleDto role : createUserDto.getRoles()) {
            Role role1 = new Role(role.getId(),role.getName());
            switch ( role.getName().name() ) {
                case "ADM":
                    role1.setPermissions(defaultPermissionsForADM);
                    break;
                case "MGN":
                    role1.setPermissions(defaultPermissionsForMGN);
                    break;
                case "CEN":
                    role1.setPermissions(defaultPermissionForCEN);
                    break;
                case "REP":
                    role1.setPermissions(defaultPermissionsForREP);
                    break;
            }
            roles.add(role1);
        }
        user.setRoles(roles);
        user.setCampaigns(new HashSet<>());
        return user;
    }


}
