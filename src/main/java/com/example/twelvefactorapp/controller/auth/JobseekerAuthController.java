package com.example.twelvefactorapp.controller.auth;

import com.example.twelvefactorapp.dto.JobseekerDto;
import com.example.twelvefactorapp.dto.JobseekerRegistrationRequest;
import com.example.twelvefactorapp.exception.EmailAlreadyExistsException;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.service.JobseekerRegistrationService;
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
@RequestMapping("/api/auth/jobseeker")
public class JobseekerAuthController {

    private final JobseekerRegistrationService jobseekerRegistrationService;

    public JobseekerAuthController(JobseekerRegistrationService jobseekerRegistrationService) {
        this.jobseekerRegistrationService = jobseekerRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<JobseekerDto> registerJobseeker(@Valid @RequestBody JobseekerRegistrationRequest registrationRequest) {
        Jobseeker newJobseeker = jobseekerRegistrationService.registerNewJobseeker(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(JobseekerDto.fromEntity(newJobseeker));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Using CONFLICT (409) as it's more specific for this case
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingValue, newValue) -> existingValue + "; " + newValue // In case of multiple errors for the same field
                ));
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Validation failed");
        responseBody.put("errors", errors);
        
        return ResponseEntity.badRequest().body(responseBody);
    }
}
