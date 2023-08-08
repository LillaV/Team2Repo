package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;

    private float amount;

    private LocalDate createDate;

    private boolean approved;

    private LocalDate approvedDate;

    private String notes;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User approvedBy;

    @ManyToOne
    private Campaign campaign;

    @OneToMany
    private Set<Donator> benefactor;
}