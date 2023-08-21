package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation,Long>, JpaSpecificationExecutor<Donation> {
    boolean existsByBenefactorId(Long id);

    boolean existsByCampaignAndApprovedTrue(Campaign campaign);

    @Query("SELECT DISTINCT d.currency FROM Donation d")
    List<String> getDistinctCurrencies();

    List<Donation> findAllByAmountBetween(float minValue, float maxValue);
    List<Donation> findAllByAmountLessThanEqual(float value);
    List<Donation> findAllByAmountGreaterThanEqual(float value);
    List<Donation> findAllByCurrency(String currency);
    List<Donation> findAllByCampaignId(Long id);
    List<Donation> findAllByCampaignPurposeContainingIgnoreCase(String searchTerm);
    List<Donation> findAllByCreatedById(Long id);
    List<Donation> findAllByCreateDate(LocalDate createDate);
    List<Donation> findAllByCreateDateBetween(LocalDate startDate, LocalDate endDate);
    List<Donation> findAllByBenefactorId(Long id);
    List<Donation> findAllByApproved(boolean approved);
    List<Donation> findAllByApprovedById(Long id);
    List<Donation> findAllByApprovedDate(LocalDate approvedDate);
    List<Donation> findAllByApprovedDateBetween(LocalDate startDate, LocalDate endDate);

}
