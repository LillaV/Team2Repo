package de.msg.javatraining.donationmanager.persistence.dtos.mappers;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DonatorMapper {
    SimpleDonatorDto donatorToSimpleDonatorDto(Donator donator);

    Donator SimpleDonatorDtoToDonator(SimpleDonatorDto simpleDonatorDto);

    List<SimpleDonatorDto> donatorsToSimpleDonatorDtos(List<Donator> donators);
}
