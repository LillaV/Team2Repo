package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;

import java.util.Set;

public class CreateUserMapper {
    public static User createUserDtoToUser(CreateUserDto createUserDto, Set<Role> roles, Set<Campaign> campaigns){
        User user = new User();
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setEmail(createUserDto.getEmail());
        user.setMobileNumber(createUserDto.getMobileNumber());
        user.setRoles(roles);
        user.setCampaigns(campaigns);
        return user;
    }


}
