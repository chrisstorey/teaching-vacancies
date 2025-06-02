package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {

    /**
     * Finds a publisher by their email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the Publisher if found, or an empty Optional otherwise.
     */
    Optional<Publisher> findByEmail(String email);
}
