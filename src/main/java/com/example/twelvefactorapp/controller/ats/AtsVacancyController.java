package com.example.twelvefactorapp.controller.ats;

import com.example.twelvefactorapp.dto.AtsVacancyDto;
import com.example.twelvefactorapp.exception.UnauthorizedException;
import com.example.twelvefactorapp.model.PublisherAtsApiClient; // Added
import com.example.twelvefactorapp.repository.PublisherAtsApiClientRepository; // Added
import com.example.twelvefactorapp.service.AtsVacancyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/ats-api/v1/vacancies")
public class AtsVacancyController {

    private final AtsVacancyService atsVacancyService;
    private final PublisherAtsApiClientRepository apiClientRepository; // Added

    // Removed placeholder constants for API key validation

    public AtsVacancyController(AtsVacancyService atsVacancyService,
                                PublisherAtsApiClientRepository apiClientRepository) { // Added repository to constructor
        this.atsVacancyService = atsVacancyService;
        this.apiClientRepository = apiClientRepository;
    }

    private UUID getApiClientIdFromHeader(HttpServletRequest request) {
        String apiKey = request.getHeader("X-Api-Key");

        if (!StringUtils.hasText(apiKey)) {
            throw new UnauthorizedException("API key is missing"); // Updated message
        }

        // TODO: The API key validation logic is now database-backed.
        // For a production system with multiple ATS API controllers, this validation logic
        // (including API key parsing and database lookup) should ideally be moved to a
        // Spring HandlerInterceptor or a custom Spring Security Filter.
        // This would centralize authentication and keep controller methods cleaner.
        // Consider also:
        // - Caching API key lookups to reduce database load.
        // - Implementing more sophisticated key management (e.g., hashed keys, expiry, revocation).

        Optional<PublisherAtsApiClient> clientOptional = apiClientRepository.findByApiKey(apiKey);

        if (clientOptional.isEmpty()) {
            throw new UnauthorizedException("Invalid API key"); // Updated message
        }

        // Potentially add checks here if the client account is active, not suspended, etc.
        // PublisherAtsApiClient client = clientOptional.get();
        // if (!client.isActive()) {
        // throw new UnauthorizedException("API client account is not active");
        // }

        return clientOptional.get().getId();
    }

    @GetMapping
    public ResponseEntity<Page<AtsVacancyDto>> listClientVacancies(HttpServletRequest request, Pageable pageable) {
        UUID apiClientId = getApiClientIdFromHeader(request);
        Page<AtsVacancyDto> vacanciesPage = atsVacancyService.findVacanciesByApiClient(apiClientId, pageable);
        return ResponseEntity.ok(vacanciesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtsVacancyDto> getClientVacancyById(@PathVariable UUID id, HttpServletRequest request) {
        UUID apiClientId = getApiClientIdFromHeader(request);
        Optional<AtsVacancyDto> vacancyDtoOptional = atsVacancyService.findVacancyByIdAndApiClient(id, apiClientId);

        return vacancyDtoOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Collections.singletonMap("error", ex.getMessage()));
    }
}
