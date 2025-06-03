package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.dto.JobApplicationDto;
import com.example.twelvefactorapp.dto.request.StartApplicationRequest;
import com.example.twelvefactorapp.exception.ResourceNotFoundException; // For @ExceptionHandler test
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.ApplicationStatus;
import com.example.twelvefactorapp.service.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User; // For mocking UserDetails principal

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobApplicationControllerTest {

    @Mock
    private JobApplicationService jobApplicationService;

    @Mock
    private Authentication authentication; // Mock Authentication object

    @InjectMocks
    private JobApplicationController jobApplicationController;

    private UUID testVacancyId;
    private UUID testJobseekerId;
    private StartApplicationRequest startRequest;
    private JobApplication mockJobApplication;

    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        testJobseekerId = UUID.randomUUID();

        startRequest = new StartApplicationRequest(testVacancyId);

        // Mock Jobseeker and Vacancy for JobApplication
        Jobseeker jobseeker = new Jobseeker();
        jobseeker.setId(testJobseekerId);

        Vacancy vacancy = new Vacancy();
        vacancy.setId(testVacancyId);
        vacancy.setJobTitle("Test Job");

        mockJobApplication = new JobApplication();
        mockJobApplication.setId(UUID.randomUUID());
        mockJobApplication.setJobseeker(jobseeker);
        mockJobApplication.setVacancy(vacancy);
        mockJobApplication.setStatus(ApplicationStatus.DRAFT);
        mockJobApplication.setCreatedAt(OffsetDateTime.now());
        mockJobApplication.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void testStartOrGetDraftApplication_withValidAuthAndUserDetails_extractsJobseekerIdAndCallsService() {
        // Arrange
        User mockUserDetails = new User(testJobseekerId.toString(), "password", Collections.emptyList());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jobApplicationService.startOrGetDraftApplication(testJobseekerId, testVacancyId))
                .thenReturn(mockJobApplication);

        // Act
        ResponseEntity<JobApplicationDto> response = jobApplicationController.startOrGetDraftApplication(startRequest, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockJobApplication.getId(), response.getBody().getId());
        assertEquals(testVacancyId, response.getBody().getVacancyId());
        assertEquals(testJobseekerId, response.getBody().getJobseekerId());
        assertEquals(ApplicationStatus.DRAFT.name(), response.getBody().getStatus());

        verify(jobApplicationService).startOrGetDraftApplication(testJobseekerId, testVacancyId);
    }

    @Test
    void testStartOrGetDraftApplication_withValidAuthAndStringPrincipal_extractsJobseekerIdAndCallsService() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(testJobseekerId.toString()); // Principal is just the UUID String
        when(jobApplicationService.startOrGetDraftApplication(testJobseekerId, testVacancyId))
                .thenReturn(mockJobApplication);

        // Act
        ResponseEntity<JobApplicationDto> response = jobApplicationController.startOrGetDraftApplication(startRequest, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // ... other assertions as above ...
        verify(jobApplicationService).startOrGetDraftApplication(testJobseekerId, testVacancyId);
    }


    @Test
    void testStartOrGetDraftApplication_withInvalidAuthNameFormat_returnsInternalServerError() {
        // Arrange
        User mockUserDetails = new User("not-a-valid-uuid", "password", Collections.emptyList());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        // No need to mock jobApplicationService as it shouldn't be called

        // Act
        ResponseEntity<JobApplicationDto> response = jobApplicationController.startOrGetDraftApplication(startRequest, authentication);

        // Assert
        assertNotNull(response);
        // The controller currently returns INTERNAL_SERVER_ERROR for this specific case.
        // Depending on desired behavior, this could be BAD_REQUEST or UNAUTHORIZED as well.
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody()); // Or an error DTO
    }

    @Test
    void testStartOrGetDraftApplication_withUnauthenticated_returnsUnauthorized() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);
        // No need to mock jobApplicationService

        // Act
        ResponseEntity<JobApplicationDto> response = jobApplicationController.startOrGetDraftApplication(startRequest, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testStartOrGetDraftApplication_withNullAuthentication_returnsUnauthorized() {
        // Arrange
        // Authentication object is null
        // No need to mock jobApplicationService

        // Act
        ResponseEntity<JobApplicationDto> response = jobApplicationController.startOrGetDraftApplication(startRequest, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test resource not found");
        ResponseEntity<Map<String, String>> response = jobApplicationController.handleResourceNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Test resource not found", response.getBody().get("error"));
    }

    @Test
    void testHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Test illegal state");
        ResponseEntity<Map<String, String>> response = jobApplicationController.handleIllegalStateException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode()); // As per controller's handler
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Test illegal state", response.getBody().get("error"));
    }
}
