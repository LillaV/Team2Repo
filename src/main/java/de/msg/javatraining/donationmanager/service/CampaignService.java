package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignFilterPair;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CampaignMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampaignService {
    @Autowired
    DonationRepository donationRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignMapper campaignMapper;
    @Autowired
    private UserRepository userRepository;

    public List<CampaignDto> allCampaignsWithPagination(int offset, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent()){
            throw new  InvalidRequestException("Something went wrong while retriving  the data!");
        }
        User user1 = user.get();
        user1.getRoles().stream().filter(role -> role.getName() == ERole.REP);
        if(user1.getRoles().size()!=0){
            return campaignRepository.getCustomCampaignList(user1.getId(),PageRequest.of(offset, pageSize)).stream().map(campaignMapper::campaignToCampaignDto).collect(Collectors.toList());
        }
        Page<Campaign> campaigns = campaignRepository.findAll(PageRequest.of(offset, pageSize));
        return campaigns.stream().map(campaign -> campaignMapper.campaignToCampaignDto(campaign)).collect(Collectors.toList());
    }

    public List<CampaignDto> getCampaigns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent()){
            throw new  InvalidRequestException("Something went wrong while retriving  the data!");
        }
        User user1 = user.get();
        user1.getRoles().stream().filter(role -> role.getName() == ERole.REP);
        if(user1.getRoles().size()!=0){
            return campaignRepository.getCustomCampaignList(user1.getId()).stream().map(campaignMapper::campaignToCampaignDto).collect(Collectors.toList());
        }
        List<Campaign> campaigns = campaignRepository.findAll();
        return campaigns.stream().map(campaign -> campaignMapper.campaignToCampaignDto(campaign)).collect(Collectors.toList());
    }

    public TextResponse saveCampaign(CampaignDto campaignDto) {
        Campaign campaignToSave = campaignMapper.campaignDtoToCampaign(campaignDto);
        campaignRepository.save(campaignToSave);
        return new TextResponse("Campaign saved successfully!");
    }

    public TextResponse updateCampaign(Long id, CampaignDto campaignDto) {
        Campaign campaignToSave = campaignRepository.findById(id).get();
        campaignToSave.setName(campaignDto.getName());
        campaignToSave.setPurpose(campaignDto.getPurpose());
        campaignRepository.save(campaignToSave);
        return new TextResponse("Campaign saved successfully!");
    }

    public CampaignDto findById(Long id) {
        Campaign campaignToFind = campaignRepository.findById(id).get();
        return campaignMapper.campaignToCampaignDto(campaignToFind);
    }

    public TextResponse deleteCampaignById(Long id) {
        Campaign campaignToFind = campaignRepository.findById(id).get();
        if (donationRepository.existsByCampaignAndApprovedTrue(campaignToFind)) {
            throw new InvalidRequestException("This campaign cannot be deleted!");
        } else {
            campaignRepository.deleteById(id);
            return new TextResponse("Campaign deleted successfully!");
        }
    }

    public CampaignFilterPair filterCampaignsWithPaging(Specification<Campaign> spec, Pageable pageable) {
        int size = campaignRepository.findAll(spec).size();
        Page<Campaign> campaigns = campaignRepository.findAll(spec, pageable);
        return new CampaignFilterPair(campaigns.stream().collect(Collectors.toList()), size);
    }

    public List<Campaign> filterCampaigns(Specification<Campaign> spec){
        return campaignRepository.findAll(spec);
    }

    public long getSize() {
        return campaignRepository.count();
    }

    public List<Donator> getDistinctBenefactorsByCampaignId(Long campaignId) {
        return donationRepository.findDistinctBenefactorsByCampaignId(campaignId);
    }
}
