package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.service.MailUserService;
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

    @Autowired
    private MailUserService mailService;


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

    @PostMapping("/users2")
    public ResponseEntity<String> saveUser2(@RequestBody CreateUserDto user) {
       // userService.saveUser2(user);
        return new ResponseEntity<>("User saved", HttpStatus.OK);
    }

    @PostMapping("/send")
    public void sendEmail(@RequestBody CreateUserDto user){
        user.setPassword(this.mailService.sendSimpleMessage(user));
        //aici mai trebuie salvata parola
    }

    @PutMapping("/users/{id}")
    public ResponseEntity UpdateUser(@RequestBody UserDto user, @PathVariable("id") Long id) {
        userService.updateUser(id, user);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }
}
