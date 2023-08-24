package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.DeletedUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.NewUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.UpdatedUserEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    UserValidator userValidator;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<UserDto> allUsersWithPagination(int offset, int pageSize) {
        Page<User> users = factory.getUserRepository().findAll(PageRequest.of(offset, pageSize));
        return users.stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }


    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User updatedUser = factory.getUserRepository().findById(id).get();
        String beforeUpdate = updatedUser.toString();
        updatedUser.setFirstName(updateUserDto.getFirstName());
        updatedUser.setLastName(updateUserDto.getLastName());
        updatedUser.setActive(updateUserDto.isActive());
        updatedUser.setNewUser(updateUserDto.isNewUser());
        updatedUser.setEmail(updateUserDto.getEmail());
        updatedUser.setMobileNumber(updateUserDto.getMobileNumber());
        updatedUser.setRoles(updateUserDto.getRoles());
        if (userValidator.validate(updatedUser)) {User user = factory.getUserRepository().save(updatedUser);
            if(user != null){
                eventPublisher.publishEvent(new UpdatedUserEvent(user.toString(),beforeUpdate,user.getUsername()));
            }
        }
    }

    public TextResponse firstLogin(Long id, FirstLoginDto pd){
        Optional<User> updatedUser=factory.getUserRepository().findById(id);
        if(!updatedUser.isPresent()){
            throw new UsernameNotFoundException("The user you are trying to change the password for  is not existent");
        }
        User user = updatedUser.get();
        user.setPassword(passwordEncoder.encode(pd.getPassword()));
        user.setNewUser(false);
        factory.getUserRepository().save(user);
        return new TextResponse("PasswordChanged");
    }

    public User toggleActivation(Long id){
        User updatedUser = factory.getUserRepository().findById(id).get();
        updatedUser.setActive(!updatedUser.isActive());
        User user = factory.getUserRepository().save(updatedUser);
        eventPublisher.publishEvent(new DeletedUserEvent(user));
        return updatedUser;
    }

    public User findById(Long id) {
        return factory.getUserRepository().findById(id).get();
    }

    public void deleteUserById(Long id) {
        factory.getUserRepository().deleteById(id);
    }

    public void saveUser(CreateUserDto userDto) {
        if (userDto.getRolesIDs().size() != 0) {
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
            if (userValidator.validate(userToSave)) {
                userToSave.setUsername(serviceUtils.generateUsername(userToSave, factory.getUserRepository().findAll()));
                User user = factory.getUserRepository().save(userToSave);
                if (user != null) {
                    System.out.println("Am ajuns aici");
                    eventPublisher.publishEvent(new NewUserEvent(user));
                    serviceUtils.sendSimpleMessage(user, password);
                }

            } else {
                System.out.println("Cannot save");
            }

        } else {
            System.out.println("Cannot save, user must have at least 1 role");
        }
    }

    public List<UserDto> getAllUsers() {
        return  this.factory.getUserRepository().findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }
}

