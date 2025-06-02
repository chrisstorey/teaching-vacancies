package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.PublisherAtsApiClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublisherAtsApiClientRepository extends JpaRepository<PublisherAtsApiClient, UUID> {

    /**
     * Finds a PublisherAtsApiClient by its API key.
     *
     * @param apiKey The API key to search for.
     * @return An Optional containing the PublisherAtsApiClient if found, or an empty Optional otherwise.
     */
    Optional<PublisherAtsApiClient> findByApiKey(String apiKey);
}
