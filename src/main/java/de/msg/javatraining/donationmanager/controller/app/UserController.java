package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userService;


    @GetMapping("/users")
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        UserDto user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody UserDto user) {
        userService.saveUser(user);
        return new ResponseEntity<>("User saved", HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity UpdateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable("id") Long id) {
        userService.updateUser(id, updateUserDto);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }
}
