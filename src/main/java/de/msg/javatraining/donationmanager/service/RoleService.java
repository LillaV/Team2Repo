package de.msg.javatraining.donationmanager.service;

import com.sun.jdi.request.InvalidRequestStateException;
import de.msg.javatraining.donationmanager.config.exception.RoleNotFoundException;
import de.msg.javatraining.donationmanager.config.exception.UserNotFoundException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.PermissionMaper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.RolePermissionsDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.PermissionRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    PermissionMaper mapper;
    @Autowired
    RoleMapper roleMapper;

    public List<CreateRoleDto> findAll() {
        List<ERole> roles = List.of(ERole.values());
        return roles.stream().map(ERole -> new CreateRoleDto(ERole,new HashSet<PermissionDTO>())).collect(Collectors.toList());
    }

    public TextResponse addPermission(Set<PermissionDTO> permissionDTOS, Long roleId){
        Optional<Role> foundRole = roleRepository.findById(roleId);
        if(foundRole.isPresent()){
             Role role = foundRole.get();
            Set<Permission> permissions = role.getPermissions();
            permissions.addAll(permissionDTOS.stream().map(mapper::permisssionDTOToPermission).collect(Collectors.toSet()));
            role.setPermissions(permissions);
            roleRepository.save(role);
            return new TextResponse("The permissions were added accordingly!");
        }
        throw new RoleNotFoundException("The role you are trying to  add the permissions for is missing!");
    }

    public List<RolePermissionsDTO> getPermissions(Long userId){
       Optional<User> foundUser = userRepository.findById(userId);
       if(foundUser.isPresent()){
           User user = foundUser.get();
           List<RolePermissionsDTO> permissionsDTOS = new ArrayList<>();
           for(Role role: user.getRoles()){
               Set<PermissionDTO> acquiredPermissions = role.getPermissions().stream().map(mapper::permissionToPermissionDTO).collect(Collectors.toSet());
               Set<PermissionDTO> missingPermissions = permissionRepository.findAll().stream().map(mapper::permissionToPermissionDTO).filter(permissionDTO -> !isAcquired(acquiredPermissions,permissionDTO.getId())).collect(Collectors.toSet());
               missingPermissions.removeAll(acquiredPermissions);
               permissionsDTOS.add(new RolePermissionsDTO(acquiredPermissions,missingPermissions,role.getName(),role.getId()));
           }
           return permissionsDTOS;
       }
       throw new UserNotFoundException("the user doesn't exist");
    }

    public TextResponse removePermissions(Set<PermissionDTO> permissionDTOS,Long roleId){
        Optional<Role> foundRole = roleRepository.findById(roleId);
        if(foundRole.isPresent()){
            Role role = foundRole.get();
            Set<Permission> permissions = role.getPermissions();
            Set<Permission> resultedPermisssions = permissions.stream().filter(permission -> !isAcquired(permissionDTOS, permission.getId())).collect(Collectors.toSet());
            role.setPermissions(resultedPermisssions);
            roleRepository.save(role);
            return new TextResponse("All permissions were removed !!");
        }
        throw new RoleNotFoundException("The role you are  trying to  remove permissions from  is missing!");
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
