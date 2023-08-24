package de.msg.javatraining.donationmanager.config.notifications;

import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import de.msg.javatraining.donationmanager.persistence.model.enums.ERole;
import de.msg.javatraining.donationmanager.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.rmi.server.LogStream.log;

@Configuration
@EnableScheduling
@Slf4j
@ConditionalOnProperty(name ="scheduling.enable",matchIfMissing = false)
public class SchedulingConfiguration {

    @Autowired
    NotificationRepository repository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DonatorRepository donatorRepository;
    @Autowired
    DonationRepository donationRepository;



    @Scheduled(fixedRate = 30,timeUnit = TimeUnit.DAYS)
    @Transactional
    public void deleteExpiredNotifications(){
        repository.deleteByDateIsBetween(LocalDate.now().minusDays(30),LocalDate.now().minusDays(1));
    }



//    @Scheduled(fixedRate = 30,timeUnit = TimeUnit.DAYS)
//    @Transactional
//    public void populateDB (){
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//        Random r = new Random();
//        ERole[] roles = ERole.values();
//        EPermission[] permissions = EPermission.values();
//        int rNr = roles.length;
//        int pNr = permissions.length;
//        List<Campaign> campaigns = new ArrayList<>();
//        List<User> users = new ArrayList<>();
//        for(long i=1 ; i <= 160; i++){
//            Campaign campaign = new Campaign("campaign"+i,"purpose"+i , new HashSet<>());
//            Campaign campaign1 =campaignRepository.save(campaign);
//            campaigns.add(campaign1);
//        }
//        for(long i=0 ; i <= 160;i++){
//            String names = "user"+i;
//            Set<Role> userRoles = new HashSet<>();
//            int userNrOfRoles = r.nextInt(rNr);
//            boolean hasCampReposrtingRestricted = false;
//            for(int j=0; j <userNrOfRoles;j++){
//                int index = r.nextInt(rNr);
//                Set<Permission> userPermissions = new HashSet<>();
//                 int userNrOfPermissions = r.nextInt(pNr);
//                for(long k=0; k < userNrOfPermissions;k++){
//                    int pIndex = r.nextInt(pNr);
//                    userPermissions.add(new Permission(permissions[pIndex]));
//                    if(permissions[pIndex].name().equals(EPermission.AUTHORITY_CAMP_REPORTING_RESTRICTED.name())){
//                        hasCampReposrtingRestricted = true;
//                    }
//                }
//                userRoles.add(new Role(roles[index],userPermissions));
//            }
//            Set<Campaign> userCampaigns = new HashSet<>();
//            if(hasCampReposrtingRestricted){
//                int nrOfUserCampaigns = r.nextInt(160);
//                for(int j= 0 ; j < nrOfUserCampaigns;j++){
//                     int cindex = r.nextInt(160);
//                    Campaign campaign =  campaigns.get(cindex);
//                    userCampaigns.add(campaign);
//                }
//            }
//            String password ="password";
//            byte[] messageDigest = md.digest(password.getBytes());
//            BigInteger no = new BigInteger(1, messageDigest);
//            String hashtext = no.toString(16);
//            User  user  = new User(names,names,true,true,names,"0745682905",names+"@yahoo.ro",userCampaigns,hashtext,userRoles,0);
//            User user1 = userRepository.save(user);
//             users.add(user1);
//        }
//        ArrayList<Donator> donators = new ArrayList<>();
//
//        for(long i =1; i <= 160 ; i++){
//            String names = "donator"+i;
//            Donator donator = new Donator(names,names,names,names);
//            Donator donator1 = donatorRepository.save(donator);
//            donators.add(donator1);
//        }
//
//        for(int i=1; i <60;i++){
//            Donation donation = new Donation();
//            donation.setAmount(i);
//            donation.setCurrency("EUR");
//            donation.setApproved(false);
//            donation.setCreateDate(LocalDate.now());
//            User user = users.get(i);
//            donation.setCreatedBy(user);
//            donation.setNotes("notes"+i);
//            Donator donator = donators.get(i);
//            donation.setBenefactor(donator);
//            int campaignId = r.nextInt(160);
//            Campaign campaign = campaigns.get(campaignId);
//            donation.setCampaign(campaign);
//            donationRepository.save(donation);
//        }
//        } catch (NoSuchAlgorithmException e) {
//            log("Could  not   populate db because we couldn't get an instance of the  MD5 algorithm");
//        }
//    }
}
