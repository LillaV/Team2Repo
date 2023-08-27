package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionMaper {
    Permission permisssionDTOToPermission(PermissionDTO permissionDTO);

    PermissionDTO permissionToPermissionDTO(Permission permission);
}

