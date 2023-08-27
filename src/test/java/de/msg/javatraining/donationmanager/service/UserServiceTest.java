package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.config.notifications.events.UpdatedUserEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.RoleMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.FirstLoginDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.utils.UserServiceHelper;
import de.msg.javatraining.donationmanager.service.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;
import org.hamcrest.Matchers;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper userMapper;
    @Mock
    RoleMapper roleMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserServiceHelper serviceUtils;

    @Mock
    UserValidator userValidator;

    @Mock
    CreateUserMapper createUserMapper;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    UserRepository userRepository;

    @Mock
    CampaignMapper campaignMapper;

    @Mock
    MailService mailService;

    @InjectMocks
    UserService userService;

    @Test
    public void saveUser_saveSuccessful_whenInputsAreValid() {
        CreateUserDto createUserDto = new CreateUserDto();
        User userEntity = new User();

        createUserDto.setFirstName("FirstName");
        createUserDto.setLastName("LastName");
        createUserDto.setEmail("vlad.pasca17@gmail.com");
        createUserDto.setMobileNumber("0712345678");
        Set<CreateRoleDto> roleDtos = new HashSet<>();
        roleDtos.add(new CreateRoleDto(1,ERole.ADM, new HashSet<PermissionDTO>()));
        createUserDto.setRoles(roleDtos);

        userEntity.setId(1L);
        userEntity.setFirstName("FirstName");
        userEntity.setLastName("LastName");
        userEntity.setEmail("vlad.pasca17@gmail.com");
        userEntity.setMobileNumber("0712345678");
        userEntity.setUsername("lastnf");
        userEntity.setPassword("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        userEntity.setRoles(roles);

        List<User> users = getUserEntities();

        when(createUserMapper.createUserDtoToUser(createUserDto)).thenReturn(userEntity);
        doNothing().when(userValidator).validate(userEntity);
        when(userRepository.findAll()).thenReturn(users);
        when(serviceUtils.generateUsername(userEntity, users)).thenReturn("lastnf");
        String password = "passwd";
        when(serviceUtils.generateUUID()).thenReturn(password);
        when(passwordEncoder.encode(password)).thenReturn("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mailService.sendSimpleMessage(userEntity,password)).thenReturn(password);

        TextResponse actualResponse = userService.saveUser(createUserDto);
        TextResponse expectedResponse = new TextResponse("User created registered successfully!");

//        verify(eventPublisher).publishEvent(any());
        verify(mailService).sendSimpleMessage(userEntity, password);
        verify(userRepository).save(userEntity);

        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void saveUser_saveFailed_whenInputsAreInvalid() {
        CreateUserDto createUserDto = new CreateUserDto();
        User invalidUserEntity = new User();

        createUserDto.setFirstName("FirstName");
        createUserDto.setLastName("");
        createUserDto.setEmail("vlad.pasca17@gmail.com");
        createUserDto.setMobileNumber("0712345678");
        Set<CreateRoleDto> roleDtos = new HashSet<>();
        roleDtos.add(new CreateRoleDto(1,ERole.ADM, new HashSet<PermissionDTO>()));
        createUserDto.setRoles(roleDtos);

        invalidUserEntity.setId(1L);
        invalidUserEntity.setFirstName("FirstName");
        invalidUserEntity.setLastName("");
        invalidUserEntity.setEmail("vlad.pasca17@gmail.com");
        invalidUserEntity.setMobileNumber("0712345678");
        invalidUserEntity.setUsername("lastnf");
        invalidUserEntity.setPassword("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        invalidUserEntity.setRoles(roles);

        when(createUserMapper.createUserDtoToUser(createUserDto)).thenReturn(invalidUserEntity);
        doNothing().when(userValidator).validate(invalidUserEntity);

        assertThrows(InvalidRequestException.class, () -> userService.saveUser(createUserDto));

    }

    @Test
    public void saveUser_saveFailed_whenNoRolesAreSelected() {
        CreateUserDto createUserDto = new CreateUserDto();
        User invalidUserEntity = new User();

        createUserDto.setFirstName("FirstName");
        createUserDto.setLastName("");
        createUserDto.setEmail("vlad.pasca17@gmail.com");
        createUserDto.setMobileNumber("0712345678");


        invalidUserEntity.setId(1L);
        invalidUserEntity.setFirstName("FirstName");
        invalidUserEntity.setLastName("");
        invalidUserEntity.setEmail("vlad.pasca17@gmail.com");
        invalidUserEntity.setMobileNumber("0712345678");
        invalidUserEntity.setUsername("lastnf");
        invalidUserEntity.setPassword("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");

//        when(createUserMapper.createUserDtoToUser(createUserDto)).thenReturn(invalidUserEntity);
//        when(userValidator.validate(invalidUserEntity)).thenReturn(false);
//        doThrow(InvalidRequestException.class).when(userService.saveUser(createUserDto));

        assertThrows(InvalidRequestException.class, () -> userService.saveUser(createUserDto));

    }

    @Test
    public void updateUser_updateFailed_whenUserNotPresent() {
        UpdateUserDto userNotFound = new UpdateUserDto();
        Long invalidID = 2L;

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(invalidID, userNotFound));
    }

    @Test
    public void updateUser_updateFailed_whenAllRolesAreRemoved() {
        UpdateUserDto userNoRoles = new UpdateUserDto();
        userNoRoles.setFirstName("User");
        userNoRoles.setLastName("Prim");

        User userToBeUpdated = new User();
        Long userID = 7L;

        when(userRepository.findById(userID)).thenReturn(Optional.of(userToBeUpdated));
        assertThrows(InvalidRequestException.class, () -> userService.updateUser(userID, userNoRoles));
    }

    @Test
    public void updateUser_updateSuccessful_whenInputsAreValid() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("user");
        updateUserDto.setLastName("toBeUpdated");
        updateUserDto.setMobileNumber("0700000000");
        Set<RoleDto> roleDtos = new HashSet<>();
        RoleDto roleDto = new RoleDto();
        roleDto.setName(ERole.ADM);
        roleDtos.add(roleDto);
        updateUserDto.setRoles(roleDtos);

        User user = new User();
        Long userID = 7L;
        user.setId(userID);
        user.setEmail("vlad.pasca17@gmail.com");
        user.setMobileNumber("0712345678");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        user.setRoles(roles);

        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        user.setFirstName("USERNEW");
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(userValidator).validate(user);


        TextResponse actualResponse = userService.updateUser(userID, updateUserDto);
        TextResponse expectedResponse = new TextResponse("User updated successfully!");

        verify(userRepository).save(user);
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void updateUser_updateFailed_whenInputsAreInvalid() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("user");
        updateUserDto.setLastName("toBeUpdated");
        updateUserDto.setMobileNumber("0700000000");
        Set<RoleDto> roleDtos = new HashSet<>();
        updateUserDto.setRoles(roleDtos);

        User user = new User();
        Long userID = 7L;
        user.setId(userID);
        user.setEmail("vlad.pasca17@gmail.com");
        user.setMobileNumber("0712345678");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        user.setRoles(roles);

        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        user.setFirstName("USERNEW");

        assertThrows(InvalidRequestException.class, () -> userService.updateUser(userID, updateUserDto));
    }

    @Test
    public void firstLogin_loginSuccessful_whenUserExists() {
        Long userID = 7L;
        FirstLoginDto firstLoginDto = new FirstLoginDto();
        String loginPassword = "pswd";
        firstLoginDto.setPassword(loginPassword);

        User updatedUser = new User();
        updatedUser.setFirstName("User");
        updatedUser.setLastName("Prim");
        updatedUser.setId(userID);
        updatedUser.setEmail("vlad.pasca17@gmail.com");
        updatedUser.setMobileNumber("0712345678");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        updatedUser.setRoles(roles);
        String encodedPassword = "EncPswd";

        when(userRepository.findById(userID)).thenReturn(Optional.of(updatedUser));
        when(passwordEncoder.encode(firstLoginDto.getPassword())).thenReturn(encodedPassword);
        updatedUser.setPassword(encodedPassword);
        updatedUser.setNewUser(false);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        TextResponse actualResponse = userService.firstLogin(userID, firstLoginDto);
        TextResponse expectedResponse = new TextResponse("Password changed successfully");

        verify(userRepository).save(updatedUser);
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void firstLogin_loginFailed_whenUserIsNotFound() {
        Long invalidID = 2L;
        FirstLoginDto firstLoginDto = new FirstLoginDto();

        assertThrows(UsernameNotFoundException.class, () -> userService.firstLogin(invalidID, firstLoginDto));
    }

    @Test
    public void toggleActivation_activationSuccessful_whenValidUserIsInactive() {
        Long userID = 7L;
        User user = new User();
        user.setActive(false);

        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        TextResponse actualResponse = userService.toggleActivation(userID);
        TextResponse expectedResponse = new TextResponse("User activated successfully");

        verify(userRepository).save(user);
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void toggleActivation_deactivationSuccessful_whenValidUserIsActive() {
        Long userID = 7L;
        User user = new User();
        user.setActive(true);

        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        TextResponse actualResponse = userService.toggleActivation(userID);
        TextResponse expectedResponse = new TextResponse("User deactivated successfully");

        verify(userRepository).save(user);
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void findById_searchSuccessful_whenUserIsFound() {
        List<User> users = getUserEntities();
        UserDto userDto = new UserDto();
        userDto.setId(7L);
        userDto.setFirstName("User");
        userDto.setLastName("Prim");

        when(userRepository.findById(7L)).thenReturn(Optional.of(users.get(0)));
        when(userMapper.userToUserDto(users.get(0))).thenReturn(userDto);

        Optional<UserDto> optionalUserDto = Optional.of(userService.findById(7L));

        verify(userRepository).findById(7L);
        assertEquals(7L,optionalUserDto.get().getId());
    }

    @Test
    public void findById_searchFailed_whenUserIsNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class,() ->userService.findById(1L));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(userRepository.count()).thenReturn(5L);
        Long size=userService.getSize();
        verify(userRepository).count();
        assertEquals(5L,size);
    }

    @Test
    public void getAllUsers_returnsList_inAllCases(){
        List<User> users = getUserEntities();
        List<UserDto> userDtos = getUserDtos();

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.usersToUserDtos(users)).thenReturn(userDtos);

        List<UserDto> userDtosFinal = userService.getAllUsers();

        verify(userRepository).findAll();
        assertThat(userDtos, Matchers.is(userDtosFinal));
    }


    @Test
    public void deleteUser_deleteSuccessful(){
        List<User> users = getUserEntities();

        TextResponse actualResponse = userService.deleteUserById(7L);
        TextResponse expectedResponse = new TextResponse("User deleted successfully !");

        verify(userRepository).deleteById(users.get(0).getId());
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    @Test
    public void addCampaignsToREP_addFailed_whenUserNotPresent(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        List<CampaignDto> campaignDtos = new ArrayList<>();

        assertThrows(InvalidRequestException.class,() ->userService.addCampaignsToREP(campaignDtos,1L));
    }
    @Test
    public void addCampaignsToREP_addFailed_whenUserHasNotREPRole(){
        List<User> users = getUserEntities();

        User user = users.get(0);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM, new HashSet<Permission>()));
        user.setRoles(roles);
        List<CampaignDto> campaignDtos = new ArrayList<>();

        when(userRepository.findById(7L)).thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class, () -> userService.addCampaignsToREP(campaignDtos,user.getId()));
    }

