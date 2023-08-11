package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.model.Role;

public class LoadRolesForRegisterMapper {
    public static RoleDto roleToRoleDto(Role role){
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setPermissions(role.getPermissions());
        return roleDto;
    }
}
