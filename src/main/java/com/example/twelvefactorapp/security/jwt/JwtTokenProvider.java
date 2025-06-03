package com.example.twelvefactorapp.security.jwt;

// JJWT Library imports
import com.example.twelvefactorapp.model.Jobseeker; // Example if generating token from Jobseeker
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders; // For Base64 decoding if secret is Base64 encoded
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // JJWT's SignatureException

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // For getAuthentication method
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
pom.xml dependencies for JJWT:
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version> <!-- Use latest stable version -->
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson for JSON processing -->
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
*/

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // TODO: For production, the JWT secret key MUST be strong, securely managed, and not exposed.
        //       Consider using environment variables, a secrets management service (like HashiCorp Vault, AWS Secrets Manager),
        //       or ensure the value in application properties is injected securely.
        //       A good practice is to generate a cryptographically strong key (e.g., Base64 encoded, >= 256 bits for HS256).
        //       Example generation: `java -cp jjwt-impl.jar io.jsonwebtoken.impl.crypto.MacProvider HmacSHA256 -b 64 | base64` (adjust algorithm)

        if (jwtSecretString == null || jwtSecretString.isBlank()) {
            logger.error("FATAL: JWT secret key ('app.jwt.secret') is not configured in application properties.");
            logger.error("Application will not be able to issue or validate JWTs securely.");
            logger.error("Please provide a strong, secure secret key.");
            // In a production environment, you might want to prevent application startup:
            // throw new IllegalStateException("JWT secret key is not configured.");
            // For demonstration/dev, we'll generate a temporary key, but this is INSECURE for prod.
            logger.warn("DEVELOPMENT ONLY: Generating a temporary, insecure JWT secret key as none was configured.");
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            return;
        }

        byte[] keyBytes;
        try {
            // Attempt to decode as Base64 first, as this is a common way to store binary keys as strings.
            keyBytes = Decoders.BASE64.decode(jwtSecretString);
        } catch (IllegalArgumentException e) {
            // If not Base64, assume it's a raw string secret (less ideal for binary keys but common).
            logger.warn("JWT secret key is not Base64 encoded, using raw string bytes. Ensure it's sufficiently strong.");
            keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        }

        // HS512 requires a key of at least 512 bits (64 bytes).
        // HS256 requires at least 256 bits (32 bytes).
        // Ensure your key length matches the algorithm you choose.
        if (keyBytes.length < 64 && SignatureAlgorithm.HS512.isHmac()) { // Example check for HS512
             logger.warn("Configured JWT secret key length ({}) is less than the recommended 64 bytes for HS512. " +
                         "Consider using a stronger key or a different algorithm if appropriate.", keyBytes.length);
        }
         if (keyBytes.length < 32 && SignatureAlgorithm.HS256.isHmac()) { // Example check for HS256
             logger.warn("Configured JWT secret key length ({}) is less than the recommended 32 bytes for HS256. ", keyBytes.length);
        }


        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return buildToken(userPrincipal.getUsername(), roles);
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return buildToken(userDetails.getUsername(), roles);
    }

    public String generateToken(Jobseeker jobseeker) {
        // Example: Assign a default role or derive from jobseeker entity if it has roles
        List<String> roles = Collections.singletonList("ROLE_JOBSEEKER");
        return buildToken(jobseeker.getEmail(), roles); // Using email as subject
    }

    private String buildToken(String subject, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles); // Custom claim for roles
        // TODO: Add other claims if needed (e.g., user ID, name)
        // claims.put("userId", ...);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512) // Or HS256, ensure key matches
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) { // Includes token being null or empty
            logger.error("JWT token is invalid: {}", ex.getMessage());
        }
        // TODO: Consider rethrowing a custom application-specific AuthenticationException for filter handling.
        return null;
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.warn("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) { // Handles claims string being empty
            logger.warn("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token, UserDetailsService userDetailsService) {
        String username = getUsernameFromToken(token);
        if (username == null) {
            return null; // Token invalid or username not parsable
        }

        // Load fresh UserDetails to ensure account status and roles are up-to-date.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // If you store roles directly in the token and trust them (after signature validation),
        // you could parse them from claims instead of UserDetails for minor optimization,
        // but UserDetailsService ensures data freshness.
        // Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        // List<String> rolesFromToken = claims.get("roles", List.class);
        // List<GrantedAuthority> authorities = rolesFromToken != null ?
        // rolesFromToken.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()) :
        // Collections.emptyList();

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // TODO: Consider refresh token strategy for longer-lived sessions.
    // TODO: More sophisticated error handling for different JWT exceptions if specific client responses are needed.
}
