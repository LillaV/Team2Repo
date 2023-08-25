package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonatorMapper;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import de.msg.javatraining.donationmanager.service.validation.DonatorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonatorService {

    @Autowired
    DonatorRepository donatorRepository;

    @Autowired
    DonatorMapper donatorMapper;

    @Autowired
    DonatorValidator donatorValidator;

    @Autowired
    DonationRepository donationRepository;

    public List<Donator> allDonatorsWithPagination(int offset, int pageSize){
        Page<Donator> donators =  donatorRepository.findAll(PageRequest.of(offset, pageSize));
        return donators.stream().collect(Collectors.toList());
    }

    public List<Donator> getDonators(){
        return donatorRepository.findAll();
    }

    public Donator updateDonator(Long id, SimpleDonatorDto simpleDonatorDto) {
        Donator updatedDonator = donatorRepository.findById(id).get();
        updatedDonator.setFirstName(simpleDonatorDto.getFirstName());
        updatedDonator.setLastName(simpleDonatorDto.getLastName());
        updatedDonator.setAdditionalName(simpleDonatorDto.getAdditionalName());
        updatedDonator.setMaidenName(simpleDonatorDto.getMaidenName());
        donatorRepository.save(updatedDonator);
        return updatedDonator;
    }

    public void deleteDonatorById(Long id) {
        if(donationRepository.existsByBenefactorId(id)){
            donationRepository.deleteBenefactorId(id);
        }

        donatorRepository.deleteById(id);
    }

public void saveDonator(SimpleDonatorDto simpleDonatorDto) {
    Donator donator = donatorMapper.SimpleDonatorDtoToDonator(simpleDonatorDto);

    // Check if the donator already exists based on first name, last name, additional name, and maiden name
    if (!donatorRepository.existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(
            donator.getFirstName(), donator.getLastName(), donator.getAdditionalName(), donator.getMaidenName())) {
        if (donatorValidator.validate(donator)) {
            donatorRepository.save(donator);
        } else {
            System.out.println("Cannot save");
        }
    } else {
        System.out.println("Donator already exists");
    }
}

    public Donator findById(Long id) {
        return donatorRepository.findById(id).get();
    }



    public void setToUnknown(Long id){
        updateDonator(id, new SimpleDonatorDto("Unknown", "Unknown", "Unknown", "Unknown"));
    }

    public long getSize(){
        return donatorRepository.count();
    }
}
