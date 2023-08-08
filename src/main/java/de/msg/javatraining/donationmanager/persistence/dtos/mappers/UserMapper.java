package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;

public class UserMapper {

    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public UserDto UserToUserDto(User user) {
        UserDto userDto1 = new UserDto();
        userDto1.setId(user.getId());
        userDto1.setUsername(user.getUsername());
        userDto1.setEmail(user.getEmail());
        userDto1.setPassword(user.getPassword());
        return userDto1;
    }
}