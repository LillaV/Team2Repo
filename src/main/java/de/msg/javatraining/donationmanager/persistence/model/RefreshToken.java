package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refreshToken")
public class RefreshToken {
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    @Id
    private String refreshToken;
    @Column(nullable = false)
    private Instant expiryDate;

}
