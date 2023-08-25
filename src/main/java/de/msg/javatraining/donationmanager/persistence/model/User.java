package de.msg.javatraining.donationmanager.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private boolean active;
    private boolean newUser;
    private String username;
    private String mobileNumber;
    @Column(nullable = false)
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_campaign",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "campaign_id"))
    private Set<Campaign> campaigns = new HashSet<>();
    private String password;
    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private Integer failedLoginAttempts=0;

    public User(String firstName, String lastName, boolean active, boolean newUser, String username, String mobileNumber, String email, Set<Campaign> campaigns, String password, Set<Role> roles, Integer failedLoginAttempts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.newUser = newUser;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.campaigns = campaigns;
        this.password = password;
        this.roles = roles;
        this.failedLoginAttempts = failedLoginAttempts;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Mobile Number: " + mobileNumber + "\n" +
                "Email: " + email + "\n" +
                "Roles:" + roles.toString();
    }
}
