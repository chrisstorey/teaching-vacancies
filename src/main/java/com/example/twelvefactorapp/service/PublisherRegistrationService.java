package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.dto.PublisherRegistrationRequest;
import com.example.twelvefactorapp.exception.EmailAlreadyExistsException; // Reusing this exception
import com.example.twelvefactorapp.model.Publisher;
import com.example.twelvefactorapp.repository.PublisherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherRegistrationService {

    private final PublisherRepository publisherRepository;
    private final PasswordEncoder passwordEncoder;

    public PublisherRegistrationService(PublisherRepository publisherRepository, PasswordEncoder passwordEncoder) {
        this.publisherRepository = publisherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new publisher with the provided details.
     *
     * @param email     The email address of the publisher.
     * @param password  The plain text password for the publisher.
     * @param firstName The first name of the publisher.
     * @param lastName  The last name of the publisher.
     * @return The created and saved Publisher entity.
     * @throws EmailAlreadyExistsException if the email is already registered.
     */
    @Transactional
    public Publisher registerNewPublisher(String email, String password, String firstName, String lastName) {
        if (publisherRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email address " + email + " is already registered by another publisher.");
        }

        Publisher newPublisher = new Publisher();
        newPublisher.setEmail(email);
        newPublisher.setPassword(passwordEncoder.encode(password)); // Encode the password
        newPublisher.setFirstName(firstName);
        newPublisher.setLastName(lastName);
        // createdAt and updatedAt will be set automatically by Hibernate/JPA auditing

        return publisherRepository.save(newPublisher);
    }

    /**
     * Registers a new publisher using details from a registration request DTO.
     *
     * @param request The DTO containing publisher registration details.
     * @return The created and saved Publisher entity.
     * @throws EmailAlreadyExistsException if the email is already registered.
     */
    @Transactional
    public Publisher registerNewPublisher(PublisherRegistrationRequest request) {
        return registerNewPublisher(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );
    }
}
