package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
}