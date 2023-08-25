package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.factories.IDonationServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {
    @Mock
    CampaignRepository campaignRepository;
    @Mock
    IDonationServiceFactory factory;
    @Mock
    CampaignMapper campaignMapper;
    @InjectMocks
    CampaignService campaignService;

    private List<Campaign> generate(){
        Campaign camp1=new Campaign(1L,"UNICEF","Save me");
        Campaign camp2=new Campaign(2L,"Rotes Kreuz","Save Ukraine");
        Campaign camp3=new Campaign(3L,"ONU","Save Maui");
        List<Campaign> campaignList=new ArrayList<>();
        campaignList.add(camp1);
        campaignList.add(camp2);
        campaignList.add(camp3);
        return campaignList;
    }

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
    public void getCampaigns_returnList_whenRepoNotEmpty(){
        List <Campaign> campList=generate();
        List<CampaignDto> dtoList=generateDtos();

        //when(campaignMapper.campaignDtoToCampaign(dtoList.get(0))).thenReturn(campList.get(0));
       // when(campaignMapper.campaignDtoToCampaign(dtoList.get(1))).thenReturn(campList.get(1));
       // when(campaignMapper.campaignDtoToCampaign(dtoList.get(2))).thenReturn(campList.get(2));
        for(int i=0;i<3;i++){
            when(campaignMapper.campaignDtoToCampaign(dtoList.get(i))).thenReturn(campList.get(i));
        }
        when(campaignRepository.findAll()).thenReturn(campList);

        List<CampaignDto> res=campaignService.getCampaigns();

        verify(campaignRepository,times(1)).findAll();
        assertEquals(3,res.size());
        assertEquals("UNICEF",res.get(0).getName());
        assertEquals("Rotes Kreuz",res.get(1).getName());
    }

    @Test
    public void saveCampaign_saveSuccessful_whenValid(){
        CampaignDto camp=new CampaignDto(1L,"Abc","abc");
        Campaign campaignEntity = new Campaign(1L,"Abc","abc");

        when(campaignMapper.campaignDtoToCampaign(camp)).thenReturn(campaignEntity);
        when(campaignRepository.save(campaignEntity)).thenReturn(campaignEntity);

        campaignService.saveCampaign(camp);

        verify(campaignRepository).save(campaignEntity);
    }

    @Test
    public void saveCampaign_saveUnsuccessful_whenNotValid(){
        Campaign camp1=new Campaign(1L,"Abc","abc");
        CampaignDto dto1=new CampaignDto(1L,"Abc","abc");
        Campaign camp2=new Campaign(2L,"Abc","abcd");
        CampaignDto dto2=new CampaignDto(2L,"Abc","abcd");

        when(campaignMapper.campaignDtoToCampaign(dto1)).thenReturn(camp1);
        when(campaignRepository.save(camp1)).thenReturn(camp1);
        campaignService.saveCampaign(dto1);
        verify(campaignRepository).save(camp1);

        when(campaignMapper.campaignDtoToCampaign(dto2)).thenReturn(camp2);
        //when(campaignRepository.save(camp2)).thenThrow(IllegalStateException.class);
        doThrow(SQLException.class).when(campaignRepository).save(camp2);
        campaignService.saveCampaign(dto2);
        assertThrows(SQLException.class,()->campaignRepository.save(camp2));
    }

    @Test
    public void updateCampaign_updateSuccessful_whenValid(){
        List<Campaign> list=generate();
        when(campaignRepository.save(any(Campaign.class))).thenReturn(new Campaign());
        CampaignDto campaignToEdit=campaignMapper.campaignToCampaignDto(list.get(0));
        campaignToEdit.setName("UNICEFF");
        campaignService.updateCampaign(campaignToEdit.getId(),campaignToEdit);
        verify(campaignRepository,times(1)).save(any(Campaign.class));
        assertEquals("UNICEFF",campaignRepository.findById(1L).get().getName());
    }

    @Test
    public void updateCampaign_updateUnsuccessful_whenNotValid(){
        List<Campaign> list=generate();
        // mock campaignRepository.findById
        // mock campaignRepository.save to throw exception
//        when(campaignRepository.save(any(Campaign.class))).thenReturn(new Campaign());
        Mockito.doThrow(IllegalStateException.class).when(campaignRepository).save(any(Campaign.class));

        CampaignDto campaignToEdit=campaignMapper.campaignToCampaignDto(list.get(1));
        campaignToEdit.setName("UNICEF");
        campaignService.updateCampaign(campaignToEdit.getId(),campaignToEdit);
        verify(campaignRepository,times(1)).save(any(Campaign.class));
        assertEquals("Rotes Kreuz",campaignRepository.findById(2L).get().getName());
        //assertThrows
    }

    @Test
    public void findById_findSuccessful_whenValidId(){
        List<Campaign> list=generate();
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(list.get(0)));
        Optional<CampaignDto> res=Optional.of(campaignService.findById(1L));
        verify(campaignRepository,times(1)).findById(1L);
        assertEquals(1L,res.get().getId());
    }

}