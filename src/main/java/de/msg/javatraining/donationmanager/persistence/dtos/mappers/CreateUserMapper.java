package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface CreateUserMapper {
    User createUserDtoToUser(CreateUserDto createUserDto);


}
