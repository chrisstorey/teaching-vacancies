package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.Jobseeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobseekerRepository extends JpaRepository<Jobseeker, UUID> {

    /**
     * Finds a jobseeker by their email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the Jobseeker if found, or an empty Optional otherwise.
     */
    Optional<Jobseeker> findByEmail(String email);
}
