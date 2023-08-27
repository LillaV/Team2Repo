package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.mapstruct.Mapper;

@Mapper
public interface DonatorMapper {

    Donator SimpleDonatorDtoToDonator(SimpleDonatorDto simpleDonatorDto);
    SimpleDonatorDto donatorToSimpleDonatorDto(Donator donator);
}
