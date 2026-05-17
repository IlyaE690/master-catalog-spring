package kfu.itis.model.entity;

import jakarta.persistence.*;
import kfu.itis.model.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 70)
    private String username;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 60)
    private String firstName;

    @Column(length = 60)
    private String lastName;

    @Column(unique = true, length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double badWeatherPriceCoefficient = 1.20;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_specializations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")

    )
    @Builder.Default
    private Set<Specialization> specializations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Builder.Default
    private Set<Order> customerOrders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "master")
    @Builder.Default
    private Set<Order> masterOrders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @Builder.Default
    private Set<Review> writtenReviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "targetUser")
    @Builder.Default
    private Set<Review> receivedReviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Builder.Default
    private Set<FavoriteMaster> favoriteMasters = new HashSet<>();

    @OneToMany(fetch =  FetchType.LAZY, mappedBy = "master")
    @Builder.Default
    private Set<FavoriteMaster> favoriteBy = new HashSet<>();

    @PrePersist
    protected void onCreated() {
        this.createdAt = LocalDateTime.now();
    }




}
