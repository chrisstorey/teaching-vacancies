package com.example.twelvefactorapp.model;

import com.example.twelvefactorapp.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_applications") // Standard Rails table name would be job_applications
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(name = "completed_steps", columnDefinition = "TEXT") // Storing as TEXT for flexibility (e.g. CSV, JSON)
    private String completedSteps; // TODO: Consider JSONB or a separate Step entity/collection for more robust step management.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private Jobseeker jobseeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = ApplicationStatus.DRAFT;
        }
    }

    // TODO: Incorporate all other fields from Rails JobApplication model,
    //       especially has_encrypted fields (e.g., for personal statements, disability info),
    //       and other application form fields (education, experience, etc.) as application drafting progresses.
    // TODO: Consider fields for application submission timestamp, withdrawal timestamp, etc.
}
