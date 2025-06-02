package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.dto.JobseekerRegistrationRequest;
import com.example.twelvefactorapp.exception.EmailAlreadyExistsException;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.repository.JobseekerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobseekerRegistrationService {

    private final JobseekerRepository jobseekerRepository;
    private final PasswordEncoder passwordEncoder;

    public JobseekerRegistrationService(JobseekerRepository jobseekerRepository, PasswordEncoder passwordEncoder) {
        this.jobseekerRepository = jobseekerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new jobseeker with the provided details.
     *
     * @param email     The email address of the jobseeker.
     * @param password  The plain text password for the jobseeker.
     * @param firstName The first name of the jobseeker.
     * @param lastName  The last name of the jobseeker.
     * @return The created and saved Jobseeker entity.
     * @throws EmailAlreadyExistsException if the email is already registered.
     */
    @Transactional
    public Jobseeker registerNewJobseeker(String email, String password, String firstName, String lastName) {
        if (jobseekerRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email address " + email + " is already registered.");
        }

        Jobseeker newJobseeker = new Jobseeker();
        newJobseeker.setEmail(email);
        newJobseeker.setPassword(passwordEncoder.encode(password)); // Encode the password
        newJobseeker.setFirstName(firstName);
        newJobseeker.setLastName(lastName);
        // createdAt and updatedAt will be set automatically by Hibernate/JPA auditing

        return jobseekerRepository.save(newJobseeker);
    }

    /**
     * Registers a new jobseeker using details from a registration request DTO.
     *
     * @param request The DTO containing jobseeker registration details.
     * @return The created and saved Jobseeker entity.
     * @throws EmailAlreadyExistsException if the email is already registered.
     */
    @Transactional
    public Jobseeker registerNewJobseeker(JobseekerRegistrationRequest request) {
        return registerNewJobseeker(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );
    }
}
