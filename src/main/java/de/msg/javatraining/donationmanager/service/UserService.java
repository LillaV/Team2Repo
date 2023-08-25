package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.config.exception.UserNotFoundException;
import de.msg.javatraining.donationmanager.config.notifications.events.DeletedUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.NewUserEvent;
import de.msg.javatraining.donationmanager.config.notifications.events.UpdatedUserEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.FirstLoginDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
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
    @Autowired
    private CampaignMapper campaignMapper;

    public List<UserDto> allUsersWithPagination(int offset, int pageSize) {
        Page<User> users = factory.getUserRepository().findAll(PageRequest.of(offset, pageSize));
        return users.stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }


    public TextResponse updateUser(Long id, UpdateUserDto updateUserDto) {
        Optional<User> updatedUser = factory.getUserRepository().findById(id);
        if(!updatedUser.isPresent()){
            throw new UsernameNotFoundException("The user you are trying to  update doesn't exists!");
        }
        if (updateUserDto.getRoles().size() == 0) {
            throw new InvalidRequestException("You cannot remove all the roles from an user!");
        }
        User userToBeUpdated =  updatedUser.get();
        String beforeUpdate = updatedUser.toString();
        userToBeUpdated.setFirstName(updateUserDto.getFirstName());
        userToBeUpdated.setLastName(updateUserDto.getLastName());
        userToBeUpdated.setActive(updateUserDto.isActive());
        userToBeUpdated.setNewUser(updateUserDto.isNewUser());
        userToBeUpdated.setEmail(updateUserDto.getEmail());
        userToBeUpdated.setMobileNumber(updateUserDto.getMobileNumber());
        userToBeUpdated.setRoles(updateUserDto.getRoles().stream().map(roleMapper::roleDtoToRole).collect(Collectors.toSet()));
        if (userValidator.validate(userToBeUpdated)) {
            User user = factory.getUserRepository().save(userToBeUpdated);
            eventPublisher.publishEvent(new UpdatedUserEvent(user.toString(),beforeUpdate,user.getUsername()));
            return new TextResponse("User  updated successfully!");
        }
        throw new InvalidRequestException("Invalid data!");
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
        return new TextResponse("Password changed successfully");
    }

    public TextResponse toggleActivation(Long id){
        boolean activation = false;
        User updatedUser = factory.getUserRepository().findById(id).get();
        if(!updatedUser.isActive()){
            activation = true;
        }
        updatedUser.setActive(!updatedUser.isActive());
        User user = factory.getUserRepository().save(updatedUser);
        if(!activation) {
            eventPublisher.publishEvent(new DeletedUserEvent(user));
            return new TextResponse("User deactivated successfully");
        }else{
            return new TextResponse("User activated successfully");
        }
    }

    public UserDto findById(Long id) {
        Optional<User> user =  factory.getUserRepository().findById(id);
        if(user.isPresent()){
            return userMapper.userToUserDto(user.get());
        }
        throw new UserNotFoundException("There is no user  with the id = " + id);
    }

    public TextResponse deleteUserById(Long id) {
        factory.getUserRepository().deleteById(id);
        return new TextResponse("User deleted successfully !");
    }

    public TextResponse saveUser(CreateUserDto userDto) {
        if (userDto.getRoles().size() > 0) {
            User userToSave = CreateUserMapper.createUserDtoToUser(userDto);
            if (userValidator.validate(userToSave)) {
                userToSave.setUsername(serviceUtils.generateUsername(userToSave, factory.getUserRepository().findAll()));
                String password = serviceUtils.generateUUID();
                userToSave.setPassword(passwordEncoder.encode(password));
                User user = factory.getUserRepository().save(userToSave);
                eventPublisher.publishEvent(new NewUserEvent(user));
                serviceUtils.sendSimpleMessage(user, password);
                return new TextResponse("User created registered successfully!");
            }else{
                throw new InvalidRequestException("Invalid data!");
            }
        }
        throw new InvalidRequestException("You cannot save a user without any roles !");
    }

    public List<UserDto> getAllUsers() {
        return  this.factory.getUserRepository().findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public long getSize(){
        return factory.getUserRepository().count();
    }

    public TextResponse addCampaignsToREP(List<CampaignDto> campaigns,Long userID){
        Optional<User> foundUser = this.factory.getUserRepository().findById(userID);
        if(!foundUser.isPresent()){
            throw new  UserNotFoundException("The user you are trying to add the campaigns does not exists!");
        }
        User user = foundUser.get();
        boolean hasRoleREP = false;
        for(Role role : user.getRoles()){
            if(role.getName().name().equals(ERole.REP.name())){
                hasRoleREP = true;
            }
        }
        if(!hasRoleREP){
            throw new   InvalidRequestException("The user doesn't have  the  necessary role!");
        }
        user.getCampaigns().addAll(campaigns.stream().map(campaignMapper::campaignDtoToCampaign).collect(Collectors.toSet()));
        factory.getUserRepository().save(user);
        return new TextResponse("The  campaigns were added successfully!");
    }
}

