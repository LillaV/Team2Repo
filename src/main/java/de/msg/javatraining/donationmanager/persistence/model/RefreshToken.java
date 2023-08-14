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
public class RefreshToken {
    @Id
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(nullable = false)
    private Instant expiryDate;

}
