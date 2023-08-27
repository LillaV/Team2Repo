package de.msg.javatraining.donationmanager.service;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.validation.DonationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {
    @InjectMocks
    DonationService donationService;

    @Mock
    DonationRepository donationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    DonationMapper donationMapper;
    @Mock
    DonationValidator donationValidator;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    @Test
    public void findById_throwException_whenDonationIsMissing(){
        when(donationRepository.findById(6L)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class, ()->donationService.findById(6L));
    }

    @Test
    public void findById_returnDonation_whenIdIsValid(){
        List<Donation> donations = getDonationEntities();

        when(donationRepository.findById(2L)).thenReturn(Optional.of(donations.get(0)));

        Optional<Donation> optionalDonation = Optional.of(donationService.findById(2L));

        verify(donationRepository).findById(2L);
        assertEquals(2L,optionalDonation.get().getId());
    }

    @Test
    public void saveDonation_addFailed_whenUserNotValid(){
       
    }

    @Test
    public void saveDonation_addSuccessful(){

    }

    @Test
    public void updateDonation_updateFailed_whenDonationIdNotValid(){
        when(donationRepository.findById(6L)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class, ()->donationService.updateDonation(6L, new UpdateDonationDto()));
    }

    @Test
    public void updateDonation_updateSuccessful_whenDonationIsValid(){

    }

    @Test
    public void approveDonation_approved(){

    }

    @Test
    public void filterDonations_returnsList_inAllCases(){

    }

    @Test
    public void getCUrrencies_returnsCurrencies(){

    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(donationRepository.count()).thenReturn(5L);
        Long size=donationService.getSize();
        verify(donationRepository).count();
        assertEquals(5L,size);
    }

    private List<Donation> getDonationEntities(){
        List<Donation> donations = new ArrayList<>();

        Donation donation = new Donation();
        donation.setId(1L);
        donations.add(donation);

        Donation donation1 = new Donation();
        donation.setId(2L);
        donations.add(donation1);

        return donations;
    }
}
