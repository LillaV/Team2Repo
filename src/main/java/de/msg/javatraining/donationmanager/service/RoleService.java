package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.PermissionMaper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.RolePermissionsDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.PermissionRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    PermissionMaper mapper;
    @Autowired
    RoleMapper roleMapper;

    public List<RoleDto> findAll() {
        //return roleRepository.findAll().stream().map(roleMapper::roleToRoleDto).collect(Collectors.toList());
        List<Role> roles=roleRepository.findAll();
        return roleMapper.rolesToRoleDtos(roles);

    }

    public TextResponse addPermission(Set<PermissionDTO> permissionDTOS, Long roleId) {
        Optional<Role> foundRole = roleRepository.findById(roleId);
        if (foundRole.isPresent()) {
            Role role = foundRole.get();
            role.getPermissions().addAll(permissionDTOS.stream().map(mapper::permisssionDTOToPermission).collect(Collectors.toSet()));
            roleRepository.save(role);
            return new TextResponse("The permissions were added accordingly!");
        }
        throw new InvalidRequestException("The role you are trying to  add the permissions for is missing!");
    }

    public List<RolePermissionsDTO> getPermissions() {
        List<Role> roles = roleRepository.findAll();
        List<RolePermissionsDTO> permissionsDTOS = new ArrayList<>();
        for (Role role : roles) {
            Set<PermissionDTO> acquiredPermissions = role.getPermissions().stream().map(mapper::permissionToPermissionDTO).collect(Collectors.toSet());
            Set<PermissionDTO> missingPermissions = permissionRepository.findAll().stream().map(mapper::permissionToPermissionDTO).filter(permissionDTO -> !isAcquired(acquiredPermissions, permissionDTO.getId())).collect(Collectors.toSet());
            permissionsDTOS.add(new RolePermissionsDTO(acquiredPermissions, missingPermissions, role.getName(), role.getId()));
        }
        return permissionsDTOS;
    }

    public TextResponse removePermissions(Set<PermissionDTO> permissionDTOS, Long roleId) {
        Optional<Role> foundRole = roleRepository.findById(roleId);
        if (foundRole.isPresent()) {
            Role role = foundRole.get();
            Set<Permission> resultedPermisssions = role.getPermissions().stream().filter(permission -> !isAcquired(permissionDTOS, permission.getId())).collect(Collectors.toSet());
            role.setPermissions(resultedPermisssions);
            roleRepository.save(role);
            return new TextResponse("All permissions were removed !!");
        }
        throw new InvalidRequestException("The role you are  trying to  remove permissions from  is missing!");
    }

    private boolean isAcquired(Set<PermissionDTO> acquired, Long permissionId) {
        for (PermissionDTO permissionDTO : acquired) {
            if (permissionDTO.getId() == permissionId) {
                return true;
            }
        }
        return false;
    }

}
