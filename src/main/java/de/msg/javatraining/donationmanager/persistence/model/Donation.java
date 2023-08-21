package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

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
    private float amount;
    @Column(nullable = false)
    private LocalDate createDate;
    private boolean approved;
    private LocalDate approvedDate;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "cuser_id")
    private User createdBy;
    @ManyToOne
    @JoinColumn(name = "auser_id")
    private User approvedBy;
//    @ManyToOne(cascade = CascadeType.PERSIST)
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(
            name = "campaign_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "FK_CAMPAIGN_ID",
                    foreignKeyDefinition = "FOREIGN KEY (campaign_id) REFERENCES campaign(id) ON DELETE CASCADE"
            )
    )
    private Campaign campaign;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(cascade = CascadeType.PERSIST)
    @ManyToOne
    @JoinColumn(name = "donator_id")
    private Donator benefactor;
}