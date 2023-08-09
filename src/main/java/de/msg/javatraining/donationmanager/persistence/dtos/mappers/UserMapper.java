package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;

public class UserMapper {
    public static UserDto UserToUserDto(User user) {
        return  new UserDto(user.getId(), user.getFirstName(),user.getLastName(),user.isActive(),user.getEmail(),user.getMobileNumber(),user.getRoles(),user.getCampaigns());
    }
}