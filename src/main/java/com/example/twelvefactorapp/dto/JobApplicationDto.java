package com.example.twelvefactorapp.dto;

import com.example.twelvefactorapp.model.JobApplication;
import com.example.twelvefactorapp.model.enums.ApplicationStatus; // For status field type
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDto {

    private UUID id;
    private String status; // String representation of ApplicationStatus
    private String completedSteps;
    private UUID vacancyId;
    private String vacancyTitle; // Added for summary
    private UUID jobseekerId;
    // private String jobseekerName; // Could be added if needed
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static JobApplicationDto fromEntity(JobApplication application) {
        if (application == null) {
            return null;
        }
        return new JobApplicationDto(
                application.getId(),
                application.getStatus() != null ? application.getStatus().name() : null,
                application.getCompletedSteps(),
                application.getVacancy() != null ? application.getVacancy().getId() : null,
                application.getVacancy() != null ? application.getVacancy().getJobTitle() : null, // Added
                application.getJobseeker() != null ? application.getJobseeker().getId() : null,
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
