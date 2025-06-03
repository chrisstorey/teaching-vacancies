package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.dto.request.StartApplicationRequest;
import com.example.twelvefactorapp.dto.request.UpdateCompletedStepsRequest; // Added
import com.example.twelvefactorapp.exception.ForbiddenAccessException; // Added
import com.example.twelvefactorapp.exception.ResourceNotFoundException; // Added
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.ApplicationStatus;
import com.example.twelvefactorapp.service.JobApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; // Added for PATCH

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch; // Already available via MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobApplicationService jobApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testVacancyId;
    private UUID testApplicationId; // Added
    private UUID mockJobseekerUuid;
    private StartApplicationRequest startRequest;
    private UpdateCompletedStepsRequest updateStepsRequest; // Added
    private JobApplication mockJobApplication;


    private final String MOCK_JOBSEEKER_UUID_STRING = "0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11";


    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        testApplicationId = UUID.randomUUID(); // Initialize
        mockJobseekerUuid = UUID.fromString(MOCK_JOBSEEKER_UUID_STRING);

        startRequest = new StartApplicationRequest(testVacancyId);
        updateStepsRequest = new UpdateCompletedStepsRequest("initial_steps,personal_details_completed");


        Jobseeker jobseeker = new Jobseeker();
        jobseeker.setId(mockJobseekerUuid);

        Vacancy vacancy = new Vacancy();
        vacancy.setId(testVacancyId);
        vacancy.setJobTitle("Integration Test Job");

        mockJobApplication = new JobApplication();
        mockJobApplication.setId(testApplicationId); // Use consistent ID
        mockJobApplication.setJobseeker(jobseeker);
        mockJobApplication.setVacancy(vacancy);
        mockJobApplication.setStatus(ApplicationStatus.DRAFT);
        mockJobApplication.setCompletedSteps("initial_steps");
        mockJobApplication.setCreatedAt(OffsetDateTime.now());
        mockJobApplication.setUpdatedAt(OffsetDateTime.now());
    }

    // --- Tests for POST /api/jobapplications/start ---
    @Test
    void testStartApplication_withoutAuth_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testStartApplication_withValidAuthAndRequest_returnsOk() throws Exception {
        // mockJobseekerUuid is already set from MOCK_JOBSEEKER_UUID_STRING
        when(jobApplicationService.startOrGetDraftApplication(eq(mockJobseekerUuid), eq(testVacancyId)))
                .thenReturn(mockJobApplication);

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockJobApplication.getId().toString())))
                .andExpect(jsonPath("$.vacancyId", is(testVacancyId.toString())))
                .andExpect(jsonPath("$.jobseekerId", is(mockJobseekerUuid.toString())))
                .andExpect(jsonPath("$.status", is(ApplicationStatus.DRAFT.name())));
    }

    @Test
    @WithMockUser(username = "another-uuid-string", roles = {"JOBSEEKER"})
    void testStartApplication_withDifferentValidAuthAndRequest_returnsOk() throws Exception {
        UUID specificJobseekerId = UUID.fromString("another-uuid-string");

        Jobseeker currentJobseeker = new Jobseeker(); // Create a new jobseeker for this specific mock
        currentJobseeker.setId(specificJobseekerId);
        mockJobApplication.setJobseeker(currentJobseeker); // Update mockJobApplication for this test case

        when(jobApplicationService.startOrGetDraftApplication(eq(specificJobseekerId), eq(testVacancyId)))
                .thenReturn(mockJobApplication);

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobseekerId", is(specificJobseekerId.toString())));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testStartApplication_whenServiceThrowsResourceNotFound_returnsNotFound() throws Exception {
        when(jobApplicationService.startOrGetDraftApplication(eq(mockJobseekerUuid), eq(testVacancyId)))
                .thenThrow(new ResourceNotFoundException("Vacancy not found"));

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Vacancy not found")));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testStartApplication_whenServiceThrowsIllegalState_returnsConflict() throws Exception {
        when(jobApplicationService.startOrGetDraftApplication(eq(mockJobseekerUuid), eq(testVacancyId)))
                .thenThrow(new IllegalStateException("Vacancy not open for applications"));

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Vacancy not open for applications")));
    }

    // --- Tests for PATCH /api/jobapplications/{applicationId}/completed-steps ---

    @Test
    void testUpdateCompletedSteps_withoutAuth_returnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStepsRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testUpdateCompletedSteps_withValidAuthAndRequest_returnsOk() throws Exception {
        mockJobApplication.setCompletedSteps(updateStepsRequest.getCompletedSteps()); // Simulate the update for response check

        when(jobApplicationService.updateCompletedSteps(eq(testApplicationId), eq(mockJobseekerUuid), eq(updateStepsRequest.getCompletedSteps())))
                .thenReturn(mockJobApplication);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStepsRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testApplicationId.toString())))
                .andExpect(jsonPath("$.jobseekerId", is(mockJobseekerUuid.toString())))
                .andExpect(jsonPath("$.completedSteps", is(updateStepsRequest.getCompletedSteps())));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testUpdateCompletedSteps_whenServiceThrowsResourceNotFound_returnsNotFound() throws Exception {
        when(jobApplicationService.updateCompletedSteps(eq(testApplicationId), eq(mockJobseekerUuid), any(String.class)))
                .thenThrow(new ResourceNotFoundException("JobApplication not found"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStepsRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("JobApplication not found")));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testUpdateCompletedSteps_whenServiceThrowsForbiddenAccess_returnsForbidden() throws Exception {
        when(jobApplicationService.updateCompletedSteps(eq(testApplicationId), eq(mockJobseekerUuid), any(String.class)))
                .thenThrow(new ForbiddenAccessException("Not authorized to update this application"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStepsRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Not authorized to update this application")));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testUpdateCompletedSteps_whenServiceThrowsIllegalState_returnsConflict() throws Exception {
        when(jobApplicationService.updateCompletedSteps(eq(testApplicationId), eq(mockJobseekerUuid), any(String.class)))
                .thenThrow(new IllegalStateException("Application can only be updated if in DRAFT status"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStepsRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Application can only be updated if in DRAFT status")));
    }

    @Test
    @WithMockUser(username = MOCK_JOBSEEKER_UUID_STRING, roles = {"JOBSEEKER"})
    void testUpdateCompletedSteps_withInvalidRequestBody_returnsBadRequest() throws Exception {
        // To make this test meaningful, UpdateCompletedStepsRequest would need a validation that can fail.
        // E.g., if completedSteps had @NotBlank and we sent an empty string.
        // Current @Size(max=2000) is less likely to be violated easily here.
        // For demonstration, let's assume we could send a totally malformed JSON or violate a future constraint.
        // This test primarily ensures that @Valid is active on the controller method (implicitly).
        // If UpdateCompletedStepsRequest.completedSteps was @NotBlank:
        // UpdateCompletedStepsRequest invalidRequest = new UpdateCompletedStepsRequest("");
        // String requestBody = objectMapper.writeValueAsString(invalidRequest);

        // For now, sending a structurally different JSON to trigger a general 400 from Spring's deserialization
        String malformedJsonRequestBody = "{\"unrelatedField\":\"someValue\"}"; // Missing 'completedSteps' if it were required

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobapplications/{applicationId}/completed-steps", testApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJsonRequestBody)) // Or use invalidRequest with a failing constraint
                .andExpect(status().isBadRequest());
    }
}
