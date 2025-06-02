package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Added
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, UUID>, JpaSpecificationExecutor<Organisation> { // Extended JpaSpecificationExecutor

    /**
     * Finds an organisation by its name.
     * Spring Data JPA will automatically generate the query based on the method name.
     *
     * @param name The name of the organisation to search for.
     * @return An Optional containing the organisation if found, or an empty Optional otherwise.
     */
    Optional<Organisation> findByName(String name);
}
