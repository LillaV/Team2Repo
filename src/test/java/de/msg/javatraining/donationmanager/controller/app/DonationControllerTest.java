package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.DonationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    DonationMapper donationMapper;

    @InjectMocks
    DonationController donationController;

    private Donation generate(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<Donation> donations=new HashSet<>();
        User user1=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        Campaign campaign=new Campaign("Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new Donation(1L,"EUR",1000F, LocalDate.now(),true,LocalDate.now(),"",user1,user2,campaign,donator);
    }

    private SimpleDonationDto generateSimpleDto(){
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        Set<Donation> donations=new HashSet<>();
        User user1=new User(1L,"Andrei","Banu",true,false,"andrban1","","andr@yahoo.com",campaigns,"password",roles,0);
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        Campaign campaign=new Campaign("Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new SimpleDonationDto("EUR", 1000F, LocalDate.now(),true,LocalDate.now(),"",user1,user2,campaign,donator);
    }

    private UpdateDonationDto generateUpdateDto(){
        Set<Donation> donations=new HashSet<>();
        Campaign campaign=new Campaign("Unicef","Help me",donations);
        Donator donator=new Donator(1L,"Vivi","Lang","","");
        return new UpdateDonationDto("EUR", 1000F,"",campaign,donator,false);
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
        SimpleDonationDto dto=generateSimpleDto();
        when(donationService.findById(donation.getId())).thenReturn(donation);
        when(donationMapper.donationToSimpleDonationDto(donation)).thenReturn(dto);

        SimpleDonationDto res=donationController.findDonationById(donation.getId());

        verify(donationService).findById(donation.getId());
        assertEquals(dto,res);
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
        TextResponse textResponse=new TextResponse("Donation saved successfully!");
        when(donationService.saveDonation(donationDto)).thenReturn(textResponse);

        TextResponse response=donationController.saveDonation(donationDto);

        verify(donationService).saveDonation(donationDto);
        assertEquals("Donation added",response.getText());
    }

    @Test
    public void updateDonation_updateSuccessful_whenNotApproved(){
        Donation donation=generate();
        donation.setApproved(false);
        UpdateDonationDto updateDto=generateUpdateDto();
        TextResponse textResponse=new TextResponse("Donation saved successfully!");


        when(donationService.findById(1L)).thenReturn(donation);
        when(donationService.updateDonation(1L,updateDto)).thenReturn(textResponse);

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
        TextResponse textResponse=new TextResponse("Donation deleted successfully!");

        when(donationService.findById(1L)).thenReturn(donation);
        when(donationService.deleteDonation(1L)).thenReturn(textResponse);

        TextResponse response=donationController.deleteDonation(1L);

        verify(donationService).deleteDonation(1L);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void deleteDonation_deleteUnsuccessful_whenApproved(){
        Donation donation=generate();
        TextResponse textResponse=new TextResponse("Donation is already approved, you cannot delete it anymore.");


        when(donationService.findById(1L)).thenReturn(donation);

        TextResponse response=donationController.deleteDonation(1L);

        verify(donationService).findById(1L);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void approveDonation_approveSuccessful_whenCreatedByNotApprovedBy(){
        Donation donation=generate();
        TextResponse textResponse=new TextResponse("Donation approved successfully");
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);

        when(donationService.findById(1L)).thenReturn(donation);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        doNothing().when(donationService).approveDonation(donation,user2);

        TextResponse response=donationController.approveDonation(1L,2L);

        verify(donationService).approveDonation(donation,user2);
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void approveDonation_approveUnsuccessful_whenCreatedByIsApprovedBy(){
        Donation donation=generate();
        TextResponse textResponse=new TextResponse("You cannot  approve your own donation!");
        Set<Campaign> campaigns=new HashSet<>();
        Set<Role> roles=new HashSet<>();
        User user2=new User(2L,"Andre","Ban",true,false,"andban1","","andre@yahoo.com",campaigns,"passwor",roles,0);
        donation.setCreatedBy(user2);

        when(donationService.findById(1L)).thenReturn(donation);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        TextResponse response=donationController.approveDonation(1L,2L);

        verify(donationService).findById(1L);
        assertEquals(textResponse.getText(),response.getText());
    }
}