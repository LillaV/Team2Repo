package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.DonationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationControllerTest {

    @Mock
    DonationService donationService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DonationController donationController;

    private Donation generate(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<Donation> donations=new HashSet<>();
        User user1=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        Campaign campaign=new Campaign(1L,"Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new Donation(1L,"EUR",1000, LocalDate.now(),true,LocalDate.now(),"",user1,user2,campaign,donator);
    }

    private SimpleDonationDto generateSimpleDto(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<Donation> donations=new HashSet<>();
        User user1=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        Campaign campaign=new Campaign(1L,"Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new SimpleDonationDto("EUR",1000, LocalDate.now(),true,LocalDate.now(),"",user1,user2,campaign,donator);
    }

    private UpdateDonationDto generateUpdateDto(){
        Set<Donation> donations=new HashSet<>();
        Campaign campaign=new Campaign(1L,"Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new UpdateDonationDto("EUR",1000,"",campaign,donator,false);
    }

    @Test
    public void getCurrencies_returnsList_whenValid(){
        List<String> currencies=new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        when(donationService.getCurrencies()).thenReturn(currencies);

        List<String> res=donationController.getCurrencies();

        verify(donationService).getCurrencies();
        assertEquals(currencies,res);
    }

    @Test
    public void findDonationById_findSuccesful_whenValid(){
        Donation donation=generate();
        when(donationService.findById(donation.getId())).thenReturn(donation);

        Donation res=donationController.findDonationById(donation.getId());

        verify(donationService).findById(donation.getId());
        assertEquals(donation,res);
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(donationService.getSize()).thenReturn(5L);

        Long res=donationController.getSize();

        verify(donationService).getSize();
        assertEquals(5L,res);
    }

    @Test
    public void saveDonation_saveSuccessful_whenValid(){
        SimpleDonationDto donationDto=generateSimpleDto();
        doNothing().when(donationService).saveDonation(donationDto);

        TextResponse response=donationController.saveDonation(donationDto);

        verify(donationService).saveDonation(donationDto);
        assertEquals("Donation added",response.getText());
    }

    @Test
    public void updateDonation_updateSuccessful_whenNotApproved(){
        Donation donation=generate();
        donation.setApproved(false);
        UpdateDonationDto updateDto=generateUpdateDto();

        when(donationService.findById(1L)).thenReturn(donation);
        doNothing().when(donationService).updateDonation(1L,updateDto);

        TextResponse response=donationController.updateDonation(updateDto,1L);

        verify(donationService).updateDonation(1L,updateDto);
        assertEquals("Donation updated",response.getText());
    }

    @Test
    public void updateDonation_updateUnsuccessful_whenApproved(){
        Donation donation=generate();
        UpdateDonationDto updateDto=generateUpdateDto();
        updateDto.setApproved(true);

        when(donationService.findById(1L)).thenReturn(donation);

        TextResponse response=donationController.updateDonation(updateDto,1L);

        verify(donationService).findById(1L);
        assertEquals("Donation is already approved, you cannot edit it anymore.",response.getText());
    }

    @Test
    public void deleteDonation_deleteSuccessful_whenNotApproved(){
        Donation donation=generate();
        donation.setApproved(false);

        when(donationService.findById(1L)).thenReturn(donation);
        doNothing().when(donationService).deleteDonation(1L);

        ResponseEntity response=donationController.deleteDonation(1L);

        verify(donationService).deleteDonation(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donation deleted successfully", response.getBody());
    }

    @Test
    public void deleteDonation_deleteUnsuccessful_whenApproved(){
        Donation donation=generate();

        when(donationService.findById(1L)).thenReturn(donation);

        ResponseEntity response=donationController.deleteDonation(1L);

        verify(donationService).findById(1L);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Donation is already approved, you cannot delete it anymore.", response.getBody());
    }

    @Test
    public void approveDonation_approveSuccessful_whenCreatedByNotApprovedBy(){
        Donation donation=generate();
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);

        when(donationService.findById(1L)).thenReturn(donation);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        doNothing().when(donationService).approveDonation(donation,user2);

        ResponseEntity response=donationController.approveDonation(1L,2L);

        verify(donationService).approveDonation(donation,user2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donation approved successfully", response.getBody());
    }

    @Test
    public void approveDonation_approveUnsuccessful_whenCreatedByIsApprovedBy(){
        Donation donation=generate();
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        donation.setCreatedBy(user2);

        when(donationService.findById(1L)).thenReturn(donation);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        ResponseEntity response=donationController.approveDonation(1L,2L);

        verify(donationService).findById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("4 Augen Prinzip is violated", response.getBody());
    }
}