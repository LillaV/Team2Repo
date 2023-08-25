package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.notifications.events.DonationApprovedEvent;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.DonationFilterPair;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.security.UserDetailsImpl;
import de.msg.javatraining.donationmanager.service.validation.DonationValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    public List<Donation> allDonationsWithPagination(int offset, int pageSize) {
        Page<Donation> donations = donationRepository.findAll(PageRequest.of(offset, pageSize));
        return donations.stream().collect(Collectors.toList());
    }

    public Donation findById(Long id) {
        return donationRepository.findById(id).get();
    }

    public void saveDonation(SimpleDonationDto simpleDonationDto) {
        Donation donationToSave = donationMapper.simpleDonationDtoToDonation(simpleDonationDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User creatorUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        donationToSave.setCreatedBy(creatorUser);
        donationToSave.setCreateDate(LocalDate.now());
        donationToSave.setApproved(false);

        donationValidator.validate(donationToSave);

        donationRepository.save(donationToSave);
    }


    public void updateDonation(Long id, UpdateDonationDto updateDonationDto) {
        Donation updatedDonation = donationRepository.findById(id).get();
        updatedDonation.setAmount(updateDonationDto.getAmount());
        updatedDonation.setCurrency(updateDonationDto.getCurrency());
        updatedDonation.setCampaign(updateDonationDto.getCampaign());
        updatedDonation.setBenefactor(updateDonationDto.getBenefactor());
        updatedDonation.setNotes(updateDonationDto.getNotes());

        donationValidator.validate(updatedDonation);

        donationRepository.save(updatedDonation);
    }

    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
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

    public List<String> getCurrencies() {
        return donationRepository.getDistinctCurrencies();
    }

    public long getSize() {
        return donationRepository.count();
    }

}
