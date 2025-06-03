package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.dto.JobApplicationDto;
import com.example.twelvefactorapp.dto.request.StartApplicationRequest;
import com.example.twelvefactorapp.exception.ResourceNotFoundException;
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.service.JobApplicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; // For a more robust principal extraction
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobapplications")
public class JobApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(JobApplicationController.class);
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/start")
    public ResponseEntity<JobApplicationDto> startOrGetDraftApplication(
            @Valid @RequestBody StartApplicationRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Attempt to start application without authentication.");
            // This case should ideally be caught by Spring Security if endpoint is secured
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(null); // Or an error DTO
        }

        UUID jobseekerId;
        Object principal = authentication.getPrincipal();

        // TODO: Refine principal to Jobseeker object/ID mapping, possibly in a custom AuthenticationPrincipal resolver
        //       or by ensuring UserDetails loaded by JobseekerUserDetailsService contains the UUID directly or is the Jobseeker entity itself.
        //       For now, assuming the name (username) in UserDetails IS the Jobseeker's UUID as a string for simplicity.
        //       This is a common simplification if UserDetailsService stores UUID as username.
        if (principal instanceof UserDetails) {
            String principalName = ((UserDetails) principal).getUsername();
            try {
                jobseekerId = UUID.fromString(principalName);
            } catch (IllegalArgumentException e) {
                logger.error("Principal name '{}' is not a valid UUID. Cannot process application start. Authentication details: {}", principalName, authentication);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Or BAD_REQUEST if client could control this
                                     .body(null); // Or an error DTO indicating malformed user ID in token
            }
        } else if (principal instanceof String) { // Fallback if principal is just a String (less common with UserDetails)
             try {
                jobseekerId = UUID.fromString((String) principal);
            } catch (IllegalArgumentException e) {
                logger.error("Principal string '{}' is not a valid UUID. Cannot process application start. Authentication details: {}", principal, authentication);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        else {
            logger.error("Unexpected principal type: {}. Cannot extract Jobseeker ID. Authentication details: {}",
                         principal.getClass().getName(), authentication);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or UNAUTHORIZED
        }

        logger.info("Jobseeker ID {} attempting to start/get draft application for vacancy ID {}", jobseekerId, request.getVacancyId());

        JobApplication jobApplication = jobApplicationService.startOrGetDraftApplication(jobseekerId, request.getVacancyId());
        return ResponseEntity.ok(JobApplicationDto.fromEntity(jobApplication));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        // Could be BAD_REQUEST or CONFLICT depending on the specific IllegalStateException context
        // For "vacancy not open" or "application already submitted", CONFLICT (409) might be more appropriate.
        logger.warn("Illegal state encountered: {}", ex.getMessage()); // Log it as it might indicate business logic issues
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Or BAD_REQUEST
                .body(Map.of("error", ex.getMessage()));
    }

    // MethodArgumentNotValidException handler would also be useful here, similar to Auth controllers,
    // if not handled globally. For this subtask, focusing on service-thrown exceptions.
}
