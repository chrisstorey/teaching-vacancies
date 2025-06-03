package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.dto.JobApplicationDto;
import com.example.twelvefactorapp.dto.request.StartApplicationRequest;
import com.example.twelvefactorapp.dto.request.UpdateCompletedStepsRequest; // Added
import com.example.twelvefactorapp.exception.ForbiddenAccessException; // Added
import com.example.twelvefactorapp.exception.ResourceNotFoundException;
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.service.JobApplicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    // Helper method to extract Jobseeker ID from Authentication principal
    private UUID getJobseekerIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Authentication is null or not authenticated.");
            throw new ForbiddenAccessException("User not authenticated."); // Or a more specific AuthenticationRequiredException
        }

        Object principal = authentication.getPrincipal();
        // TODO: Refine principal to Jobseeker object/ID mapping, possibly in a custom AuthenticationPrincipal resolver
        //       or by ensuring UserDetails loaded by JobseekerUserDetailsService contains the UUID directly.
        if (principal instanceof UserDetails) {
            String principalName = ((UserDetails) principal).getUsername();
            try {
                return UUID.fromString(principalName);
            } catch (IllegalArgumentException e) {
                logger.error("Principal name '{}' from UserDetails is not a valid UUID.", principalName, e);
                throw new IllegalArgumentException("User ID in token is malformed."); // This indicates an issue with token content
            }
        } else if (principal instanceof String) {
             try {
                return UUID.fromString((String) principal);
            } catch (IllegalArgumentException e) {
                logger.error("Principal string '{}' is not a valid UUID.", principal, e);
                throw new IllegalArgumentException("User ID in token is malformed.");
            }
        } else {
            logger.error("Unexpected principal type: {}. Cannot extract Jobseeker ID.", principal.getClass().getName());
            throw new IllegalArgumentException("Cannot determine user ID from principal.");
        }
    }


    @PostMapping("/start")
    public ResponseEntity<JobApplicationDto> startOrGetDraftApplication(
            @Valid @RequestBody StartApplicationRequest request,
            Authentication authentication) {

        UUID jobseekerId;
        try {
            jobseekerId = getJobseekerIdFromAuthentication(authentication);
        } catch (IllegalArgumentException | ForbiddenAccessException e) {
             // IllegalArgumentException from malformed UUID, Forbidden from not authenticated
            logger.error("Error extracting jobseekerId: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Or specific error DTO
        }

        logger.info("Jobseeker ID {} attempting to start/get draft application for vacancy ID {}", jobseekerId, request.getVacancyId());
        JobApplication jobApplication = jobApplicationService.startOrGetDraftApplication(jobseekerId, request.getVacancyId());
        return ResponseEntity.ok(JobApplicationDto.fromEntity(jobApplication));
    }

    @PatchMapping("/{applicationId}/completed-steps")
    public ResponseEntity<JobApplicationDto> updateCompletedSteps(
            @PathVariable UUID applicationId,
            @Valid @RequestBody UpdateCompletedStepsRequest request,
            Authentication authentication) {

        UUID jobseekerId;
        try {
            jobseekerId = getJobseekerIdFromAuthentication(authentication);
        } catch (IllegalArgumentException | ForbiddenAccessException e) {
            logger.error("Error extracting jobseekerId for update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        logger.info("Jobseeker ID {} attempting to update completed_steps for application ID {}", jobseekerId, applicationId);
        JobApplication updatedApplication = jobApplicationService.updateCompletedSteps(
                applicationId,
                jobseekerId,
                request.getCompletedSteps()
        );
        return ResponseEntity.ok(JobApplicationDto.fromEntity(updatedApplication));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        logger.warn("Illegal state encountered: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenAccessException.class) // Added
    public ResponseEntity<Map<String, String>> handleForbiddenAccessException(ForbiddenAccessException ex) {
        logger.warn("Forbidden access attempt: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class) // Added for malformed UUID in principal
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // This can catch issues from UUID.fromString() if the principal name is not a valid UUID
        logger.error("Illegal argument in request processing: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // Or INTERNAL_SERVER_ERROR if it's truly an internal token issue
                .body(Map.of("error", "Invalid request data or malformed user identifier."));
    }

    // MethodArgumentNotValidException handler would also be useful here if not global.
}
