package com.example.twelvefactorapp.service.auth;

import com.example.twelvefactorapp.dto.auth.JobseekerLoginRequest;
import com.example.twelvefactorapp.dto.auth.JobseekerLoginResponse;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.repository.JobseekerRepository;
import com.example.twelvefactorapp.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // For manual check if not using AuthManager fully
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder; // To get Authentication object after manager.authenticate
import org.springframework.security.core.userdetails.UserDetails; // For manual check
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobseekerAuthenticationService {

    private final JobseekerRepository jobseekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager; // Optional, but recommended

    public JobseekerAuthenticationService(JobseekerRepository jobseekerRepository,
                                          PasswordEncoder passwordEncoder,
                                          JwtTokenProvider jwtTokenProvider,
                                          AuthenticationManager authenticationManager) {
        this.jobseekerRepository = jobseekerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a jobseeker and generates a JWT upon successful authentication.
     *
     * @param loginRequest DTO containing login credentials (email and password).
     * @return JobseekerLoginResponse DTO containing the JWT and jobseeker details.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    public JobseekerLoginResponse loginJobseeker(JobseekerLoginRequest loginRequest) {
        // Step 1: Authenticate using Spring Security's AuthenticationManager
        // This will use the configured UserDetailsService (JobseekerUserDetailsService)
        // and PasswordEncoder to verify the credentials.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // If authentication is successful, the Authentication object will be populated.
        // We can set it in the security context if this were a stateful session, but for JWT,
        // we primarily need it to get the principal (UserDetails) for token generation.
        SecurityContextHolder.getContext().setAuthentication(authentication); // Optional for JWT, but good practice

        // Step 2: Get the authenticated Jobseeker details.
        // The principal from the Authentication object should be our UserDetails instance.
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString();
        }

        // Fetch the full Jobseeker entity to get all necessary details for the response.
        // The username from UserDetails is the email in our case.
        Jobseeker jobseeker = jobseekerRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated jobseeker not found in repository, which should not happen."));
                // This exception indicates a data consistency issue if authentication passed but user is gone.

        // Step 3: Generate JWT
        // We can pass the Authentication object or the UserDetails or the Jobseeker entity itself to the token provider.
        // Using Authentication object is common as it's readily available.
        String token = jwtTokenProvider.generateToken(authentication);
        // Alternatively, if generateToken takes UserDetails:
        // String token = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        // Or if it takes Jobseeker entity:
        // String token = jwtTokenProvider.generateToken(jobseeker);


        // Step 4: Construct and return the response
        return new JobseekerLoginResponse(
                token,
                jobseeker.getId(),
                jobseeker.getEmail(),
                jobseeker.getFirstName(),
                jobseeker.getLastName()
        );
    }

    /**
     * Alternative login logic if AuthenticationManager is not used directly for some reason
     * (e.g., in a system without full Spring Security filter chain for this specific endpoint).
     * This is generally less recommended if Spring Security is available.
     */
    public JobseekerLoginResponse loginJobseekerManually(JobseekerLoginRequest loginRequest) {
        Optional<Jobseeker> jobseekerOptional = jobseekerRepository.findByEmail(loginRequest.getEmail());
        if (jobseekerOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        Jobseeker jobseeker = jobseekerOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), jobseeker.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        // At this point, authentication is successful.
        // Manually create UserDetails if your JwtTokenProvider expects it.
        // Or pass the Jobseeker entity directly if your JwtTokenProvider supports it.
        String token = jwtTokenProvider.generateToken(jobseeker); // Assuming generateToken(Jobseeker) exists

        return new JobseekerLoginResponse(
                token,
                jobseeker.getId(),
                jobseeker.getEmail(),
                jobseeker.getFirstName(),
                jobseeker.getLastName()
        );
    }
}
