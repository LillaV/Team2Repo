package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UpdateUserMapper {
    UpdateUserDto userToUpdateUserDto(User user);
    User userDtoToUser(UpdateUserDto updateUserDto);
}
