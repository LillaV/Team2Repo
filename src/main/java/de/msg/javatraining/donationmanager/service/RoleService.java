package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.RoleNotFoundException;
import de.msg.javatraining.donationmanager.config.exception.UserNotFoundException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.LoadRolesForRegisterMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.PermissionMaper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.RolePermissionsDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class RoleService {

    @Autowired
    IUserServiceFactory factory;
    @Autowired
    PermissionMaper mapper;
    @Autowired
    RoleMapper roleMapper;

    public List<CreateRoleDto> findAll() {
        List<ERole> roles = List.of(ERole.values());
        return roles.stream().map(ERole -> new CreateRoleDto(ERole,new HashSet<PermissionDTO>())).collect(Collectors.toList());
    }

    public String addPermission(Set<PermissionDTO> permissionDTOS,Long roleId){
        Optional<Role> foundRole = this.factory.getRoleRepository().findById(roleId);
        if(foundRole.isPresent()){
             Role role = foundRole.get();
            Set<Permission> permissions = role.getPermissions();
            permissions.addAll(permissionDTOS.stream().map(mapper::permisssionDTOToPermission).collect(Collectors.toSet()));
            role.setPermissions(permissions);
            this.factory.getRoleRepository().save(role);
            return "Added permissions";
        }
        throw new RoleNotFoundException("the role doesn't exist");
    }

    public List<RolePermissionsDTO> getPermissions(Long userId){
       Optional<User> foundUser = this.factory.getUserRepository().findById(userId);
       if(foundUser.isPresent()){
           User user = foundUser.get();
           List<RolePermissionsDTO> permissionsDTOS = new ArrayList<>();
           for(Role role: user.getRoles()){
               Set<PermissionDTO> acquiredPermissions = role.getPermissions().stream().map(mapper::permissionToPermissionDTO).collect(Collectors.toSet());
               Set<PermissionDTO> missingPermissions = this.factory.getPermissionRepository().findAll().stream().map(mapper::permissionToPermissionDTO).filter(permissionDTO -> !isAcquired(acquiredPermissions,permissionDTO.getId())).collect(Collectors.toSet());
               missingPermissions.removeAll(acquiredPermissions);
               permissionsDTOS.add(new RolePermissionsDTO(acquiredPermissions,missingPermissions,role.getName(),role.getId()));
           }
           return permissionsDTOS;
       }
       throw new UserNotFoundException("the user doesn't exist");
    }

    public String removePermissions(Set<PermissionDTO> permissionDTOS,Long roleId){
        Optional<Role> foundRole = this.factory.getRoleRepository().findById(roleId);
        if(foundRole.isPresent()){
            Role role = foundRole.get();
            Set<Permission> permissions = role.getPermissions();
            Set<Permission> resultedPermisssions = permissions.stream().filter(permission -> !isAcquired(permissionDTOS, permission.getId())).collect(Collectors.toSet());
            role.setPermissions(resultedPermisssions);
            this.factory.getRoleRepository().save(role);
            return "Removed permissions";
        }
        throw new RoleNotFoundException("the role is missing");
    }

    private boolean isAcquired(Set<PermissionDTO> acquired,Long permissionId){
        for(PermissionDTO permissionDTO: acquired){
            if(permissionDTO.getId() == permissionId){
                return true;
            }
        }
        return  false;
    }

}
