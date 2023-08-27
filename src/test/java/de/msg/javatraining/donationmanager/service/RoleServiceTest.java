package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.PermissionMaper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.PermissionRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PermissionRepository permissionRepository;
    @Mock
    PermissionMaper mapper;
    @Mock
    RoleMapper roleMapper;

    @InjectMocks
    RoleService roleService;

    @Test
    public void getAllRoles_returnsList_inAllCases(){
        List<Role> roles = getRoleEntities();
        List<RoleDto> roleDtos = getRoleDtos();

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.rolesToRoleDtos(roles)).thenReturn(roleDtos);

        List<RoleDto> roleDtoList = roleService.findAll();

        verify(roleRepository).findAll();
        assertThat(roleDtoList, Matchers.is(roleDtos));
    }

    @Test
    public void addPermission_missingRole(){
        when(roleRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class,()->roleService.addPermission(new HashSet<>(),5L));
    }

    @Test
    public void addPermission_addSuccessful(){

    }

    @Test
    public void getPermissions_returnsList_inAllCases(){

    }

    @Test
    public void removePermissions_missingRole(){

    }

    @Test
    public void removePermission_successfullyRemoved(){

    }

    @Test
    public void isAcquired_true(){

    }

    @Test
    public void isAcquired_false(){

    }

    private List<Role> getRoleEntities() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ADM);
        role.setPermissions(new HashSet<>());
        roles.add(role);

        Role role1 = new Role();
        role1.setId(2);
        role1.setName(ERole.MGN);
        role1.setPermissions(new HashSet<>());
        roles.add(role1);

        return roles;
    }

    private List<RoleDto> getRoleDtos() {
        List<RoleDto> roleDtos = new ArrayList<>();
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName(ERole.ADM);
        roleDto.setPermissions(new HashSet<>());
        roleDtos.add(roleDto);

        RoleDto roleDto2 = new RoleDto();
        roleDto.setId(2);
        roleDto.setName(ERole.MGN);
        roleDto.setPermissions(new HashSet<>());
        roleDtos.add(roleDto);

        return roleDtos;
    }
    
}