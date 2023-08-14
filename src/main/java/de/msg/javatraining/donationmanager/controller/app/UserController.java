package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('PERMISSION_MANAGEMENT')")
    @GetMapping("/{offset}/{pageSize}")
    //@PreAuthorize("hasAuthority('AUTHORITY_CAMP_REPORTING')")
    public List<UserDto> getPage(@PathVariable(name = "offset") int offset,@PathVariable(name = "pageSize") int pageSize) {
        return userService.allUsersWithPagination(offset, pageSize);
    }

    @PostMapping()
    public ResponseEntity<String> saveUser(@RequestBody CreateUserDto user) {
        try{
            userService.saveUser(user);
            return new ResponseEntity<>("User saved", HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{id}/activation")
    public ResponseEntity toggleActivation(@PathVariable("id") Long id) {
        userService.toggleActivation(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<String> updateUser(@RequestParam("id")Long id,@RequestBody UpdateUserDto userDto){
        userService.updateUser(id,userDto);
        return new ResponseEntity<>("User updated",HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }


}
