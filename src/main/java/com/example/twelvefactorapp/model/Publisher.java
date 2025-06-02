package com.example.twelvefactorapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "publishers") // Assuming the table name is 'publishers'
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // For @CreationTimestamp and @UpdateTimestamp if not using Hibernate specific ones directly
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Add other fields relevant to a Publisher if known, e.g., name
    // For this example, we'll keep it minimal as requested.
    // private String name;

    // Example of a field that might be encrypted in Rails
    // For demonstration, let's assume 'email' could be an encrypted field
    private String email; // TODO: Handle Rails-compatible encryption/decryption or re-encryption strategy if this was encrypted.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Minimal constructor for stubbing
    public Publisher(UUID id) {
        this.id = id;
    }
}
