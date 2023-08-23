package de.msg.javatraining.donationmanager.config.notifications;

import de.msg.javatraining.donationmanager.persistence.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name ="scheduling.enable",matchIfMissing = false)
public class SchedulingConfiguration {

    @Autowired
    NotificationRepository repository;

    @Scheduled(fixedRate = 30,timeUnit = TimeUnit.DAYS)
    @Transactional
    public void deleteExpiredNotifications(){
        repository.deleteByDateIsBetween(LocalDate.now().minusDays(30),LocalDate.now().minusDays(1));
    }
}
