package com.example.twelvefactorapp.security.jwt;

import com.example.twelvefactorapp.model.Jobseeker; // Assuming we might use Jobseeker directly
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // Correct import for modern JJWT
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // Spring's UserDetails
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct; // For @PostConstruct
import javax.crypto.SecretKey; // For SecretKey
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections; // Added for Collections.emptyList()

/*
Conceptual pom.xml dependencies for JJWT:
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version> <!-- Use latest version -->
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson -->
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
*/

@Component
public class JwtTokenProvider { // Changed from interface to class

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey jwtSecretKey;

    @PostConstruct
    public void init() {
        // TODO: Secure key management is crucial.
        byte[] secretBytes = jwtSecretString.getBytes();
        if (secretBytes.length < 32) { // HS256 needs at least 256 bits (32 bytes)
            logger.warn("JWT secret key from properties is too short (less than 32 bytes for HS256)! " +
                        "Using a dynamically generated, more secure key for this session. " +
                        "THIS IS NOT SUITABLE FOR PRODUCTION if keys are not consistent across restarts/instances.");
            this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a key suitable for HS256
        } else {
            this.jwtSecretKey = Keys.hmacShaKeyFor(secretBytes);
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return buildToken(userPrincipal.getUsername(), userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
    }

    public String generateToken(Jobseeker jobseeker) {
        List<String> roles = List.of("ROLE_JOBSEEKER"); // Example role
        return buildToken(jobseeker.getEmail(), roles);
    }

    private String buildToken(String subject, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            logger.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT token: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.warn("Invalid JWT token: {}", ex.getMessage());
        } catch (SignatureException ex) {
            logger.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) { // Covers null or empty token
            logger.warn("JWT token is null, empty or only whitespace: {}", ex.getMessage());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty or token is null: {}", ex.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        if (username == null) {
            return null;
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody();
        @SuppressWarnings("unchecked")
        List<String> rolesClaim = claims.get("roles", List.class);
        List<GrantedAuthority> authorities = rolesClaim != null ?
                rolesClaim.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()) :
                Collections.emptyList();

        UserDetails userDetails = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // TODO: Secure key management: avoid hardcoding or easily guessable keys in properties for production.
    //       Consider environment variables or secrets management services.
    // TODO: Refresh token strategy for longer-lived sessions without exposing long-lived access tokens.
    // TODO: More granular error handling/logging for different JWT exception types if needed for metrics/alerts.
}
