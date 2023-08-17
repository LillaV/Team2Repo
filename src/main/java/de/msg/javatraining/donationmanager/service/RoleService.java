package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.mappers.LoadRolesForRegisterMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.PermisssionMaper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class RoleService {

    @Autowired
    IUserServiceFactory factory;
    @Autowired
    PermisssionMaper mapper;

    public List<RoleDto> findAll() {
        List<Role> roles = this.factory.getRoleRepository().findAll();
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {
            RoleDto roleDto = LoadRolesForRegisterMapper.roleToRoleDto(role);
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    public void addPermission(Set<PermissionDTO> permissionDTOS,Long roleId){
        Optional<Role> foundRole = this.factory.getRoleRepository().findById(roleId);
        if(foundRole.isPresent()){
             Role role = foundRole.get();
            Set<Permission> permissions = role.getPermissions();
            permissions.addAll(permissionDTOS.stream().map(mapper::permisssionDTOToPermission).collect(Collectors.toList()));
            role.setPermissions(permissions);
            this.factory.getRoleRepository().save(role);
        }
    }
}
