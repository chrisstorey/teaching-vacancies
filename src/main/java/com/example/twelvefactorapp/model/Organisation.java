package com.example.twelvefactorapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point; // Import for JTS Point
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

// It's assumed that a dependency like org.hibernate:hibernate-spatial will be added to pom.xml for JTS Point mapping.

@Entity
@Table(name = "organisations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "urn")
    private String urn;

    @Column(name = "uid")
    private String uid;

    @Column(name = "ukprn")
    private String ukprn;

    @Column(name = "phase")
    private Integer phase;

    @Column(name = "url")
    private String url;

    @Column(name = "minimum_age")
    private Integer minimumAge;

    @Column(name = "maximum_age")
    private Integer maximumAge;

    @Column(name = "address")
    private String address;

    @Column(name = "town")
    private String town;

    @Column(name = "county")
    private String county;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "region")
    private String region;

    @Column(name = "locality")
    private String locality;

    @Column(name = "geopoint", columnDefinition = "geometry(Point,4326)") // Standard PostGIS type with SRID 4326 for WGS84
    private Point geopoint; // Changed from String to org.locationtech.jts.geom.Point

    @Column(name = "gias_data", columnDefinition = "jsonb")
    private String giasData; // TODO: Map to JSONB, potentially using a custom Hibernate UserType or a library like Hypersistence Utils.

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganisationVacancy> organisationVacancies;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
