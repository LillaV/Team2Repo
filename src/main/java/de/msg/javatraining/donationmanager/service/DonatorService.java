package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonatorMapper;
import de.msg.javatraining.donationmanager.persistence.factories.IDonatorServiceFactory;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.validation.DonatorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonatorService {
    @Autowired
    IDonatorServiceFactory factory;

    @Autowired
    DonatorMapper donatorMapper;
    public List<Donator> allDonatorsWithPagination(int offset, int pageSize){
        Page<Donator> donators =  factory.getDonatorRepository().findAll(PageRequest.of(offset, pageSize));
        return donators.stream().collect(Collectors.toList());
    }

    public Donator updateDonator(Long id, SimpleDonatorDto simpleDonatorDto) {
        Donator updatedDonator = factory.getDonatorRepository().findById(id).get();
        updatedDonator.setFirstName(simpleDonatorDto.getFirstName());
        updatedDonator.setLastName(simpleDonatorDto.getLastName());
        updatedDonator.setAdditionalName(simpleDonatorDto.getAdditionalName());
        updatedDonator.setMaidenName(simpleDonatorDto.getMaidenName());
        factory.getDonatorRepository().save(updatedDonator);
        return updatedDonator;
    }

    public void deleteDonatorById(Long id) {
        if(factory.getDonationRepository().existsByBenefactorId(id)){
            setToUnknown(id);
        } else {
            factory.getDonatorRepository().deleteById(id);
        }
    }

//    public void saveDonator(SimpleDonatorDto simpleDonatorDto) {
//        Donator donator = donatorMapper.SimpleDonatorDtoToDonator(simpleDonatorDto);
//        if (DonatorValidator.donatorValidation(donator)) {
//            factory.getDonatorRepository().save(donator);
//        } else {
//            System.out.println("Cannot save");
//        }
//    }
public void saveDonator(SimpleDonatorDto simpleDonatorDto) {
    Donator donator = donatorMapper.SimpleDonatorDtoToDonator(simpleDonatorDto);

    // Check if the donator already exists based on first name, last name, additional name, and maiden name
    if (!factory.getDonatorRepository().existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(
            donator.getFirstName(), donator.getLastName(), donator.getAdditionalName(), donator.getMaidenName())) {
        if (DonatorValidator.donatorValidation(donator)) {
            factory.getDonatorRepository().save(donator);
        } else {
            System.out.println("Cannot save");
        }
    } else {
        System.out.println("Donator already exists");
    }
}

    public Donator findById(Long id) {
        return factory.getDonatorRepository().findById(id).get();
    }



    public void setToUnknown(Long id){
        updateDonator(id, new SimpleDonatorDto("Unknown", "Unknown", "Unknown", "Unknown"));
    }
}
