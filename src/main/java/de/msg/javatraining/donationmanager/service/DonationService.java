package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.CreateUserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.factories.IDonationServiceFactory;
import de.msg.javatraining.donationmanager.persistence.factories.IUserServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.utils.DonationSpecifications;
import de.msg.javatraining.donationmanager.service.utils.UserServiceUtils;
import de.msg.javatraining.donationmanager.service.validation.DonationValidator;
import de.msg.javatraining.donationmanager.service.validation.UserValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonationService {
    @Autowired
    IDonationServiceFactory factory;

    @Autowired
    IUserServiceFactory userFactory;

    @Autowired
    DonationMapper donationMapper;

    public List<SimpleDonationDto> allDonationsWithPagination(int offset, int pageSize){
        Page<Donation> donations =  factory.getDonationRepository().findAll(PageRequest.of(offset, pageSize));
        return donations.stream().map(donation -> donationMapper.donationToSimpleDonationDto(donation)).collect(Collectors.toList());
    }

    public Donation findById(Long id) {
        return factory.getDonationRepository().findById(id).get();
    }

    public void saveDonation(SimpleDonationDto simpleDonationDto) {
        Donation donationToSave = donationMapper.simpleDonationDtoToDonation(simpleDonationDto);
        donationToSave.setCreateDate(LocalDate.now());
        donationToSave.setApproved(false);

        DonationValidator.donationValidation(donationToSave);

        factory.getDonationRepository().save(donationToSave);
    }


    public void updateDonation(Long id, UpdateDonationDto updateDonationDto) {
        Donation updatedDonation = factory.getDonationRepository().findById(id).get();
        updatedDonation.setAmount(updateDonationDto.getAmount());
        updatedDonation.setCurrency(updateDonationDto.getCurrency());
        updatedDonation.setCampaign(updateDonationDto.getCampaign());
        updatedDonation.setBenefactor(updateDonationDto.getBenefactor());
        updatedDonation.setNotes(updateDonationDto.getNotes());

        DonationValidator.donationValidation(updatedDonation);

        factory.getDonationRepository().save(updatedDonation);
    }

    public void deleteDonation(Long id){
        factory.getDonationRepository().deleteById(id);
    }

    public void approveDonation(Donation donation, User approvedBy){
            donation.setApproved(true);
            donation.setApprovedBy(approvedBy);
            donation.setApprovedDate(LocalDate.now());
            factory.getDonationRepository().save(donation);
    }

    public List<SimpleDonationDto> filterDonationsWithPaging(Specification<Donation> spec, Pageable pageable) {

        Page<Donation> donations =  factory.getDonationRepository().findAll(
                spec,
                pageable
        );

        return donations.stream().map(donation -> donationMapper.donationToSimpleDonationDto(donation)).collect(Collectors.toList());
    }
}
