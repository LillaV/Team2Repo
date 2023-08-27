package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Modifying
    @Query("delete from RefreshToken where user.id = :id")
    void deleteRefreshTokenByUser(@Param("id") Long id);
}
