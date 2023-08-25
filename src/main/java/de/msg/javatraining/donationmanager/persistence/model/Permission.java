package de.msg.javatraining.donationmanager.persistence.model;

import de.msg.javatraining.donationmanager.persistence.model.enums.EPermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private EPermission permission;

    public Permission(EPermission permission) {
        this.permission = permission;
    }

}
