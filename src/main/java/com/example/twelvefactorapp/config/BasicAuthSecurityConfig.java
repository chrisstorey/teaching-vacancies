package com.example.twelvefactorapp.config;

import com.example.twelvefactorapp.service.auth.JobseekerUserDetailsService; // Assuming this is the path
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class BasicAuthSecurityConfig {

    // JobseekerUserDetailsService will be injected by Spring if it's a @Service
    // Alternatively, you can explicitly wire it here.
    // For this setup, we assume JobseekerUserDetailsService is available as a bean.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Removed InMemoryUserDetailsManager to allow JobseekerUserDetailsService to be primary.
    // If other UserDetailsServices are needed (e.g., for publishers, or the old basic auth users),
    // a more complex configuration would be required (e.g., multiple SecurityFilterChain beans,
    // or a composite UserDetailsService).

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1) // Define order if multiple SecurityFilterChains are present
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Apply this filter chain to /api/** paths
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/auth/jobseeker/register", "/api/auth/jobseeker/login").permitAll()
                .requestMatchers("/api/auth/publisher/**").permitAll() // Assuming publisher auth is also open for now
                .requestMatchers("/actuator/health", "/public/**").permitAll() // Keep existing public paths
                .requestMatchers("/admin/**").hasRole("ADMIN") // Example admin rule, might be from a different auth mechanism
                .anyRequest().authenticated() // All other /api/** requests need authentication
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Essential for JWT/stateless APIs
            )
            .csrf(csrf -> csrf.disable()); // Typically disabled for stateless APIs

        // TODO: Implement JWTRequestFilter to validate tokens and integrate with JwtTokenProvider
        // for securing other jobseeker endpoints (e.g., /api/jobseekers/profile/**).
        // This filter would be added to the chain, e.g., http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        // For now, other /api/** endpoints will likely fail or fall back to other auth mechanisms if any are present globally.
        // If only this filter chain matches /api/**, then .anyRequest().authenticated() without a JWT filter
        // means no actual authentication mechanism is specified for those other /api/** paths yet beyond this config.

        // If we intend for this to be the *only* security for /api paths and JWT is the goal,
        // then `httpBasic(withDefaults())` should be removed or replaced by the JWT filter.
        // For now, removing httpBasic as we transition to token-based for APIs.
        // If basic auth is still needed for other parts of the app (not /api/**), a separate SecurityFilterChain would handle it.

        return http.build();
    }

    // If you had other SecurityFilterChain beans for different path patterns (e.g., a UI with formLogin),
    // they would be defined here with different @Order values.
}
