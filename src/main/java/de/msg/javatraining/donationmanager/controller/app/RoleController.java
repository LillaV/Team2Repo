package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.RolePermissionsDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }

    @PutMapping("add/{id}")
    public TextResponse accordPermissions(@PathVariable("id") Long roleId, @RequestBody Set<PermissionDTO> permissionDTOS) {
        return this.roleService.addPermission(permissionDTOS, roleId);
    }

    @GetMapping("/permissions")
    public List<RolePermissionsDTO> getPermissions() {
        return this.roleService.getPermissions();
    }

    @PutMapping("remove/{id}")
    public TextResponse removePermissions(@PathVariable("id") Long roleId, @RequestBody Set<PermissionDTO> permissionDTOS) {
        return this.roleService.removePermissions(permissionDTOS, roleId);
    }
}
