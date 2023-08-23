package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import org.mapstruct.control.MappingControl;
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

  @Query(value = "SELECT u from User u JOIN u.roles r JOIN r.permissions p " +
          "WHERE p.permission= :permission")
  List<User> findAllByPermissions(@Param("permission") EPermission permission);
}
