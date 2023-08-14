package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.mapstruct.Mapper;

@Mapper
public interface DonatorMapper {
    SimpleDonatorDto donatorToSimpleDonatorDto(Donator donator);

    Donator SimpleDonatorDtoToDonator(SimpleDonatorDto simpleDonatorDto);
}
