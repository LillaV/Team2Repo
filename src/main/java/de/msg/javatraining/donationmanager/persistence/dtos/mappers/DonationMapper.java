package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import org.mapstruct.Mapper;

@Mapper
public interface DonationMapper {
    SimpleDonationDto donationToSimpleDonationDto(Donation donation);

    Donation simpleDonationDtoToDonation(SimpleDonationDto simpleDonationDto);

}
