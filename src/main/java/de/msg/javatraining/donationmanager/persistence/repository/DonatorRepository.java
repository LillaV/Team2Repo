package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonatorRepository extends JpaRepository<Donator,Long> {
}
