package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CampaignRepository  extends JpaRepository<Campaign, Long>, JpaSpecificationExecutor<Campaign> {

    @Query("select c from  Campaign c join User  u join u.campaigns uc on uc.id= c.id and u.id = :userId")
    List<Campaign> getCustomCampaignList(@Param("userId") Long userId);

    @Query("select c from  Campaign c join User  u join u.campaigns uc on uc.id= c.id and u.id = :userId")
    Page<Campaign> getCustomCampaignList(@Param("userId")Long id, PageRequest of);

    @Query("select c from  Campaign c where c.name like '%:text%'")
    Page<Campaign> searchForCampaign(@Param("text")String text,PageRequest pageRequest);
}
