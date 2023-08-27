package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonatorMapper;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import de.msg.javatraining.donationmanager.service.validation.DonatorValidator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonatorServiceTest {
    @Mock
    DonatorRepository donatorRepository;

    @Mock
    DonationRepository donationRepository;

    @Mock
    DonatorMapper donatorMapper;

    @Mock
    DonatorValidator donatorValidator;

    @InjectMocks
    DonatorService donatorService;

    private List<Donator> generate(){
        Donator don1=new Donator(1L,"Alex","Nebunu","","");
        Donator don2=new Donator(2L,"Alexia","Nebunu","","But");
        Donator don3=new Donator(3L,"Alexandru","Normalu","Ioan","");
        List<Donator> donatorList=new ArrayList<>();
        donatorList.add(don1);
        donatorList.add(don2);
        donatorList.add(don3);
        return donatorList;
    }

    private List<SimpleDonatorDto> generateDtos(){
        SimpleDonatorDto don1=new SimpleDonatorDto(1L,"Alex","Nebunu","","");
        SimpleDonatorDto don2=new SimpleDonatorDto(2L,"Alexia","Nebunu","","But");
        SimpleDonatorDto don3=new SimpleDonatorDto(3L,"Alexandru","Normalu","Ioan","");
        List<SimpleDonatorDto> donatorList=new ArrayList<>();
        donatorList.add(don1);
        donatorList.add(don2);
        donatorList.add(don3);
        return donatorList;
    }

    @Test
    public void getDonators_returnList_inAllCases(){
        List<Donator> donList=generate();
        List<SimpleDonatorDto> dtoList=generateDtos();

        when(donatorRepository.findAll()).thenReturn(donList);
        when(donatorMapper.donatorsToSimpleDonatorDtos(donList)).thenReturn(dtoList);

        List<SimpleDonatorDto> donatorList=donatorService.getDonators();

        verify(donatorRepository).findAll();
        assertThat(dtoList, Matchers.is(donatorList));
    }
    @Test
    public void saveDonator_saveSuccessful_whenValid(){
        SimpleDonatorDto donatorDto=new SimpleDonatorDto(1L,"First","Last","Additional","");
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorMapper.SimpleDonatorDtoToDonator(donatorDto)).thenReturn(donator);
        when(donatorRepository.existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(donator.getFirstName(),donator.getLastName(),donator.getAdditionalName(),donator.getMaidenName())).thenReturn(false);
        when(donatorValidator.validate(donator)).thenReturn(true);
        when(donatorRepository.save(donator)).thenReturn(donator);

        donatorService.saveDonator(donatorDto);

        verify(donatorRepository).save(donator);
    }

    @Test
    public void saveDonator_saveUnsuccessful_whenAlreadyExists(){
        SimpleDonatorDto donatorDto=new SimpleDonatorDto(1L,"First","Last","Additional","");
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorMapper.SimpleDonatorDtoToDonator(donatorDto)).thenReturn(donator);
        when(donatorRepository.existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(donator.getFirstName(),donator.getLastName(),donator.getAdditionalName(),donator.getMaidenName())).thenReturn(true);

        //assertThrows(CevaEx.class,()->donatorService.saveDonator(donatorDto));
    }

    @Test
    public void saveDonator_saveUnsuccessful_whenNotValid(){
        SimpleDonatorDto donatorDto=new SimpleDonatorDto(1L,"First","Last","Additional","");
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorMapper.SimpleDonatorDtoToDonator(donatorDto)).thenReturn(donator);
        when(donatorRepository.existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(donator.getFirstName(),donator.getLastName(),donator.getAdditionalName(),donator.getMaidenName())).thenReturn(true);
        when(donatorValidator.validate(donator)).thenReturn(false);

        //assertThrows(CevaEx.class,()->donatorService.saveDonator(donatorDto));
    }

    @Test
    public void updateDonator_updateSuccessful_whenValid(){
        SimpleDonatorDto donatorDto=new SimpleDonatorDto(1L,"First","Last","Additional","");
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorRepository.findById(1L)).thenReturn(Optional.of(donator));
        donator.setFirstName("Firs");
        when(donatorRepository.save(donator)).thenReturn(donator);

        donatorService.updateDonator(donatorDto.getId(),donatorDto);

        verify(donatorRepository).save(donator);
    }

    @Test
    public void updateDonator_updateUnsuccessful_whenInvalid(){
        SimpleDonatorDto donatorDto=new SimpleDonatorDto(1L,"First","Last","Additional","");
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorRepository.findById(1L)).thenReturn(Optional.of(donator));
        donator.setFirstName("Firs1");
        //when(donatorRepository.save(donator)).thenThrow(CevaEx.class);

        //assertThrows(CevaEx.class,()->donatorService.updateDonator(donatorDto.getId(),donatorDto));
    } //aici 2 functii deoarece arunca 2 exceptii diferite

    @Test
    public void findById_findSuccessful_whenValid(){
        List<Donator> donList=generate();
        List<SimpleDonatorDto> dtoList=generateDtos();

        when(donatorRepository.findById(1L)).thenReturn(Optional.of(donList.get(0)));
        when(donatorMapper.donatorToSimpleDonatorDto(donList.get(0))).thenReturn(dtoList.get(0));

        Optional<SimpleDonatorDto> res=Optional.of(donatorService.findById(1L));

        verify(donatorRepository).findById(1L);
        assertEquals(1L,res.get().getId());
    }

    @Test
    public void findById_findUnsuccessful_whenInvalid(){

        when(donatorRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,()->donatorService.findById(4L));
    }

    @Test
    public void deleteDonatorById_setToUnknown_whenExistsDonations(){
        List<Donator> donList=generate();

        when(donationRepository.existsByBenefactorId(donList.get(0).getId())).thenReturn(true);
        when(donatorRepository.findById(donList.get(0).getId())).thenReturn(Optional.of(donList.get(0)));
        donList.get(0).setFirstName("Unknown");
        donList.get(0).setLastName("Unknown");
        donList.get(0).setAdditionalName("Unknown");
        donList.get(0).setMaidenName("Unknown");
        when(donatorRepository.save(donList.get(0))).thenReturn(donList.get(0));

        donatorService.deleteDonatorById(donList.get(0).getId());

        verify(donationRepository).existsByBenefactorId(donList.get(0).getId());
        verify(donatorRepository).save(donList.get(0));
    }

    @Test
    public void deleteDonatorById_deleteSuccessful_whenNoDonationsByDonator(){
        List<Donator> donList=generate();

        when(donationRepository.existsByBenefactorId(donList.get(0).getId())).thenReturn(false);
        doNothing().when(donatorRepository).deleteById(donList.get(0).getId());

        donatorService.deleteDonatorById(donList.get(0).getId());

        verify(donatorRepository).deleteById(donList.get(0).getId());
    }

    @Test
    public void setToUnknown_executeUpdate_inAllCases(){
        Donator donator=new Donator(1L,"First","Last","Additional","");

        when(donatorRepository.findById(1L)).thenReturn(Optional.of(donator));
        donator.setFirstName("Unknown");
        donator.setLastName("Unknown");
        donator.setAdditionalName("Unknown");
        donator.setMaidenName("Unknown");
        when(donatorRepository.save(donator)).thenReturn(donator);

        donatorService.setToUnknown(1L);

        verify(donatorRepository).save(donator);
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(donatorRepository.count()).thenReturn(4L);
        Long size=donatorService.getSize();
        verify(donatorRepository).count();
        assertEquals(4L,size);
    }
}