//    @Test
//    public void addCampaignsToREP_addSuccessful_whenAllRequirementsAreMet(){
//        List<User> users = getUserEntities();
//
//        User user = users.get(0);
//        Set<Role> roles = new HashSet<>();
//        roles.add(new Role(ERole.REP, new HashSet<Permission>()));
//        user.setRoles(roles);
//        List<CampaignDto> campaignDtos = new ArrayList<>();
//        campaignDtos.add(new CampaignDto(1L,"C1","Purpose"));
//        campaignDtos.add(new CampaignDto(2L,"C2","Purpose"));
//        campaignDtos.add(new CampaignDto(3L,"C3","Purpose"));
//
//        Set<Campaign> campaigns = new HashSet<>();
//        Campaign campaign = new Campaign();
//        campaign.setName("C1");
//        campaign.setPurpose("Purpose");
//        campaigns.add(campaign);
//        Campaign campaign2 = new Campaign();
//        campaign.setName("C2");
//        campaign.setPurpose("Purpose");
//        campaigns.add(campaign2);
//        Campaign campaign3 = new Campaign();
//        campaign.setName("C3");
//        campaign.setPurpose("Purpose");
//        campaigns.add(campaign3);
//
//        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
//        when(user.getCampaigns()).thenReturn(campaigns);
//        when(campaignMapper.campaignDtosToCampaigns(campaignDtos)).thenReturn(campaigns);
//    }



    private List<User> getUserEntities() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setFirstName("User");
        user.setLastName("Prim");
        user.setId(7L);
        users.add(user);

        User user1 = new User();
        user1.setFirstName("User");
        user1.setLastName("Unu");
        user1.setId(9L);
        users.add(user1);

        return users;
    }

    private List<UserDto> getUserDtos() {
        List<UserDto> userDtos = new ArrayList<>();
        UserDto user = new UserDto();
        user.setFirstName("User");
        user.setLastName("Prim");
        user.setId(7L);
        userDtos.add(user);

        UserDto user1 = new UserDto();
        user1.setFirstName("User");
        user1.setLastName("Unu");
        user1.setId(9L);
        userDtos.add(user1);

        return userDtos;
    }


}
