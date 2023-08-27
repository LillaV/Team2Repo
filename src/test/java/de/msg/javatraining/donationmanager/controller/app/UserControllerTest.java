package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.dtos.role.CreateRoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.FirstLoginDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    private List<UserDto> generateList(){
        List<UserDto> list=new ArrayList<>();
        Set<CampaignDto> campaigns=new HashSet<>();
        Set<RoleDto> roles=new HashSet<>();
        UserDto user1=new UserDto(2L,"Andre","Ban",true,false,"andre@yahoo.com","",roles,campaigns);
        UserDto user2=new UserDto(3L,"Andrei","Banu",false,false,"andrei@yahoo.com","",roles,campaigns);
        list.add(user1);
        list.add(user2);
        return list;
    }

    @Test
    public void getUsers_returnsList_whenOffsetAndPageSizeNotNull(){
        List<UserDto> dtos=generateList();

        when(userService.allUsersWithPagination(0,5)).thenReturn(dtos);

        List<UserDto> res=userController.getUsers(0,5);

        verify(userService).allUsersWithPagination(0, 5);
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getUsers_returnsList_whenOffsetAndPageSizeNull(){
        List<UserDto> dtos=generateList();

        when(userService.getAllUsers()).thenReturn(dtos);

        List<UserDto> res=userController.getUsers(null,null);

        verify(userService).getAllUsers();
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(userService.getSize()).thenReturn(5L);
        Long res=userController.getSize();
        verify(userService).getSize();
        assertEquals(5L,res);
    }

    @Test
    public void getUserById_returnsUserDto_whenValid(){
        List<UserDto> dtos=generateList();

        when(userService.findById(2L)).thenReturn(dtos.get(0));

        UserDto res=userController.getUserById(2L);

        verify(userService).findById(2L);
        assertEquals(dtos.get(0),res);
    }

    @Test
    public void saveUser_isSuccessfull_whenValid(){
        Set<CreateRoleDto> roles=new HashSet<>();
        CreateUserDto user=new CreateUserDto("Andre","Ban","andre@yahoo.com","",roles);
        TextResponse textResponse=new TextResponse("User created registered successfully!");

        when(userService.saveUser(user)).thenReturn(textResponse);
        TextResponse response=userController.saveUser(user);

        verify(userService).saveUser(user);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void firstLoginChanges_isSuccessful_whenValid(){
        FirstLoginDto pwd=new FirstLoginDto("password");
        TextResponse textResponse=new TextResponse("Password changed successfully");

        when(userService.firstLogin(2L,pwd)).thenReturn(textResponse);

        TextResponse response=userController.firstLoginChanges(2L,pwd);

        verify(userService).firstLogin(2L,pwd);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void toggleActivation_isSuccessful_whenValid(){
        TextResponse textResponse=new TextResponse("User deactivated successfully");

        when(userService.toggleActivation(2L)).thenReturn(textResponse);

        TextResponse response=userController.toggleActivation(2L);

        verify(userService).toggleActivation(2L);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void updateUser_isSuccessful_whenValid(){
        Set<RoleDto> roles=new HashSet<>();
        TextResponse textResponse=new TextResponse("User  updated successfully!");
        UpdateUserDto userDto=new UpdateUserDto("Andre","Ban",true,false,"andre@yahoo.com","",roles);

        when(userService.updateUser(2L,userDto)).thenReturn(textResponse);

        TextResponse response=userController.updateUser(2L,userDto);

        verify(userService).updateUser(2L,userDto);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void deleteUserById_isSuccessful_whenValid(){
        TextResponse textResponse=new TextResponse("User deleted successfully !");

        when(userService.deleteUserById(2L)).thenReturn(textResponse);

        TextResponse response=userController.deleteUserById(2L);

        verify(userService).deleteUserById(2L);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void addCampaigns_isSuccessful_whenValid(){
        List<CampaignDto> campaigns=new ArrayList<>();
        TextResponse textResponse=new TextResponse("The  campaigns were added successfully!");

        when(userService.addCampaignsToREP(campaigns,2L)).thenReturn(textResponse);

        TextResponse response=userController.addCampaigns(2L,campaigns);

        verify(userService).addCampaignsToREP(campaigns,2L);
        assertEquals(textResponse.getText(),response.getText());
    }
}