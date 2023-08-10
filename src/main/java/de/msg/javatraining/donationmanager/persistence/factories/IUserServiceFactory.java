package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;

public interface IUserServiceFactory{
    public UserRepository getUserRepository();
    public RoleRepository getRoleRepository();
    public  CampaignRepository getCampaignRepository();

}
