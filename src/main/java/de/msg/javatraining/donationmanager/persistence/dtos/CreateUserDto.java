package de.msg.javatraining.donationmanager.persistence.dtos;

import de.msg.javatraining.donationmanager.persistence.model.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


public class CreateUserDto {

    private String username;


    private String email;

    private String password;

    public CreateUserDto() {
    }

    public CreateUserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
