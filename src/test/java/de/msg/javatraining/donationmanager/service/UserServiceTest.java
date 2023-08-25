package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.permission.PermissionDTO;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Permission;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.utils.UserServiceUtils;
import de.msg.javatraining.donationmanager.service.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserServiceUtils serviceUtils;
    @Mock
    IUserServiceFactory factory;
    @Mock
    UserValidator userValidator;

    @Mock
    CreateUserMapper createUserMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    UserRepository userRepository;

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
        roleDtos.add(new CreateRoleDto(ERole.ADM,new HashSet<PermissionDTO>()));
        createUserDto.setRoles(roleDtos);

        userEntity.setId(1L);
        userEntity.setFirstName("FirstName");
        userEntity.setLastName("LastName");
        userEntity.setEmail("vlad.pasca17@gmail.com");
        userEntity.setMobileNumber("0712345678");
        userEntity.setUsername("lastnf");
        userEntity.setPassword("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM,new HashSet<Permission>()));
        userEntity.setRoles(roles);

        List<User> users = getUserEntities();

        when(createUserMapper.createUserDtoToUser(createUserDto)).thenReturn(userEntity);
        when(userValidator.validate(userEntity)).thenReturn(true);
        when(userRepository.findAll()).thenReturn( users);
        when(serviceUtils.generateUsername(userEntity, users)).thenReturn( "lastnf" );
        String password="passwd";
        when(serviceUtils.generateUUID()).thenReturn( password );
        when(passwordEncoder.encode(password)).thenReturn( "$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi" );
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        TextResponse actualResponse = userService.saveUser(createUserDto);
        TextResponse expectedResponse = new TextResponse("User created registered successfully!");

//        verify(eventPublisher).publishEvent(any());
        verify(serviceUtils).sendSimpleMessage(userEntity, password);
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
        roleDtos.add(new CreateRoleDto(ERole.ADM,new HashSet<PermissionDTO>()));
        createUserDto.setRoles(roleDtos);

        invalidUserEntity.setId(1L);
        invalidUserEntity.setFirstName("FirstName");
        invalidUserEntity.setLastName("");
        invalidUserEntity.setEmail("vlad.pasca17@gmail.com");
        invalidUserEntity.setMobileNumber("0712345678");
        invalidUserEntity.setUsername("lastnf");
        invalidUserEntity.setPassword("$2a$10$Z/OSRNFBmw5J.2P2g33/2OnH7qsxkiGfAaZpaTwg.UP0P466zmvVi");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ADM,new HashSet<Permission>()));
        invalidUserEntity.setRoles(roles);

        when(createUserMapper.createUserDtoToUser(createUserDto)).thenReturn(invalidUserEntity);
        when(userValidator.validate(invalidUserEntity)).thenReturn(false);

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
    public void updateUser_updateFailed_whenUserNotPresent(){
        List<User> users = getUserEntities();


    }

    @Test
    public void updateUser_updateSuccessful_whenInputsAreValid(){
        List<User> users = getUserEntities();

    }

    @Test
    public void updateUser_updateFailed_whenInputsAreInvalid(){
        List<User> users = getUserEntities();

    }

    @Test
    public void firstLogin_loginSuccessful_whenUserExists(){
        List<User> users = getUserEntities();
    }

    @Test
    public void firstLogin_loginFailed_whenUserIsNotFound(){
        List<User> users = getUserEntities();
    }

    @Test
    public void toggleActivation_activationSuccessful_whenValidUserIsInactive(){}

    @Test
    public void toggleActivation_deactivationSuccessful_whenValidUserIsActive(){}

    @Test
    public void findById_searchSuccessful_whenUserIsFound(){}

    @Test
    public void findById_searchFailed_whenUserIsNotFound(){}

//    @Test
//    public void

    private List<User> getUserEntities() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setFirstName("User");
        user.setLastName("Prim");
        user.setId(7L);
        users.add(user);

        User user1 = new User();
        user.setFirstName("User");
        user.setLastName("Unu");
        user1.setId(9L);
        users.add(user1);

        return users;
    }


}
