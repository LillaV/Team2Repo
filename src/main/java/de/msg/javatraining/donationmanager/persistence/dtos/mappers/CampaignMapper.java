package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CampaignMapper {

    Campaign campaignDtoToCampaign(CampaignDto campaignDto);

    CampaignDto campaignToCampaignDto(Campaign campaign);

    List<CampaignDto> campaignsToCampaignDtos(List<Campaign> campaign);
}
