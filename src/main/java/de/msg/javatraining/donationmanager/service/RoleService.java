package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.mappers.LoadRolesForRegisterMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleService {
//    @Autowired
//    LoadRolesForRegisterMapper roleMapper;

    @Autowired
    IUserServiceFactory factory;

    public List<RoleDto> findAll() {
        List<Role> roles = this.factory.getRoleRepository().findAll();
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {
            RoleDto roleDto = LoadRolesForRegisterMapper.roleToRoleDto(role);
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }
}
