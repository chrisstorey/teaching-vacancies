package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.ContractType;
// import com.example.twelvefactorapp.model.enums.WorkingPattern; // Already imported if needed by other parts
import com.example.twelvefactorapp.repository.OrganisationRepository;
import com.example.twelvefactorapp.repository.VacancyRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order; // Added for JPA Order
import jakarta.persistence.criteria.Predicate;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl; // For manual page creation if needed
import org.springframework.data.domain.PageRequest; // For manual page creation if needed
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // For Sort object
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class VacancyService {

    private static final Logger logger = LoggerFactory.getLogger(VacancyService.class);

    private final VacancyRepository vacancyRepository;
    private final OrganisationRepository organisationRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final String SIMULATED_LONDON_COORDS_KEY = "LONDON_COORDS";


    public VacancyService(VacancyRepository vacancyRepository, OrganisationRepository organisationRepository) {
        this.vacancyRepository = vacancyRepository;
        this.organisationRepository = organisationRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Vacancy> findVacancyById(UUID id) {
        return vacancyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Vacancy> findAllVacancies(Pageable pageable) {
        return vacancyRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Vacancy> searchVacancies(String keyword, String location, Integer radius,
                                         Map<String, String> filters, Pageable pageable) {

        Point searchPoint = null;
        // TODO: Replace simulated geocoding with a real geocoding client/service.
        if (StringUtils.hasText(location)) {
            if (SIMULATED_LONDON_COORDS_KEY.equalsIgnoreCase(location)) {
                searchPoint = geometryFactory.createPoint(new Coordinate(-0.1278, 51.5074));
                logger.info("Simulated geocoding for {}: {}", location, searchPoint);
            } else {
                logger.warn("Location parameter '{}' provided, but no simulated geocoding available for it. Location search will be skipped for ST_DWithin and distance sort.", location);
            }
        }

        final Point effectiveSearchPoint = searchPoint;

        Specification<Vacancy> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.like(cb.lower(root.get("jobTitle")), "%" + keyword.toLowerCase() + "%"));
            }

            boolean locationSearchActive = false;
            if (effectiveSearchPoint != null && radius != null && radius > 0) {
                predicates.add(cb.isTrue(cb.function("ST_DWithin", Boolean.class,
                        root.get("geolocation"),
                        cb.literal(effectiveSearchPoint),
                        cb.literal(radius)
                )));
                locationSearchActive = true;
                logger.info("Added ST_DWithin predicate: Point={}, Radius={}", effectiveSearchPoint, radius);
            } else {
                if (StringUtils.hasText(location) || (radius != null && radius > 0)) {
                     logger.warn("Location search was attempted but conditions were not fully met. Skipping ST_DWithin predicate and distance sort.");
                }
            }

            if (filters != null) {
                // Contract Type Filter
                String contractTypeFilter = filters.get("contractType");
                if (StringUtils.hasText(contractTypeFilter)) {
                    try {
                        ContractType contractType = ContractType.valueOf(contractTypeFilter.toUpperCase());
                        predicates.add(cb.equal(root.get("contractType"), contractType));
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid contractType filter value: {}", contractTypeFilter);
                    }
                }
                // Working Patterns Filter
                String workingPatternFilter = filters.get("workingPatterns");
                 if (StringUtils.hasText(workingPatternFilter)) {
                    logger.warn("LIKE on a List<WorkingPattern> attribute ('workingPatterns') via Criteria API is complex. This is a placeholder.");
                    // TODO: The LIKE on workingPatterns (a List<WorkingPattern> converted to String) needs verification and possibly a custom solution
                    predicates.add(cb.like(cb.lower(root.get("workingPatterns").as(String.class)), "%" + workingPatternFilter.toLowerCase() + "%"));
                }

                // Phases Filter (Advanced Filter)
                String phaseFilter = filters.get("phases");
                if (StringUtils.hasText(phaseFilter)) {
                    // Assuming Vacancy entity has a 'phasesCsvString' field (String) for this simple LIKE search
                    // TODO: Implement more robust filtering for 'phases' collection (e.g., array overlap if DB supports, proper enum list mapping).
                    predicates.add(cb.like(cb.lower(root.get("phasesCsvString")), "%" + phaseFilter.toLowerCase() + "%"));
                    logger.info("Added LIKE predicate for phasesCsvString: {}", phaseFilter);
                }

                // Job Roles Filter (Advanced Filter)
                String jobRoleFilter = filters.get("job_roles");
                if (StringUtils.hasText(jobRoleFilter)) {
                    // Assuming Vacancy entity has a 'jobRolesCsvString' field (String)
                    // TODO: Implement more robust filtering for 'job_roles' collection.
                    predicates.add(cb.like(cb.lower(root.get("jobRolesCsvString")), "%" + jobRoleFilter.toLowerCase() + "%"));
                    logger.info("Added LIKE predicate for jobRolesCsvString: {}", jobRoleFilter);
                }
            }
            
            if (locationSearchActive && query.getResultType() == Vacancy.class) { 
                Expression<Double> distanceExpression = cb.function("ST_Distance", Double.class,
                        root.get("geolocation"),
                        cb.literal(effectiveSearchPoint)
                );
                query.orderBy(cb.asc(distanceExpression));
                logger.info("Attempting to order by ST_Distance.");
            }
            // TODO: If the above query.orderBy() is ineffective due to Spring Data Pageable overriding it... (rest of comment remains)

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return vacancyRepository.findAll(spec, pageable);
    }
}
