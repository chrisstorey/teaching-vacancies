package com.example.twelvefactorapp.controller.auth;

import com.example.twelvefactorapp.dto.JobseekerDto;
import com.example.twelvefactorapp.dto.JobseekerRegistrationRequest;
import com.example.twelvefactorapp.dto.auth.JobseekerLoginRequest; // Added
import com.example.twelvefactorapp.dto.auth.JobseekerLoginResponse; // Added
import com.example.twelvefactorapp.exception.EmailAlreadyExistsException;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.service.JobseekerRegistrationService;
import com.example.twelvefactorapp.service.auth.JobseekerAuthenticationService; // Added
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException; // Added
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/jobseeker")
public class JobseekerAuthController {

    private final JobseekerRegistrationService jobseekerRegistrationService;
    private final JobseekerAuthenticationService jobseekerAuthenticationService; // Added

    public JobseekerAuthController(
            JobseekerRegistrationService jobseekerRegistrationService,
            JobseekerAuthenticationService jobseekerAuthenticationService) { // Added service to constructor
        this.jobseekerRegistrationService = jobseekerRegistrationService;
        this.jobseekerAuthenticationService = jobseekerAuthenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<JobseekerDto> registerJobseeker(@Valid @RequestBody JobseekerRegistrationRequest registrationRequest) {
        Jobseeker newJobseeker = jobseekerRegistrationService.registerNewJobseeker(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(JobseekerDto.fromEntity(newJobseeker));
    }

    @PostMapping("/login")
    public ResponseEntity<JobseekerLoginResponse> loginJobseeker(@Valid @RequestBody JobseekerLoginRequest loginRequest) {
        JobseekerLoginResponse loginResponse = jobseekerAuthenticationService.loginJobseeker(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
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

    @ExceptionHandler(AuthenticationException.class) // Handles BadCredentialsException and others
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        // Log the exception for server-side review if needed, but don't expose detailed internal errors to client.
        // logger.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password")); // Generic message for security
    }
}
