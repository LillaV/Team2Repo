package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto userToUserDto(User user);
    UpdateUserDto userToUpdateUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);
}