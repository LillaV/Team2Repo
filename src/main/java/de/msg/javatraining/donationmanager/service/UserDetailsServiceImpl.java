package de.msg.javatraining.donationmanager.service;


import de.msg.javatraining.donationmanager.persistence.dtos.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    UserMapper userMapper = new UserMapper();

    CreateUserMapper createUserMapper=new CreateUserMapper();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }


    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = userMapper.UserToUserDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto findById(Long id) {
        User userToFind = userRepository.findById(id).get();
        return userMapper.UserToUserDto(userToFind);
    }

    public void saveUser(UserDto userDto) {
        User userToSave = userMapper.userDtoToUser(userDto);
        userRepository.save(userToSave);
    }

    /*public void saveUser2(CreateUserDto userDto) {
        User userToSave = createUserMapper.createUserDtoToUser(userDto);
        userRepository.save(userToSave);
    }*/

    public void updateUser(Long id, UserDto userDto) {
        User newUser = userRepository.findById(id).get();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        userRepository.save(newUser);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
