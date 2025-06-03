package com.example.twelvefactorapp.security.jwt;

import com.example.twelvefactorapp.service.auth.JobseekerUserDetailsService; // Or a generic UserDetailsService
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    // UserDetailsService can be used by jwtTokenProvider.getAuthentication if it needs to load fresh UserDetails.
    // Or, if getAuthentication constructs UserDetails directly from token claims, this might not be strictly needed here.
    // For this conceptual outline, let's assume JwtTokenProvider's getAuthentication might use it or has its own way.
    private final JobseekerUserDetailsService jobseekerUserDetailsService;

    @Autowired // Or constructor injection
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider,
                            JobseekerUserDetailsService jobseekerUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jobseekerUserDetailsService = jobseekerUserDetailsService; // May or may not be used directly by this filter
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                // If token is valid, try to get Authentication object from it
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

                if (authentication != null) {
                    // Set the authentication in the Spring Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Set Authentication in SecurityContextHolder for user: {}", authentication.getName());
                } else {
                    // This case might occur if token is valid but getAuthentication fails (e.g. user not found from token sub)
                    logger.warn("JWT token is valid, but failed to get Authentication object. URI: {}", request.getRequestURI());
                }
            } else {
                if (jwt != null) {
                    // Token was present but invalid (validateToken returned false)
                    // TODO: More detailed error logging if token validation fails but an attempt was made
                    // (e.g., token expired vs. invalid signature if validateToken only returns boolean).
                    // JwtTokenProvider.validateToken itself logs errors, but this filter could add context.
                    logger.warn("JWT token validation failed. URI: {}", request.getRequestURI());
                }
                // if jwt is null, it means no token was found in the header, which is normal for public endpoints.
            }
        } catch (Exception e) {
            // This catch block is for unexpected errors during token processing.
            // Specific JWT exceptions should ideally be handled within JwtTokenProvider.
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response); // Always continue the filter chain
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract token part after "Bearer "
        }
        return null;
    }
}
