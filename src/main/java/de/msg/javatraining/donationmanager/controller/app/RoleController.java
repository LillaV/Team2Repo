package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleDto> findAll(){
        return roleService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> acordPermissions(@PathVariable("id") Long roleId, @RequestBody Set<PermissionDTO> permissionDTOS){
        this.roleService.addPermission(permissionDTOS,roleId);
        return new ResponseEntity<>("Permisssion accorded", HttpStatus.OK);
    }
}
