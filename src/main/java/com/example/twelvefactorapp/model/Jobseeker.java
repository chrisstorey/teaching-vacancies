package com.example.twelvefactorapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // For Spring Data JPA auditing, if enabled

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobseekers", indexes = {
    @Index(name = "index_jobseekers_on_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // If using Spring Data JPA's auditing for @CreatedDate, @LastModifiedDate
public class Jobseeker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // TODO: Ensure this is always stored hashed (e.g., by Spring Security's PasswordEncoder)

    private String firstName;

    private String lastName;

    @CreationTimestamp // Automatically set by Hibernate on creation
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp // Automatically set by Hibernate on creation and update
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // TODO: Add fields for profile (e.g., phone_number, desired_job_title, resume_path),
    //       job applications (e.g., @OneToMany List<JobApplication>),
    //       and Devise-like trackable features (last_sign_in_at, current_sign_in_at,
    //       sign_in_count, last_sign_in_ip, current_sign_in_ip) in later iterations.
    // TODO: Consider confirmable (email confirmation), lockable, and other security features.
}
