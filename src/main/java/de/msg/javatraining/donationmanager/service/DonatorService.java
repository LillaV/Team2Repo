package de.msg.javatraining.donationmanager.service;

import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonatorMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.DonationRepository;
import de.msg.javatraining.donationmanager.persistence.repository.DonatorRepository;
import de.msg.javatraining.donationmanager.service.validation.DonatorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonatorService {
    @Autowired
    DonationRepository donationRepository;

    @Autowired
    DonatorRepository donatorRepository;

    @Autowired
    DonatorMapper donatorMapper;

    @Autowired
    DonatorValidator donatorValidator;
    public List<SimpleDonatorDto> allDonatorsWithPagination(int offset, int pageSize){
        Page<Donator> donators =  donatorRepository.findAll(PageRequest.of(offset, pageSize));
        return donators.stream().map(donator -> donatorMapper.donatorToSimpleDonatorDto(donator)).collect(Collectors.toList());
    }

    public List<SimpleDonatorDto> getDonators(){
        List<Donator> donators=donatorRepository.findAll();
        return donatorMapper.donatorsToSimpleDonatorDtos(donators);
    }

    public TextResponse updateDonator(Long id, SimpleDonatorDto simpleDonatorDto) {
        Donator updatedDonator = donatorRepository.findById(id).get();
        updatedDonator.setFirstName(simpleDonatorDto.getFirstName());
        updatedDonator.setLastName(simpleDonatorDto.getLastName());
        updatedDonator.setAdditionalName(simpleDonatorDto.getAdditionalName());
        updatedDonator.setMaidenName(simpleDonatorDto.getMaidenName());
        donatorRepository.save(updatedDonator);
        return new TextResponse("Donator updated successfully");
    }

    public TextResponse deleteDonatorById(Long id) {
        if (donationRepository.existsByBenefactorId(id)) {
            donationRepository.deleteBenefactorId(id);
        }
        donatorRepository.deleteById(id);
        return new TextResponse("Donation deleted successfully!");
    }

    public TextResponse saveDonator(SimpleDonatorDto simpleDonatorDto) {
        Donator donator = donatorMapper.SimpleDonatorDtoToDonator(simpleDonatorDto);
        if (!donatorRepository.existsByFirstNameAndLastNameAndAdditionalNameAndMaidenName(donator.getFirstName(), donator.getLastName(), donator.getAdditionalName(), donator.getMaidenName())) {
            if (donatorValidator.validate(donator)) {
                donatorRepository.save(donator);
                return new TextResponse("Donator saved successfully");
            } else {
                throw new InvalidRequestException("Invalid data!");
            }
        } else {
            throw new InvalidRequestException("Donator already exists!");
        }
    }

    public SimpleDonatorDto findById(Long id) {
        Optional<Donator> donator = donatorRepository.findById(id);
        if (donator.isPresent()) {
            return donatorMapper.donatorToSimpleDonatorDto(donator.get());
        }
        throw new InvalidRequestException("Donator doesn't exists!");
    }

    public long getSize(){
        return donatorRepository.count();
    }
}
