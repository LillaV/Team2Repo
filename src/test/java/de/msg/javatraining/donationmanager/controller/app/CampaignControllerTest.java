package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.service.CampaignService;
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
class CampaignControllerTest {
    @Mock
    CampaignService campaignService;

    @InjectMocks
    CampaignController campaignController;

    private List<CampaignDto> generateDtos(){
        CampaignDto camp1=new CampaignDto(1L,"UNICEF","Save me");
        CampaignDto camp2=new CampaignDto(2L,"Rotes Kreuz","Save Ukraine");
        CampaignDto camp3=new CampaignDto(3L,"ONU","Save Maui");
        List<CampaignDto> campaignList=new ArrayList<>();
        campaignList.add(camp1);
        campaignList.add(camp2);
        campaignList.add(camp3);
        return campaignList;
    }

    @Test
    public void getCampaigns_returnsList_whenOffsetAndPageSizeNull(){
        List<CampaignDto> dtos=generateDtos();
        when(campaignService.getCampaigns()).thenReturn(dtos);

        List<CampaignDto> res=campaignController.getCampaigns(null,null);

        verify(campaignService).getCampaigns();
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getCampaigns_returnsList_whenOffsetAndPageSizeNotNull(){
        List<CampaignDto> dtos=generateDtos();
        when(campaignService.allCampaignsWithPagination(0,5)).thenReturn(dtos);

        List<CampaignDto> res=campaignController.getCampaigns(0,5);

        verify(campaignService).allCampaignsWithPagination(0,5);
        assertThat(dtos, Matchers.is(res));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(campaignService.getSize()).thenReturn(5L);

        Long res=campaignController.getSize();

        verify(campaignService).getSize();
        assertEquals(5L,res);
    }

    @Test
    public void saveCampaign_saveSuccessful_whenValid(){
        List<CampaignDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Campaign saved successfully!");
        when(campaignService.saveCampaign(dtos.get(0))).thenReturn(textResponse);

        ResponseEntity<String> response=campaignController.saveCampaign(dtos.get(0));

        verify(campaignService).saveCampaign(dtos.get(0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Campaign saved", response.getBody());
    }

    @Test
    public void updateCampaign_updateSuccessful_whenValid(){
        List<CampaignDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Campaign saved successfully!");
        when(campaignService.updateCampaign(dtos.get(0).getId(),dtos.get(0))).thenReturn(textResponse);

        ResponseEntity response=campaignController.updateCampaign(dtos.get(0).getId(),dtos.get(0));

        verify(campaignService).updateCampaign(dtos.get(0).getId(),dtos.get(0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Campaign saved", response.getBody());

    }

    @Test
    public void findCampaignById_findSuccessful_whenValid(){
        List<CampaignDto> dtos=generateDtos();
        when(campaignService.findById(dtos.get(0).getId())).thenReturn(dtos.get(0));

        CampaignDto res=campaignController.findCampaignById(dtos.get(0).getId());

        verify(campaignService).findById(dtos.get(0).getId());
        assertEquals(dtos.get(0),res);
    }

    @Test
    public void deleteCampaignById_deleteSuccessful_whenValid(){
        List<CampaignDto> dtos=generateDtos();
        TextResponse textResponse=new TextResponse("Campaign deleted successfully!");
        when(campaignService.deleteCampaignById(dtos.get(0).getId())).thenReturn(textResponse);

        ResponseEntity response=campaignController.deleteCampaignById(dtos.get(0).getId());

        verify(campaignService).deleteCampaignById(dtos.get(0).getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Campaign deleted", response.getBody());
    }
}