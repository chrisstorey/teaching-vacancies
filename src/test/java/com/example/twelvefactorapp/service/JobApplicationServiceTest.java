package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.exception.ForbiddenAccessException;
import com.example.twelvefactorapp.exception.ResourceNotFoundException;
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.ApplicationStatus;
import com.example.twelvefactorapp.model.enums.VacancyStatus;
import com.example.twelvefactorapp.repository.JobApplicationRepository;
import com.example.twelvefactorapp.repository.JobseekerRepository;
import com.example.twelvefactorapp.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobseekerRepository jobseekerRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    private UUID jobseekerId;
    private UUID vacancyId;
    private UUID applicationId;
    private Jobseeker jobseeker;
    private Vacancy vacancy;
    private JobApplication jobApplication;

    @BeforeEach
    void setUp() {
        jobseekerId = UUID.randomUUID();
        vacancyId = UUID.randomUUID();
        applicationId = UUID.randomUUID();

        jobseeker = new Jobseeker();
        jobseeker.setId(jobseekerId);

        vacancy = new Vacancy();
        vacancy.setId(vacancyId);
        vacancy.setStatus(VacancyStatus.PUBLISHED); // Default to open vacancy
        vacancy.setExpiresAt(OffsetDateTime.now().plusDays(30)); // Default to not expired

        jobApplication = new JobApplication();
        jobApplication.setId(applicationId);
        jobApplication.setJobseeker(jobseeker);
        jobApplication.setVacancy(vacancy);
        jobApplication.setStatus(ApplicationStatus.DRAFT);
        jobApplication.setCompletedSteps("step1,step2");
    }

    // Tests for startOrGetDraftApplication
    @Test
    void startOrGetDraftApplication_jobseekerNotFound_throwsResourceNotFound() {
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId));
    }

    @Test
    void startOrGetDraftApplication_vacancyNotFound_throwsResourceNotFound() {
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId));
    }

    @Test
    void startOrGetDraftApplication_vacancyExpired_throwsIllegalState() {
        vacancy.setExpiresAt(OffsetDateTime.now().minusDays(1));
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));
        assertThrows(IllegalStateException.class, () ->
                jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId));
    }

    @Test
    void startOrGetDraftApplication_vacancyNotPublished_throwsIllegalState() {
        vacancy.setStatus(VacancyStatus.DRAFT); // Any status other than PUBLISHED
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));
        assertThrows(IllegalStateException.class, () ->
                jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId));
    }

    @Test
    void startOrGetDraftApplication_existingDraft_returnsExisting() {
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));
        when(jobApplicationRepository.findByJobseekerIdAndVacancyId(jobseekerId, vacancyId))
                .thenReturn(Optional.of(jobApplication)); // Existing DRAFT application

        JobApplication result = jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId);
        assertEquals(jobApplication, result);
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void startOrGetDraftApplication_existingSubmitted_throwsIllegalState() {
        jobApplication.setStatus(ApplicationStatus.SUBMITTED);
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));
        when(jobApplicationRepository.findByJobseekerIdAndVacancyId(jobseekerId, vacancyId))
                .thenReturn(Optional.of(jobApplication));

        assertThrows(IllegalStateException.class, () ->
                jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId));
    }

    @Test
    void startOrGetDraftApplication_noExisting_createsAndReturnsNewDraft() {
        when(jobseekerRepository.findById(jobseekerId)).thenReturn(Optional.of(jobseeker));
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));
        when(jobApplicationRepository.findByJobseekerIdAndVacancyId(jobseekerId, vacancyId))
                .thenReturn(Optional.empty());
        when(jobApplicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = jobApplicationService.startOrGetDraftApplication(jobseekerId, vacancyId);

        assertNotNull(result);
        assertEquals(jobseeker, result.getJobseeker());
        assertEquals(vacancy, result.getVacancy());
        assertEquals(ApplicationStatus.DRAFT, result.getStatus()); // Should be set by @PrePersist or service
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    // Tests for updateCompletedSteps
    @Test
    void testUpdateCompletedSteps_success() {
        String newSteps = "step1,step2,step3_completed";
        when(jobApplicationRepository.findById(applicationId)).thenReturn(Optional.of(jobApplication));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(jobApplication);

        JobApplication updatedApp = jobApplicationService.updateCompletedSteps(applicationId, jobseekerId, newSteps);

        assertNotNull(updatedApp);
        assertEquals(newSteps, updatedApp.getCompletedSteps());
        verify(jobApplicationRepository).findById(applicationId);
        verify(jobApplicationRepository).save(jobApplication); // Verifies the same instance is saved
    }

    @Test
    void testUpdateCompletedSteps_applicationNotFound_throwsResourceNotFound() {
        when(jobApplicationRepository.findById(applicationId)).thenReturn(Optional.empty());
        String newSteps = "anySteps";

        assertThrows(ResourceNotFoundException.class, () ->
                jobApplicationService.updateCompletedSteps(applicationId, jobseekerId, newSteps));
        verify(jobApplicationRepository).findById(applicationId);
        verify(jobApplicationRepository, never()).save(any());
    }

    @Test
    void testUpdateCompletedSteps_notOwner_throwsForbiddenAccess() {
        UUID differentJobseekerId = UUID.randomUUID();
        when(jobApplicationRepository.findById(applicationId)).thenReturn(Optional.of(jobApplication));
        String newSteps = "anySteps";
        // jobApplication belongs to jobseekerId, but called with differentJobseekerId

        assertThrows(ForbiddenAccessException.class, () ->
                jobApplicationService.updateCompletedSteps(applicationId, differentJobseekerId, newSteps));
        verify(jobApplicationRepository).findById(applicationId);
        verify(jobApplicationRepository, never()).save(any());
    }

    @Test
    void testUpdateCompletedSteps_notDraftStatus_throwsIllegalState() {
        jobApplication.setStatus(ApplicationStatus.SUBMITTED); // Not DRAFT
        when(jobApplicationRepository.findById(applicationId)).thenReturn(Optional.of(jobApplication));
        String newSteps = "anySteps";

        assertThrows(IllegalStateException.class, () ->
                jobApplicationService.updateCompletedSteps(applicationId, jobseekerId, newSteps));
        verify(jobApplicationRepository).findById(applicationId);
        verify(jobApplicationRepository, never()).save(any());
    }
}
