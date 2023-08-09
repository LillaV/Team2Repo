package de.msg.javatraining.donationmanager.service;


import de.msg.javatraining.donationmanager.persistence.dtos.UserDtoCreate;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.dtos.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public boolean userValidation(User user) {
        return containsOnlyLetters(user.getFirstName()) && containsOnlyLetters(user.getLastName())
                && isValidEmail(user.getEmail()) && validPhoneNumber(user.getPhoneNumber());
    }

    public void saveUser(UserDtoCreate userDto) {
        Set<Role> roles = new HashSet<>();
        Set<Campaign> campaigns = new HashSet<>();
        for(long id : userDto.getRolesIDs()){
            Optional<Role> role = roleRepository.findById(id);
            if(role.isPresent()){
                roles.add(role.get());
            }
        }
        for(long id : userDto.getCampaignIDs()){
            Optional<Campaign> campaign = campaignRepository.findById(id);
            if(campaign.isPresent()){
                campaigns.add(campaign.get());
            }
        }
        User userToSave = CreateUserMapper.createUserDtoToUser(userDto,roles,campaigns);
        if (userValidation(userToSave)) {
            userToSave.setUsername(generateUsername(userToSave));
            userRepository.save(userToSave);
        } else {
            System.out.println("Cannot save");
        }

    }


    public static boolean containsOnlyLetters(String input) {
        // Define a regular expression pattern that matches only letters (A-Z, a-z)
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(input);
        System.out.println("For lastname and firstName  the matcher returns" + matcher.matches());
        return matcher.matches();
    }

    public static boolean validPhoneNumber(String input) {
        // Define a regular expression pattern that matches only (00407XXXXXXXX, 07XXXXXXXX, +407XXXXXXXX) format
        Pattern pattern = Pattern.compile("^(00407|07|\\+407)\\d{8}$");
        Matcher matcher = pattern.matcher(input);
        System.out.println("For mobile number" + matcher.matches());
        // Check if the entire input string matches the pattern
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        // Define a regular expression pattern for basic email validation
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        System.out.println("For email returns" + matcher.matches());
        return matcher.matches();
    }

    public String generateUsername(User user) {
        //Username generated from the first 5 characters from lastName and one character from firstName
        String username = user.getLastName().substring(0, Math.min(5, user.getLastName().length())) + user.getFirstName().charAt(0);
        String originalUsername = username;

        int number = 1;
        while (isUsernameTaken(username)) {
            username = originalUsername + number;
            number++;
        }

        return username;
    }

    public boolean isUsernameTaken(String username) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


}
