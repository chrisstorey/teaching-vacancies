package com.example.twelvefactorapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // Added
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List; // For Vacancy relationship
import java.util.UUID;

@Entity
@Table(name = "publishers", indexes = {
    @Index(name = "index_publishers_on_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Added
@EntityListeners(AuditingEntityListener.class)
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // TODO: Ensure this is always stored hashed

    private String firstName; // Corresponds to 'given_name' potentially

    private String lastName;  // Corresponds to 'family_name' potentially

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Existing relationships (from original Rails model context)
    // A Publisher may be associated with multiple vacancies they posted
    @OneToMany(mappedBy = "publisher") // Assuming 'publisher' field in Vacancy entity
    private List<Vacancy> vacancies;

    // TODO: Link to specific Organisation(s) upon registration or through a separate profile completion step.
    //       This might be a @ManyToOne or @ManyToMany relationship to Organisation entity.
    //       For example:
    // @ManyToOne
    // @JoinColumn(name = "organisation_id")
    // private Organisation organisation; // If a publisher belongs to one primary org
    //
    // Or if a publisher can be part of multiple organisations (e.g. MAT structure)
    // @ManyToMany
    // @JoinTable(
    //   name = "publisher_organisations",
    //   joinColumns = @JoinColumn(name = "publisher_id"),
    //   inverseJoinColumns = @JoinColumn(name = "organisation_id"))
    // private Set<Organisation> organisations;

    // TODO: Add Devise-like trackable features (last_sign_in_at, etc.) in later iterations.
    // TODO: Consider confirmable (email confirmation), lockable, and other security features.

    // Constructor for stubbing if still needed, though @AllArgsConstructor covers more
    public Publisher(UUID id) {
        this.id = id;
    }
}
