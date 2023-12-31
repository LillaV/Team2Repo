package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private Float amount;
    @Column(nullable = false)
    private LocalDate createDate;
    private Boolean approved;
    private LocalDate approvedDate;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "cuser_id", nullable = false)
    private User createdBy;
    @ManyToOne
    @JoinColumn(name = "auser_id")
    private User approvedBy;
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;
    @ManyToOne
    @JoinColumn(name = "donator_id")
    private Donator benefactor;
}