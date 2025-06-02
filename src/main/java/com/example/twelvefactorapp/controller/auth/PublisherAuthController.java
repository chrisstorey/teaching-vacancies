package com.example.twelvefactorapp.controller.auth;

import com.example.twelvefactorapp.dto.PublisherDto;
import com.example.twelvefactorapp.dto.PublisherRegistrationRequest;
import com.example.twelvefactorapp.exception.EmailAlreadyExistsException; // Reusing
import com.example.twelvefactorapp.model.Publisher;
import com.example.twelvefactorapp.service.PublisherRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/publisher")
public class PublisherAuthController {

    private final PublisherRegistrationService publisherRegistrationService;

    public PublisherAuthController(PublisherRegistrationService publisherRegistrationService) {
        this.publisherRegistrationService = publisherRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<PublisherDto> registerPublisher(@Valid @RequestBody PublisherRegistrationRequest registrationRequest) {
        Publisher newPublisher = publisherRegistrationService.registerNewPublisher(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(PublisherDto.fromEntity(newPublisher));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Using CONFLICT (409)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingValue, newValue) -> existingValue + "; " + newValue
                ));
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Validation failed");
        responseBody.put("errors", errors);
        
        return ResponseEntity.badRequest().body(responseBody);
    }
}
