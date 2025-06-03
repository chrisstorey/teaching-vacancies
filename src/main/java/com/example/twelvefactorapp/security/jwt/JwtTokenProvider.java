package com.example.twelvefactorapp.security.jwt;

import com.example.twelvefactorapp.model.Jobseeker; // Assuming we might use Jobseeker directly
import org.springframework.security.core.Authentication; // For Spring Security context
import org.springframework.security.core.userdetails.UserDetails; // For Spring Security UserDetails

// This is a conceptual outline. A full implementation would require a JWT library like jjwt.
public interface JwtTokenProvider {

    /**
     * Generates a JWT token for the given authentication object.
     * The Authentication object typically holds UserDetails after successful authentication.
     *
     * @param authentication The Spring Security Authentication object.
     * @return A JWT string.
     */
    String generateToken(Authentication authentication);

    /**
     * Generates a JWT token directly from UserDetails.
     * Useful if AuthenticationManager is not used directly before token generation.
     *
     * @param userDetails The UserDetails object.
     * @return A JWT string.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a JWT token directly from a Jobseeker entity.
     * This might be used if custom claims related to the Jobseeker entity are needed.
     *
     * @param jobseeker The Jobseeker entity.
     * @return A JWT string.
     */
    String generateToken(Jobseeker jobseeker);


    /**
     * Validates the given JWT token.
     * Checks for signature, expiration, and other validation rules.
     *
     * @param token The JWT token string.
     * @return true if the token is valid, false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the username (or user ID) from the JWT token.
     *
     * @param token The JWT token string.
     * @return The username or user ID claim from the token.
     */
    String getUsernameFromToken(String token); // Or getUserIdFromToken(String token) -> UUID

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token The JWT token string.
     * @return The UUID user ID claim from the token.
     */
    // UUID getUserIdFromToken(String token); // Alternative to getUsernameFromToken

    /**
     * Constructs a Spring Security Authentication object from a validated JWT token.
     * This is typically used by a JWT authentication filter.
     *
     * @param token The JWT token string.
     * @return An Authentication object if the token is valid and represents a user, null otherwise.
     */
    Authentication getAuthentication(String token);

    // TODO: Implement full JWT generation (e.g., using jjwt library) including:
    //       - Secret key management (loading from application properties, ensuring it's strong).
    //       - Setting appropriate claims (subject, issuer, expiration, custom claims like roles or user ID).
    //       - Signing the token with a secure algorithm (e.g., HS256, HS512, or RS256).

    // TODO: Implement full JWT validation logic:
    //       - Parsing the token.
    //       - Verifying the signature against the secret key.
    //       - Checking for token expiration.
    //       - Handling various exceptions (e.g., ExpiredJwtException, MalformedJwtException, SignatureException).
}
