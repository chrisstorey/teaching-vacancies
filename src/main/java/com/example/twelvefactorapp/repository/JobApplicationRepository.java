package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.JobApplication;
import org.springframework.data.domain.Pageable; // For Pageable in findByJobseekerId
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    /**
     * Finds a job application by the jobseeker's ID and the vacancy's ID.
     * Useful for checking if a jobseeker has already applied for a specific vacancy.
     *
     * @param jobseekerId The ID of the jobseeker.
     * @param vacancyId   The ID of the vacancy.
     * @return An Optional containing the JobApplication if found, or an empty Optional otherwise.
     */
    Optional<JobApplication> findByJobseekerIdAndVacancyId(UUID jobseekerId, UUID vacancyId);

    /**
     * Finds all job applications submitted by a specific jobseeker, with pagination.
     *
     * @param jobseekerId The ID of the jobseeker.
     * @param pageable    Pagination information.
     * @return A list of JobApplications for the given jobseeker.
     *         Consider returning Page<JobApplication> if total count is also needed.
     */
    List<JobApplication> findByJobseekerId(UUID jobseekerId, Pageable pageable);

    // If you need to fetch applications by jobseeker ID and also sort them,
    // and want a Page for proper pagination UI:
    // Page<JobApplication> findByJobseekerId(UUID jobseekerId, Pageable pageable);
    // The current return type List<JobApplication> is also fine if only a slice is needed.
    // Changed to List as per initial request, but Page is often more useful.
}
