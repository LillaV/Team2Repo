package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.UserNotFoundException;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2, ERole.ROLE_ADMIN, new HashSet<Permission>()));
        Set<Campaign> campaigns = new HashSet<>();

        userEntity.setId(1L);
        userEntity.setFirstName("FirstName");
        userEntity.setLastName("LastName");
        userEntity.setEmail("vlad.pasca17@gmail.com");
        userEntity.setMobileNumber("0712345678");
        Set<Role> roles2 = new HashSet<>();
        roles2.add(new Role(2, ERole.ROLE_ADMIN, new HashSet<Permission>()));
        userEntity.setRoles(roles2);
        Set<Campaign> campaigns2 = new HashSet<>();
        userEntity.setCampaigns(campaigns2);


//        when(createUserMapper.createUserDtoToUser(createUserDto,roles, campaigns)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        userRepository.save(userEntity);

        verify(userRepository).save(userEntity);
    }

    @Test //TODO
    public void saveUser_saveFailed_whenInputsAreInvalid() {
        CreateUserDto createUserDto = new CreateUserDto();
        User userEntity = new User();

        createUserDto.setFirstName("FirstName");
        createUserDto.setLastName("");
        createUserDto.setEmail("vlad.pasca17@gmail.com");
        createUserDto.setMobileNumber("0712345678");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2, ERole.ROLE_ADMIN, new HashSet<Permission>()));
        Set<Campaign> campaigns = new HashSet<>();

        userEntity.setId(1L);
        userEntity.setFirstName("FirstName");
        userEntity.setLastName("");
        userEntity.setEmail("vlad.pasca17@gmail.com");
        userEntity.setMobileNumber("0712345678");
        Set<Role> roles2 = new HashSet<>();
        roles2.add(new Role(2, ERole.ROLE_ADMIN, new HashSet<Permission>()));
        userEntity.setRoles(roles2);
        Set<Campaign> campaigns2 = new HashSet<>();
        userEntity.setCampaigns(campaigns2);


//        when(createUserMapper.createUserDtoToUser(createUserDto,roles, campaigns)).thenReturn(userEntity);
//        when(userRepository.save(userEntity)).thenReturn(userEntity);
       // doThrow()

        userRepository.save(userEntity);

        verify(userRepository).save(userEntity);
    }
}