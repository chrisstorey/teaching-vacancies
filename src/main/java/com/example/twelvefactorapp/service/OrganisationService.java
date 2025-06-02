package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.model.Organisation;
import com.example.twelvefactorapp.repository.OrganisationRepository;
import jakarta.persistence.criteria.Predicate;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganisationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganisationService.class);

    private final OrganisationRepository organisationRepository;

    // GeometryFactory is thread-safe and can be reused. SRID 4326 is for WGS84.
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final String SIMULATED_LONDON_COORDS_KEY = "LONDON_COORDS";

    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Organisation> findOrganisationById(UUID id) {
        return organisationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Organisation> findAllOrganisations(Pageable pageable) {
        return organisationRepository.findAll(pageable);
    }

    /**
     * Searches for organisations based on name and location (PostGIS ST_DWithin).
     *
     * @param name     The name of the organisation for a keyword-like search (case-insensitive LIKE).
     * @param location The location string (e.g., "LONDON_COORDS" for simulated geocoding).
     * @param radius   The radius in meters for the location search.
     * @param pageable The pagination information.
     * @return A Page of matching organisations.
     */
    @Transactional(readOnly = true)
    public Page<Organisation> searchOrganisations(String name, String location, Integer radius, Pageable pageable) {
        Point searchPoint = null;
        // TODO: Replace simulated geocoding with a real geocoding client/service.
        if (StringUtils.hasText(location)) {
            if (SIMULATED_LONDON_COORDS_KEY.equalsIgnoreCase(location)) {
                // Approximate coordinates for London (longitude -0.1278, latitude 51.5074)
                searchPoint = geometryFactory.createPoint(new Coordinate(-0.1278, 51.5074));
                logger.info("Simulated geocoding for {}: {}", location, searchPoint);
            } else {
                logger.warn("Location parameter '{}' provided, but no simulated geocoding available for it. Location search will be skipped.", location);
            }
        }

        final Point effectiveSearchPoint = searchPoint; // For use in lambda

        Specification<Organisation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Name Search (Keyword-like)
            if (StringUtils.hasText(name)) {
                // TODO: Consider Full-Text Search for Organisation name for better performance and relevance.
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // Location Filter (PostGIS ST_DWithin)
            if (effectiveSearchPoint != null && radius != null && radius > 0) {
                predicates.add(cb.isTrue(cb.function("ST_DWithin", Boolean.class,
                        root.get("geopoint"),             // The geometry column in Organisation entity
                        cb.literal(effectiveSearchPoint), // The search point
                        cb.literal(radius)                // The radius in meters
                )));
                logger.info("Added ST_DWithin predicate for Organisation search: Point={}, Radius={}", effectiveSearchPoint, radius);
            } else {
                if (StringUtils.hasText(location) || (radius != null && radius > 0)) {
                     logger.warn("Organisation location search was attempted but conditions were not fully met (e.g. geocoding failed or radius missing/invalid). Skipping ST_DWithin predicate.");
                }
            }
            
            // TODO: Consider adding distance sorting if location search is active and results are to be ordered by proximity.
            // This would likely require a native query or a more complex Criteria API setup if done with Pageable.

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return organisationRepository.findAll(spec, pageable);
    }
}
