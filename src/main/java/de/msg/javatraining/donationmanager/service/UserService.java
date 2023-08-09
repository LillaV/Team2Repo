package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.MailUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender emailSender ;

    public String generateUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }
    public String sendSimpleMessage(MailUserDto userDto){
        SimpleMailMessage message= new SimpleMailMessage();
        String passwd=generateUUID();
        message.setFrom("iana.baltes@gmail.com");
        message.setTo(userDto.getEmail());
        message.setSubject("Activate account");
        message.setText("Hello " + userDto.getUsername() +
                "!\nThank you for registering! For your first time logging in, " +
                "you will have to use the following password:\n"
                +passwd);
        emailSender.send(message);
        return passwd;
    }

    public List<UserDto> allUsersWithPagination(int offset, int pageSize){
        Page<User> users =  userRepository.findAll(PageRequest.of(offset, pageSize));
        return users.stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User updatedUser = userRepository.findById(id).get();
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
        userRepository.save(updatedUser);
    }

    public UserDto findById(Long id) {
        User userToFind = userRepository.findById(id).get();
        return userMapper.userToUserDto(userToFind);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
