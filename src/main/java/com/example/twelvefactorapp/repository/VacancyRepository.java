package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Added
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, UUID>, JpaSpecificationExecutor<Vacancy> { // Extended JpaSpecificationExecutor

    /**
     * Finds vacancies by job title.
     * Spring Data JPA will automatically generate the query based on the method name.
     *
     * @param jobTitle The job title to search for.
     * @return A list of vacancies matching the job title.
     */
    List<Vacancy> findByJobTitle(String jobTitle);

    /**
     * Finds vacancies for a specific ATS API client, excluding TRASHED ones,
     * ordered by publishOn descending.
     * Eagerly fetches organisation data to prevent N+1 issues when mapping to DTOs.
     *
     * @param apiClientId The ID of the PublisherAtsApiClient.
     * @param statusNot   The status to exclude (e.g., VacancyStatus.TRASHED).
     * @param pageable    Pagination information.
     * @return A Page of vacancies.
     */
    @EntityGraph(attributePaths = {"organisationVacancies.organisation", "publisherAtsApiClient"})
    @Query("SELECT v FROM Vacancy v " +
           "WHERE v.publisherAtsApiClient.id = :apiClientId AND v.status <> :statusNot " +
           "ORDER BY v.publishOn DESC, v.createdAt DESC") // Added createdAt for secondary sort
    Page<Vacancy> findByPublisherAtsApiClientIdAndStatusNotOrderByPublishOnDescCreatedAtDesc(
            @Param("apiClientId") UUID apiClientId,
            @Param("statusNot") VacancyStatus statusNot,
            Pageable pageable);

    /**
     * Finds a single vacancy by its ID, associated PublisherAtsApiClient ID, and status.
     * Eagerly fetches organisation data and other relevant associations for a detailed view.
     *
     * @param id          The ID of the Vacancy.
     * @param apiClientId The ID of the PublisherAtsApiClient.
     * @param status      The required status of the Vacancy (e.g., PUBLISHED).
     * @return An Optional containing the vacancy if found and conditions are met.
     */
    @EntityGraph(attributePaths = {
        "organisationVacancies.organisation", // For organisation details
        "publisherAtsApiClient",              // For client details if needed
        "publisher"                           // For original publisher user details if needed
    })
    Optional<Vacancy> findByIdAndPublisherAtsApiClientIdAndStatus(
            UUID id,
            UUID apiClientId,
            VacancyStatus status
    );

    // Example of a more complex query method (optional, just for illustration)
    // List<Vacancy> findByJobTitleContainingIgnoreCaseAndContractType(String jobTitle, com.example.twelvefactorapp.model.enums.ContractType contractType);
}
