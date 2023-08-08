package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String additionalName;
    private String maidenName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "donator_campaign",
            joinColumns = @JoinColumn(name = "donator_id"),
            inverseJoinColumns = @JoinColumn(name = "campaign_id"))
    private Set<Campaign> campaigns = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "donation_id",referencedColumnName = "benefactor")
    private  Donation donation;
}
