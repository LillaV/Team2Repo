package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.service.DonatorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonatorControllerTest {
    @Mock
    DonatorService donatorService;

    @InjectMocks
    DonatorController donatorController;

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
    public void getDonators_returnsList__whenOffsetAndPageSizeNull(){
        List<SimpleDonatorDto> dtos=generateDtos();
        when(donatorService.getDonators()).thenReturn(dtos);

        List<SimpleDonatorDto> res=donatorController.getDonators(null,null);

        verify(donatorService).getDonators();
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getDonators_returnsList__whenOffsetAndPageSizeNotNull(){
        List<SimpleDonatorDto> dtos=generateDtos();
        when(donatorService.allDonatorsWithPagination(0,5)).thenReturn(dtos);

        List<SimpleDonatorDto> res=donatorController.getDonators(0,5);

        verify(donatorService).allDonatorsWithPagination(0,5);
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(donatorService.getSize()).thenReturn(5L);

        Long res=donatorController.getSize();

        verify(donatorService).getSize();
        assertEquals(5L,res);
    }

    @Test
    public void saveDonator_saveSuccessful_whenValid(){
        List<SimpleDonatorDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Donator saved successfully");
        when(donatorService.saveDonator(dtos.get(0))).thenReturn(textResponse);

        TextResponse response=donatorController.saveDonator(dtos.get(0));

        verify(donatorService).saveDonator(dtos.get(0));
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void updateDonator_updateSuccessful_whenValid(){
        List<Donator> donators=generate();
        List<SimpleDonatorDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Donator updated successfully");
        when(donatorService.updateDonator(dtos.get(0).getId(),dtos.get(0))).thenReturn(textResponse);

        TextResponse response=donatorController.updateDonator(dtos.get(0),dtos.get(0).getId());

        verify(donatorService).updateDonator(dtos.get(0).getId(),dtos.get(0));
        assertEquals(textResponse.getText(),response.getText());
    }

    @Test
    public void getDonatorById_findSuccessful_whenValid(){
        List<SimpleDonatorDto> dtos=generateDtos();
        when(donatorService.findById(dtos.get(0).getId())).thenReturn(dtos.get(0));

        SimpleDonatorDto res=donatorController.getDonatorById(dtos.get(0).getId());

        verify(donatorService).findById(dtos.get(0).getId());
        assertEquals(dtos.get(0),res);
    }

    @Test
    public void deleteDonatorById_deleteSuccessful_whenValid(){
        List<SimpleDonatorDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Donation deleted successfully!");
        when(donatorService.deleteDonatorById(dtos.get(0).getId())).thenReturn(textResponse);

        TextResponse response=donatorController.deleteDonatorById(dtos.get(0).getId());

        verify(donatorService).deleteDonatorById(dtos.get(0).getId());
        assertEquals(textResponse.getText(),response.getText());
    }
}