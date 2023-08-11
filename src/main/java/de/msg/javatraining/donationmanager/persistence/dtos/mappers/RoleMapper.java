package de.msg.javatraining.donationmanager.persistence.dtos.mappers;
import de.msg.javatraining.donationmanager.persistence.dtos.role.RoleDto;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);
}
