package com.example.twelvefactorapp.model;

import com.example.twelvefactorapp.model.converter.WorkingPatternsConverter;
import com.example.twelvefactorapp.model.enums.ContractType;
import com.example.twelvefactorapp.model.enums.VacancyStatus; // Added
import com.example.twelvefactorapp.model.enums.WorkingPattern;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;
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
    @Column(name = "working_patterns")
    private List<WorkingPattern> workingPatterns;

    // 'key_stages' from DTO, maps to 'phases' in DTO. Assuming 'keyStages' is the existing field.
    @Column(name = "key_stages") 
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "vacancy_key_stages", joinColumns = @JoinColumn(name = "vacancy_id"))
    private List<String> keyStages; // This will be mapped to 'phases' in DTO

    @Column(name = "publish_on")
    private LocalDate publishOn;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "starts_on")
    private LocalDate startsOn;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "geolocation", columnDefinition = "geometry(Point,4326)")
    private Point geolocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id") // This is for the regular publisher (user)
    private Publisher publisher;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganisationVacancy> organisationVacancies;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // New fields for ATS API requirements
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VacancyStatus status = VacancyStatus.DRAFT; // Default status

    @Column(name = "external_reference")
    private String externalReference;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "vacancy_job_roles", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(name = "job_role") // Column name for each element in the collection
    private List<String> jobRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_ats_api_client_id") // Foreign key to PublisherAtsApiClient
    private PublisherAtsApiClient publisherAtsApiClient;
}
