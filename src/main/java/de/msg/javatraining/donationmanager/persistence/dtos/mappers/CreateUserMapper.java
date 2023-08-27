package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import org.mapstruct.Mapper;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface CreateUserMapper {
    User createUserDtoToUser(CreateUserDto createUserDto);


}
