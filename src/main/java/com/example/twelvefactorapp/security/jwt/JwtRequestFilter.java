package com.example.twelvefactorapp.security.jwt;

import com.example.twelvefactorapp.service.auth.JobseekerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService; // Import UserDetailsService
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService; // Use the generic interface, inject specific impl

    @Autowired
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider,
                            @Qualifier("jobseekerUserDetailsService") UserDetailsService userDetailsService) {
        // Using @Qualifier to specify which UserDetailsService if multiple exist.
        // If only JobseekerUserDetailsService is defined as a @Service, @Qualifier might not be strictly necessary.
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null) {
                if (jwtTokenProvider.validateToken(jwt)) {
                    // Token is valid, proceed to get Authentication object
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt, userDetailsService);

                    if (authentication != null) {
                        // Set the authentication in the Spring Security Context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Successfully authenticated user '{}' via JWT and set SecurityContext.", authentication.getName());
                    } else {
                        // This case might occur if token is valid but getAuthentication had an issue
                        // (e.g., user details not found via UserDetailsService from token subject).
                        // JwtTokenProvider.getAuthentication should ideally handle UsernameNotFoundException from UserDetailsService.
                        logger.warn("JWT token was valid, but failed to establish Authentication object. URI: {}", request.getRequestURI());
                        // SecurityContextHolder.clearContext(); // Optional: ensure context is cleared if auth object is null
                    }
                } else {
                    // Token was present but invalid (validateToken returned false)
                    // JwtTokenProvider.validateToken logs specific reasons (expired, malformed, etc.)
                    logger.warn("Invalid JWT token received. URI: {}", request.getRequestURI());
                    // SecurityContextHolder.clearContext(); // Ensure context is cleared for invalid token
                }
            } else {
                logger.trace("No JWT token found in 'Authorization' header. URI: {}", request.getRequestURI());
                // No token found, processing will continue. If the endpoint is secured,
                // subsequent filters or security mechanisms will deny access if SecurityContext is empty.
            }
        } catch (Exception e) {
            // This catch block is for unexpected errors during token processing.
            logger.error("Exception during JWT authentication filter processing: {}", e.getMessage(), e);
            // SecurityContextHolder.clearContext(); // Clear context on any unexpected error
        }

        filterChain.doFilter(request, response); // Always continue the filter chain
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract token part after "Bearer "
        }
        logger.trace("No 'Bearer ' token found in Authorization header.");
        return null;
    }
}
