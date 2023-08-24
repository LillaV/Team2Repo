package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campaign",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String purpose;
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST} , orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "donations")
    Set<Donation> donations;

    public Campaign(String name, String purpose, Set<Donation> donations) {
        this.name = name;
        this.purpose = purpose;
        this.donations = donations;
    }
}
