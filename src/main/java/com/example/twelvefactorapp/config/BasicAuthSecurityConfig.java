package com.example.twelvefactorapp.config;

import com.example.twelvefactorapp.security.jwt.JwtRequestFilter; // Added
// import com.example.twelvefactorapp.service.auth.JobseekerUserDetailsService; // Already correctly handled by Spring Boot if @Service
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Added for addFilterBefore

// import static org.springframework.security.config.Customizer.withDefaults; // Not using httpBasic anymore for /api/**

@Configuration
@EnableWebSecurity
public class BasicAuthSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter; // Added for injection

    public BasicAuthSecurityConfig(JwtRequestFilter jwtRequestFilter) { // Injected
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/auth/jobseeker/register", "/api/auth/jobseeker/login").permitAll()
                .requestMatchers("/api/auth/publisher/**").permitAll()
                .requestMatchers("/actuator/health", "/public/**").permitAll()
                // .requestMatchers("/admin/**").hasRole("ADMIN") // If admin is part of /api/**, it needs JWT or different auth
                .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable())
            // Add the JwtRequestFilter before the standard UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // The JwtRequestFilter will now process the token and set the Authentication
        // in SecurityContextHolder if the token is valid.
        // Subsequent security checks (like .anyRequest().authenticated()) will use this Authentication.

        return http.build();
    }

    // TODO: If an /admin/** path or other non-API paths exist and require different security
    // (e.g., form login, basic auth for different user types), a separate SecurityFilterChain
    // bean with a different @Order and different .securityMatcher() would be needed.
    // For example:
    // @Bean
    // @Order(2)
    // public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
    //     http.securityMatcher("/**") // Or specific paths like "/admin/**", "/ui/**"
    //          .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
    //          .formLogin(withDefaults());
    //     return http.build();
    // }
}
