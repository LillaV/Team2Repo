package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampaignService{
    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignMapper campaignMapper;

    public List<CampaignDto> getCampaigns(){
        List<Campaign> campaigns=campaignRepository.findAll();
        return campaigns.stream().map(campaign -> campaignMapper.campaignToCampaignDto(campaign)).collect(Collectors.toList());
    }

    public void saveCampaign(CampaignDto campaignDto){
        Campaign campaignToSave=campaignMapper.campaignDtoToCampaign(campaignDto);
        campaignRepository.save(campaignToSave);
    }

    public void updateCampaign(Long id, CampaignDto campaignDto){
        Campaign campaignToSave=campaignRepository.findById(id).get();
        campaignToSave.setName(campaignDto.getName());
        campaignToSave.setPurpose(campaignDto.getPurpose());
        campaignRepository.save(campaignToSave);
    }

    public CampaignDto findById(Long id){
        Campaign campaignToFind=campaignRepository.findById(id).get();
        return campaignMapper.campaignToCampaignDto(campaignToFind);
    }

}
