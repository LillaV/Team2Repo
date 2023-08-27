package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.config.notifications.events.DonationApprovedEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.DonationFilterPair;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.security.UserDetailsImpl;
import de.msg.javatraining.donationmanager.service.validation.DonationValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonationService {
    @Autowired
    DonationRepository donationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DonationMapper donationMapper;
    @Autowired
    DonationValidator donationValidator;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Donation findById(Long id) {
        Optional<Donation> donation = donationRepository.findById(id);
        if(!donation.isPresent()){
            throw new InvalidRequestException("The donation you enquire about  is  missing!");
        }
        return donation.get();
    }

    public TextResponse saveDonation(SimpleDonationDto simpleDonationDto) {
        Donation donationToSave = donationMapper.simpleDonationDtoToDonation(simpleDonationDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> creatorUser = userRepository.findByUsername(userDetails.getUsername());
        if (!creatorUser.isPresent()) {
            throw new InvalidRequestException("You cannot add a donation right now !");
        }
        donationToSave.setCreatedBy(creatorUser.get());
        donationToSave.setCreateDate(LocalDate.now());
        donationToSave.setApproved(false);
        donationValidator.validate(donationToSave);
        donationRepository.save(donationToSave);
        return new TextResponse("Donation saved successfully!");
    }


    public TextResponse updateDonation(Long id, UpdateDonationDto updateDonationDto) {
        Optional<Donation> foundDonation = donationRepository.findById(id);
        if (!foundDonation.isPresent()) {
                throw new InvalidRequestException("There  is no donation with the required id!");
        }
        Donation donation = foundDonation.get();
        donation.setAmount(updateDonationDto.getAmount());
        donation.setCurrency(updateDonationDto.getCurrency());
        donation.setCampaign(updateDonationDto.getCampaign());
        donation.setBenefactor(updateDonationDto.getBenefactor());
        donation.setNotes(updateDonationDto.getNotes());
        donationValidator.validate(donation);
        donationRepository.save(donation);
        return new TextResponse("Donation saved successfully!");
    }

    public TextResponse deleteDonation(Long id) {
        donationRepository.deleteById(id);
        return new TextResponse("Donation deleted successfully");
    }

    public void approveDonation(Donation donation, User approvedBy) {
        donation.setApproved(true);
        donation.setApprovedBy(approvedBy);
        donation.setApprovedDate(LocalDate.now());
        donationRepository.save(donation);
        eventPublisher.publishEvent(new DonationApprovedEvent(donation));
    }

    public DonationFilterPair filterDonationsWithPaging(Specification<Donation> spec, Pageable pageable) {
        int size = donationRepository.findAll(spec).size();
        Page<Donation> donations = donationRepository.findAll(
                spec,
                pageable
        );
        return new DonationFilterPair(donations.stream().collect(Collectors.toList()), size);
    }

    public List<Donation> filterDonations(Specification<Donation> spec){
        return donationRepository.findAll(spec);
    }

    public List<String> getCurrencies() {
        return donationRepository.getDistinctCurrencies();
    }

    public long getSize() {
        return donationRepository.count();
    }

}
