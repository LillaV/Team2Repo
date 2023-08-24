package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.user.FirstLoginDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable(name = "id") Long id){
        return  userService.findById(id);
    }

    @PostMapping()
    public TextResponse saveUser(@RequestBody CreateUserDto user) {
          return  userService.saveUser(user);
    }


    @PutMapping("/{id}/firstLogin")
    public TextResponse firstLoginChanges(@PathVariable("id") Long id, @RequestBody FirstLoginDto password){
            return userService.firstLogin(id,password);
    }

    @PutMapping("/{id}/activation")
    public TextResponse toggleActivation(@PathVariable("id") Long id) {
        return userService.toggleActivation(id);
    }

    @PutMapping("/{id}")
    public  TextResponse updateUser(@PathVariable("id")Long id,@RequestBody UpdateUserDto userDto){
        return  userService.updateUser(id,userDto);
    }

    @DeleteMapping("/{id}")
    public TextResponse deleteUserById(@PathVariable("id") Long id) {
       return userService.deleteUserById(id);
    }


    @PutMapping("/{id}/addCampaigns")
    public TextResponse  addCampaigns(@PathVariable("id") long userId, @RequestBody List<CampaignDto> campaigns){
        return this.userService.addCampaignsToREP(campaigns,userId);
    }
    @GetMapping("/listing")
    public List<UserDto> getPage(@RequestParam(name = "offset",required = false) Integer offset,@RequestParam(name = "pageSize",required = false) Integer pageSize) {
        if(offset !=null && pageSize !=null) {
            return userService.allUsersWithPagination(offset, pageSize);
        }else{
            return userService.getAllUsers();
        }
    }

}
