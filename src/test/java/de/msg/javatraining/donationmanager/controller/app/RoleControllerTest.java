package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.RolePermissionsDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.service.RoleService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    @Mock
    RoleService roleService;

    @InjectMocks
    RoleController roleController;

    private List<RoleDto> generateDto(){
        Set<PermissionDTO> permissions=new HashSet<>();
        List<RoleDto> roles=new ArrayList<>();
        PermissionDTO perm1=new PermissionDTO(1L,"Auth");
        PermissionDTO perm2=new PermissionDTO(1L,"Rep");
        permissions.add(perm1);
        permissions.add(perm2);
        RoleDto dto=new RoleDto(1,ERole.ADM,permissions);
        roles.add(dto);
        return roles;
    }

    @Test
    public void findAll_returnsList_inAllCases(){
        List<RoleDto> dtos=generateDto();

        when(roleService.findAll()).thenReturn(dtos);

        List<RoleDto> res=roleController.findAll();

        verify(roleService).findAll();
        assertEquals(dtos,res);
    }

    @Test
    public void accordPermissions_isSuccessful_inAllCases(){
        Set<PermissionDTO> permissions=new HashSet<>();
        PermissionDTO perm1=new PermissionDTO(1L,"Auth");
        PermissionDTO perm2=new PermissionDTO(1L,"Rep");
        permissions.add(perm1);
        permissions.add(perm2);
        TextResponse textResponse=new TextResponse("The permissions were added accordingly!");

        when(roleService.addPermission(permissions,1L)).thenReturn(textResponse);

        TextResponse response=roleController.accordPermissions(1L,permissions);

        verify(roleService).addPermission(permissions,1L);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void getPermissions_returnsList_inAllCases(){
        List<RolePermissionsDTO> list=new ArrayList<>();
        Set<PermissionDTO> permissions1=new HashSet<>();
        Set<PermissionDTO> permissions2=new HashSet<>();
        PermissionDTO perm1=new PermissionDTO(1L,"Auth");
        PermissionDTO perm2=new PermissionDTO(1L,"Rep");
        permissions1.add(perm1);
        permissions2.add(perm2);
        RolePermissionsDTO dto1=new RolePermissionsDTO(permissions1,permissions2,ERole.ADM,1);
        list.add(dto1);

        when(roleService.getPermissions()).thenReturn(list);

        List<RolePermissionsDTO> res=roleController.getPermissions();

        verify(roleService).getPermissions();
        assertThat(list, Matchers.is(res));
    }

    @Test
    public void removePermissions_isSuccessful_inAllCases(){
        Set<PermissionDTO> permissions=new HashSet<>();
        PermissionDTO perm1=new PermissionDTO(1L,"Auth");
        PermissionDTO perm2=new PermissionDTO(1L,"Rep");
        permissions.add(perm1);
        permissions.add(perm2);
        TextResponse textResponse=new TextResponse("All permissions were removed !!");

        when(roleService.removePermissions(permissions,1L)).thenReturn(textResponse);

        TextResponse response=roleController.removePermissions(1L,permissions);

        verify(roleService).removePermissions(permissions,1L);
        assertEquals(textResponse.getText(),response.getText());
    }
}