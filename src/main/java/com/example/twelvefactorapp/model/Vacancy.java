package com.example.twelvefactorapp.model;

import com.example.twelvefactorapp.model.converter.WorkingPatternsConverter;
import com.example.twelvefactorapp.model.enums.ContractType;
// import com.example.twelvefactorapp.model.enums.KeyStage; // Keep if keyStages is also being converted, remove if not used
import com.example.twelvefactorapp.model.enums.WorkingPattern;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point; // Import for JTS Point
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// It's assumed that a dependency like org.hibernate:hibernate-spatial will be added to pom.xml for JTS Point mapping.

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "job_advert", columnDefinition = "TEXT")
    private String jobAdvert;

    @Column(name = "salary")
    private String salary;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @Convert(converter = WorkingPatternsConverter.class)
    @Column(name = "working_patterns") // Assumes this column stores data like "FULL_TIME,PART_TIME"
    private List<WorkingPattern> workingPatterns; // Changed from List<String>

    @Column(name = "key_stages") // TODO: Map to List<KeyStage> with custom converter if needed (similar to working_patterns)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY) // Keeping this as is, per original request to only change working_patterns
    @CollectionTable(name = "vacancy_key_stages", joinColumns = @JoinColumn(name = "vacancy_id"))
    private List<String> keyStages;

    @Column(name = "publish_on")
    private LocalDate publishOn;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "starts_on")
    private LocalDate startsOn;

    @Column(name = "contact_email")
    private String contactEmail; // TODO: If this were an encrypted field in Rails, add: Handle Rails-compatible encryption/decryption or re-encryption strategy.

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "geolocation", columnDefinition = "geometry(Point,4326)") // Standard PostGIS type with SRID 4326 for WGS84
    private Point geolocation; // Changed from String to org.locationtech.jts.geom.Point

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganisationVacancy> organisationVacancies;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
