package com.example.twelvefactorapp.service.auth;

import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.repository.JobseekerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("jobseekerUserDetailsService") // Naming the bean specifically
public class JobseekerUserDetailsService implements UserDetailsService {

    private final JobseekerRepository jobseekerRepository;

    public JobseekerUserDetailsService(JobseekerRepository jobseekerRepository) {
        this.jobseekerRepository = jobseekerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Jobseeker jobseeker = jobseekerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Jobseeker not found with email: " + email));

        // For simplicity, all jobseekers get "ROLE_JOBSEEKER".
        // More complex role/permission logic could be added here if needed.
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_JOBSEEKER"));

        return new User(
                jobseeker.getEmail(),        // Username for Spring Security context
                jobseeker.getPassword(),     // Hashed password from the database
                authorities                  // Granted authorities (roles)
        );
        // Additional UserDetails properties (accountNonExpired, accountNonLocked, credentialsNonExpired, enabled)
        // can be set here if the Jobseeker entity has corresponding fields.
        // For now, using the basic User constructor which assumes true for these.
        /*
        return new org.springframework.security.core.userdetails.User(
            jobseeker.getEmail(),
            jobseeker.getPassword(),
            jobseeker.isEnabled(), // Assuming Jobseeker has an isEnabled field
            jobseeker.isAccountNonExpired(), // Assuming...
            jobseeker.isCredentialsNonExpired(), // Assuming...
            jobseeker.isAccountNonLocked(), // Assuming...
            authorities
        );
        */
    }
}
