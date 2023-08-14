package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.FirstLoginDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.utils.UserServiceUtils;
import de.msg.javatraining.donationmanager.service.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceUtils serviceUtils;
    @Autowired
    IUserServiceFactory factory;

    public List<UserDto> allUsersWithPagination(int offset, int pageSize){
        Page<User> users =  factory.getUserRepository().findAll(PageRequest.of(offset, pageSize));
        return users.stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User updatedUser = factory.getUserRepository().findById(id).get();
        updatedUser.setFirstName(updateUserDto.getFirstName());
        updatedUser.setLastName(updateUserDto.getLastName());
        updatedUser.setActive(updateUserDto.isActive());
        updatedUser.setNewUser(updateUserDto.isNewUser());
        updatedUser.setEmail(updateUserDto.getEmail());
        updatedUser.setMobileNumber(updateUserDto.getMobileNumber());
        updatedUser.setCampaigns(updateUserDto.getCampaigns());
        if (updateUserDto.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        updatedUser.setRoles(updateUserDto.getRoles());
        factory.getUserRepository().save(updatedUser);
    }

    public void firstLogin(Long id, FirstLoginDto pd){
        User updatedUser=factory.getUserRepository().findById(id).get();
        updatedUser.setPassword(passwordEncoder.encode(pd.getPassword()));
        updatedUser.setNewUser(false);
        factory.getUserRepository().save(updatedUser);
    }

    public void toggleActivation(Long id){
        User updatedUser = factory.getUserRepository().findById(id).get();
        updatedUser.setActive(!updatedUser.isActive());
        factory.getUserRepository().save(updatedUser);
    }

    public UserDto findById(Long id) {
        User userToFind = factory.getUserRepository().findById(id).get();
        return userMapper.userToUserDto(userToFind);
    }

    public void deleteUserById(Long id) {
        factory.getUserRepository().deleteById(id);
    }

    public void saveUser(CreateUserDto userDto) {
        Set<Role> roles = new HashSet<>();
        Set<Campaign> campaigns = new HashSet<>();
        for (long id : userDto.getRolesIDs()) {
            Optional<Role> role = factory.getRoleRepository().findById(id);
            if (role.isPresent()) {
                roles.add(role.get());
            }
        }
        for (long id : userDto.getCampaignIDs()) {
            Optional<Campaign> campaign = factory.getCampaignRepository().findById(id);
            if (campaign.isPresent()) {
                campaigns.add(campaign.get());
            }
        }
        User userToSave = CreateUserMapper.createUserDtoToUser(userDto, roles, campaigns);
        String password = UserServiceUtils.generateUUID();
        userToSave.setPassword(password);
        if (UserValidator.userValidation(userToSave)) {
            userToSave.setUsername(serviceUtils.generateUsername(userToSave,factory.getUserRepository().findAll()));
            User user = factory.getUserRepository().save(userToSave);
            if(user != null) {
                serviceUtils.sendSimpleMessage(user,password);
            }
        } else {
            System.out.println("Cannot save");
        }

    }
}
