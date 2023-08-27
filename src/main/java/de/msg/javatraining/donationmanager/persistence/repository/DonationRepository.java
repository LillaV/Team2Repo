package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long>, JpaSpecificationExecutor<Donation> {
    boolean existsByBenefactorId(Long id);

    boolean existsByCampaignAndApprovedTrue(Campaign campaign);

    @Query("SELECT DISTINCT d.currency FROM Donation d")
    List<String> getDistinctCurrencies();

    @Modifying
    @Query("UPDATE Donation d SET d.benefactor = null WHERE d.benefactor.id = :donatorId")
    void deleteBenefactorId(@Param("donatorId") Long benefactorId);

    @Query("SELECT DISTINCT d.benefactor FROM Donation d WHERE d.campaign.id = :campaignId")
    List<Donator> findDistinctBenefactorsByCampaignId(@Param("campaignId") Long campaignId);
}
