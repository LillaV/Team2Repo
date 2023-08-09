package de.msg.javatraining.donationmanager.service;


import de.msg.javatraining.donationmanager.persistence.dtos.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    //private UserMapper userMapper = new UserMapper();

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
            UserDto userDto = userMapper.userToUserDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto findById(Long id) {
        User userToFind = userRepository.findById(id).get();
        return userMapper.userToUserDto(userToFind);
    }

    public void saveUser(UserDto userDto) {
        User userToSave = userMapper.userDtoToUser(userDto);
        userRepository.save(userToSave);

    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User updatedUser = userRepository.findById(id).get();
        updatedUser.setFirstName(updateUserDto.getFirstName());
        updatedUser.setLastName(updateUserDto.getLastName());
        updatedUser.setActive(updateUserDto.isActive());
        updatedUser.setNewUser(updateUserDto.isNewUser());
        updatedUser.setEmail(updateUserDto.getEmail());
        updatedUser.setPhoneNumber(updateUserDto.getPhoneNumber());
        updatedUser.setCampaigns(updateUserDto.getCampaigns());
        if (updateUserDto.getPassword() != null) {
            updatedUser.setPassword(encodePassword(updateUserDto.getPassword()));
        }
        updatedUser.setRoles(updateUserDto.getRoles());
        userRepository.save(updatedUser);
    }

    public String encodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return  passwordEncoder.encode(password);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
