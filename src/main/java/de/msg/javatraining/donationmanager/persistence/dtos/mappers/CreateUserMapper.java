package de.msg.javatraining.donationmanager.persistence.dtos.mappers;


import de.msg.javatraining.donationmanager.persistence.dtos.RoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDtoCreate;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;

import java.util.Set;

public class CreateUserMapper {
    public static User createUserDtoToUser(UserDtoCreate userDtoCreate, Set<Role> roles,Set<Campaign> campaigns){
        User user = new User();
        user.setFirstName(userDtoCreate.getFirstName());
        user.setLastName(userDtoCreate.getLastName());
        user.setEmail(userDtoCreate.getEmail());
        user.setPhoneNumber(userDtoCreate.getPhoneNumber());
        user.setRoles(roles);
        user.setCampaigns(campaigns);
        return user;
    }

    public static UserDto userToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }


}
