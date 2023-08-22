package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  @Override
  Optional<User> findById(Long aLong);

  @Query(nativeQuery = true, value = "SELECT u"+
          "FROM User u "+
          "JOIN user_campaign uc ON u.id=uc.user_id"+
          "WHERE uc.campaign_id=:campaignId")
  List<User> findAllByCampaignsIsContaining(@Param("campaignId") Long campaignId);
}
