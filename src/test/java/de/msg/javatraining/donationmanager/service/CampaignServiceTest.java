package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hamcrest.Matchers;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {
    @Mock
    CampaignRepository campaignRepository;
    @Mock
    DonationRepository donationRepository;
    @Mock
    CampaignMapper campaignMapper;

    @InjectMocks
    CampaignService campaignService;

    private List<Campaign> generate(){
        Set<Donation> list=new HashSet<>();
        Campaign camp1=new Campaign(1L,"UNICEF","Save me", list);
        Campaign camp2=new Campaign(2L,"Rotes Kreuz","Save Ukraine",list);
        Campaign camp3=new Campaign(3L,"ONU","Save Maui",list);
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
    public void getCampaigns_returnList_inAllCases(){
        List <Campaign> campList=generate();
        List<CampaignDto> dtoList=generateDtos();

        when(campaignRepository.findAll()).thenReturn(campList);
        when(campaignMapper.campaignsToCampaignDtos(campList)).thenReturn(dtoList);

        List<CampaignDto> res=campaignService.getCampaigns();

        verify(campaignRepository,times(1)).findAll();
        assertThat(dtoList, Matchers.is(res));
    }

    @Test
    public void saveCampaign_saveSuccessful_whenValid(){
        Set<Donation> list=new HashSet<>();
        CampaignDto camp=new CampaignDto(1L,"Abc","abc");
        Campaign campaignEntity = new Campaign(1L,"Abc","abc",list);

        when(campaignMapper.campaignDtoToCampaign(camp)).thenReturn(campaignEntity);
        when(campaignRepository.save(campaignEntity)).thenReturn(campaignEntity);

        campaignService.saveCampaign(camp);

        verify(campaignRepository).save(campaignEntity);
    }

    @Test
    public void saveCampaign_saveUnsuccessful_whenNotValid(){
        Set<Donation> list=new HashSet<>();
        Campaign camp2=new Campaign(2L,"Abc","abcd",list);
        CampaignDto dto2=new CampaignDto(2L,"Abc","abcd");

        when(campaignMapper.campaignDtoToCampaign(dto2)).thenReturn(camp2);
        when(campaignRepository.save(camp2)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class,()->campaignService.saveCampaign(dto2));
    }

    @Test
    public void updateCampaign_updateSuccessful_whenValid(){
        Set<Donation> list=new HashSet<>();
        Campaign camp2=new Campaign(2L,"Abc","abcd",list);
        CampaignDto dto2=new CampaignDto(2L,"Abc","abcd");

        when(campaignRepository.findById(2L)).thenReturn(Optional.of(camp2));
        camp2.setName("UNICEFF");
        when(campaignRepository.save(camp2)).thenReturn(camp2);

        campaignService.updateCampaign(dto2.getId(),dto2);

        verify(campaignRepository).save(camp2);
    }

    @Test
    public void updateCampaign_updateUnsuccessful_whenNotValid(){
        Set<Donation> list=new HashSet<>();
        Campaign camp2=new Campaign(2L,"Abc","abcd",list);
        CampaignDto dto2=new CampaignDto(2L,"Abc","abcd");

        when(campaignRepository.findById(2L)).thenReturn(Optional.of(camp2));
        camp2.setName("UNICEFF");
        when(campaignRepository.save(camp2)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class,()->campaignService.updateCampaign(dto2.getId(),dto2));
    }

    @Test
    public void findById_findSuccessful_whenValidId(){
        List <Campaign> campList=generate();
        List<CampaignDto> dtoList=generateDtos();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campList.get(0)));
        when(campaignMapper.campaignToCampaignDto(campList.get(0))).thenReturn(dtoList.get(0));

        Optional<CampaignDto> res=Optional.of(campaignService.findById(1L));

        verify(campaignRepository,times(1)).findById(1L);
        assertEquals(1L,res.get().getId());
    }

    @Test
    public void findById_findUnsuccessful_whenInvalidId(){

        when(campaignRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,()->campaignService.findById(4L));
    }

    @Test
    public void deleteCampaignById_deleteSuccessful_whenCampaignIsPresent(){
        List <Campaign> campList=generate();
        List<CampaignDto> dtoList=generateDtos();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campList.get(0)));
        when(donationRepository.existsByCampaignAndApprovedTrue(campList.get(0))).thenReturn(false);
        doNothing().when(campaignRepository).deleteById(campList.get(0).getId());

        campaignService.deleteCampaignById(1L);

        verify(campaignRepository).deleteById(1L);
    }

    @Test
    public void deleteCampaignById_deleteUnsuccessful_whenCampaignIsInApprovedDonation(){
        List <Campaign> campList=generate();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campList.get(0)));
        when(donationRepository.existsByCampaignAndApprovedTrue(campList.get(0))).thenReturn(true);

        assertThrows(RuntimeException.class,()->campaignService.deleteCampaignById(1L));
    }

    @Test
    public void getSize_returnsSize_inAllCases(){
        when(campaignRepository.count()).thenReturn(5L);
        Long size=campaignService.getSize();
        verify(campaignRepository).count();
        assertEquals(5L,size);
    }

}