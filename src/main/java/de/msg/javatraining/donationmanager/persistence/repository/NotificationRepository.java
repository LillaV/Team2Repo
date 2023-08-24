package de.msg.javatraining.donationmanager.persistence.repository;

import de.msg.javatraining.donationmanager.persistence.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
   void deleteByDateIsBetween(LocalDate startDate,LocalDate endDate);

   @Query(value = "select n from Notification n join n.users u where u.id = :userId")
   List<Notification> getAllUserNotifications(@Param("userId") Long userId);

   @Query(value = "select n from Notification n join n.users u where u.id = :userId")
   Page<Notification> getUserNotifications(@Param("userId") Long userId,PageRequest pageable);

   @Query(value = "select n from Notification n join n.users u where u.id = :userId order by n.id desc ")
   Page<Notification> recentNotifications(@Param("userId") Long userId, PageRequest pageable);

   @Query(value = "select count(n) from Notification  n join n.users u where u.id = :userId")
   Long getPossibleMaxPage(@Param("userId") Long userId);

}
