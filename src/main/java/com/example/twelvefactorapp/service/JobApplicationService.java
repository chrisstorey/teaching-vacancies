package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.exception.ForbiddenAccessException; // Added
import com.example.twelvefactorapp.exception.ResourceNotFoundException;
import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.model.Jobseeker;
import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.ApplicationStatus;
import com.example.twelvefactorapp.model.enums.VacancyStatus; // Assuming Vacancy has a status field
import com.example.twelvefactorapp.repository.JobApplicationRepository;
import com.example.twelvefactorapp.repository.JobseekerRepository;
import com.example.twelvefactorapp.repository.VacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime; // For checking expiresAt
import java.util.Optional;
import java.util.UUID;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobseekerRepository jobseekerRepository;
    private final VacancyRepository vacancyRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobseekerRepository jobseekerRepository,
                                 VacancyRepository vacancyRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobseekerRepository = jobseekerRepository;
        this.vacancyRepository = vacancyRepository;
    }

    /**
     * Starts a new draft job application for a given jobseeker and vacancy,
     * or retrieves an existing draft application if one already exists.
     *
     * @param jobseekerId The ID of the jobseeker.
     * @param vacancyId   The ID of the vacancy.
     * @return The existing draft or newly created JobApplication.
     * @throws ResourceNotFoundException if the jobseeker or vacancy is not found.
     * @throws IllegalStateException     if the vacancy is not open for applications or an application
     *                                   already exists in a non-draft state.
     */
    @Transactional
    public JobApplication startOrGetDraftApplication(UUID jobseekerId, UUID vacancyId) {
        Jobseeker jobseeker = jobseekerRepository.findById(jobseekerId)
                .orElseThrow(() -> new ResourceNotFoundException("Jobseeker", "id", jobseekerId));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", "id", vacancyId));

        // TODO: Add detailed check for vacancy application period and status.
        // Conceptual check (assuming Vacancy has 'expiresAt' and 'status' fields):
        if (vacancy.getExpiresAt() != null && vacancy.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("Vacancy (ID: " + vacancyId + ") has expired and is not open for applications.");
        }
        if (vacancy.getStatus() != null && vacancy.getStatus() != VacancyStatus.PUBLISHED) {
             // Assuming VacancyStatus.PUBLISHED indicates it's open. This might need refinement.
            throw new IllegalStateException("Vacancy (ID: " + vacancyId + ") is not currently published or open for applications. Status: " + vacancy.getStatus());
        }


        Optional<JobApplication> existingApplicationOpt = jobApplicationRepository.findByJobseekerIdAndVacancyId(jobseekerId, vacancyId);

        if (existingApplicationOpt.isPresent()) {
            JobApplication existingApplication = existingApplicationOpt.get();
            if (existingApplication.getStatus() == ApplicationStatus.DRAFT) {
                return existingApplication; // Return existing draft
            } else {
                throw new IllegalStateException("An application for this vacancy (ID: " + vacancyId +
                        ") by jobseeker (ID: " + jobseekerId +
                        ") already exists in a non-draft state: " + existingApplication.getStatus());
            }
        } else {
            // Create a new draft application
            JobApplication newApplication = new JobApplication();
            newApplication.setJobseeker(jobseeker);
            newApplication.setVacancy(vacancy);
            // Status defaults to DRAFT due to @PrePersist in JobApplication entity,
            // but can be set explicitly if needed:
            // newApplication.setStatus(ApplicationStatus.DRAFT);
            // 'completedSteps' could be initialized if necessary, e.g., newApplication.setCompletedSteps("");

            return jobApplicationRepository.save(newApplication);
        }
    }

    /**
     * Updates the 'completedSteps' field of a specific job application.
     *
     * @param applicationId   The ID of the job application to update.
     * @param jobseekerId     The ID of the jobseeker attempting the update (for ownership verification).
     * @param newCompletedSteps The new string value for completedSteps.
     * @return The updated JobApplication entity.
     * @throws ResourceNotFoundException if the job application is not found.
     * @throws ForbiddenAccessException if the jobseeker does not own the application.
     * @throws IllegalStateException    if the application is not in DRAFT status.
     */
    @Transactional
    public JobApplication updateCompletedSteps(UUID applicationId, UUID jobseekerId, String newCompletedSteps) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", applicationId));

        // Verify ownership
        if (!jobApplication.getJobseeker().getId().equals(jobseekerId)) {
            throw new ForbiddenAccessException("Not authorized to update this application. Jobseeker ID mismatch.");
        }

        // Verify status is DRAFT
        if (jobApplication.getStatus() != ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Application can only be updated if in DRAFT status. Current status: " + jobApplication.getStatus());
        }

        jobApplication.setCompletedSteps(newCompletedSteps);
        return jobApplicationRepository.save(jobApplication);
    }
}
