package de.msg.javatraining.donationmanager.persistence.factories;

import de.msg.javatraining.donationmanager.persistence.repository.CampaignRepository;
import de.msg.javatraining.donationmanager.persistence.repository.PermissionRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFactory implements IUserServiceFactory{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private PermissionRepository permissionRepository;



    public UserRepository getUserRepository() {
        return userRepository;
    }


    public RoleRepository getRoleRepository() {
        return roleRepository;
    }


    public CampaignRepository getCampaignRepository() {
        return campaignRepository;
    }

    @Override
    public PermissionRepository getPermissionRepository() {
        return permissionRepository;
    }
}
