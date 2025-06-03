package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.dto.request.StartApplicationRequest;
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

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private UUID mockJobseekerUuid;
    private StartApplicationRequest startRequest;
    private JobApplication mockJobApplication;

    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        mockJobseekerUuid = UUID.randomUUID(); // This UUID string will be used as the username in @WithMockUser

        startRequest = new StartApplicationRequest(testVacancyId);

        Jobseeker jobseeker = new Jobseeker();
        jobseeker.setId(mockJobseekerUuid);

        Vacancy vacancy = new Vacancy();
        vacancy.setId(testVacancyId);
        vacancy.setJobTitle("Integration Test Job");

        mockJobApplication = new JobApplication();
        mockJobApplication.setId(UUID.randomUUID());
        mockJobApplication.setJobseeker(jobseeker);
        mockJobApplication.setVacancy(vacancy);
        mockJobApplication.setStatus(ApplicationStatus.DRAFT);
        mockJobApplication.setCreatedAt(OffsetDateTime.now());
        mockJobApplication.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void testStartApplication_withoutAuth_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isUnauthorized()); // Or 403 if default for missing auth on secured endpoint
    }

    @Test
    @WithMockUser(username = "0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", roles = {"JOBSEEKER"}) // Example UUID String
    void testStartApplication_withValidAuthAndRequest_returnsOk() throws Exception {
        UUID jobseekerIdFromMockUser = UUID.fromString("0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

        // Update mockJobApplication to use the jobseekerIdFromMockUser for its jobseeker
        Jobseeker currentJobseeker = new Jobseeker();
        currentJobseeker.setId(jobseekerIdFromMockUser);
        mockJobApplication.setJobseeker(currentJobseeker);


        when(jobApplicationService.startOrGetDraftApplication(eq(jobseekerIdFromMockUser), eq(testVacancyId)))
                .thenReturn(mockJobApplication);

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockJobApplication.getId().toString())))
                .andExpect(jsonPath("$.vacancyId", is(testVacancyId.toString())))
                .andExpect(jsonPath("$.jobseekerId", is(jobseekerIdFromMockUser.toString())))
                .andExpect(jsonPath("$.status", is(ApplicationStatus.DRAFT.name())));
    }

    @Test
    @WithMockUser(username = "another-uuid-string", roles = {"JOBSEEKER"})
    // This test is to ensure the @WithMockUser username is correctly picked up if it's different from the one in setUp
    void testStartApplication_withDifferentValidAuthAndRequest_returnsOk() throws Exception {
        UUID specificJobseekerId = UUID.fromString("another-uuid-string");

        Jobseeker currentJobseeker = new Jobseeker();
        currentJobseeker.setId(specificJobseekerId);
        mockJobApplication.setJobseeker(currentJobseeker); // Ensure DTO reflects this ID

        when(jobApplicationService.startOrGetDraftApplication(eq(specificJobseekerId), eq(testVacancyId)))
                .thenReturn(mockJobApplication);

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobseekerId", is(specificJobseekerId.toString())));
    }


    // Test for ResourceNotFoundException (e.g., Vacancy not found)
    @Test
    @WithMockUser(username = "0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", roles = {"JOBSEEKER"})
    void testStartApplication_whenServiceThrowsResourceNotFound_returnsNotFound() throws Exception {
        UUID jobseekerIdFromMockUser = UUID.fromString("0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        when(jobApplicationService.startOrGetDraftApplication(eq(jobseekerIdFromMockUser), eq(testVacancyId)))
                .thenThrow(new ResourceNotFoundException("Vacancy not found"));

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Vacancy not found")));
    }

    // Test for IllegalStateException (e.g., Vacancy not open)
    @Test
    @WithMockUser(username = "0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", roles = {"JOBSEEKER"})
    void testStartApplication_whenServiceThrowsIllegalState_returnsConflict() throws Exception {
        UUID jobseekerIdFromMockUser = UUID.fromString("0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        when(jobApplicationService.startOrGetDraftApplication(eq(jobseekerIdFromMockUser), eq(testVacancyId)))
                .thenThrow(new IllegalStateException("Vacancy not open for applications"));

        mockMvc.perform(post("/api/jobapplications/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andExpect(status().isConflict()) // As per @ExceptionHandler in controller
                .andExpect(jsonPath("$.error", is("Vacancy not open for applications")));
    }
}
