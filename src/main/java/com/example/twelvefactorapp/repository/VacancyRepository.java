package com.example.twelvefactorapp.repository;

import com.example.twelvefactorapp.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, UUID> {

    /**
     * Finds vacancies by job title.
     * Spring Data JPA will automatically generate the query based on the method name.
     *
     * @param jobTitle The job title to search for.
     * @return A list of vacancies matching the job title.
     */
    List<Vacancy> findByJobTitle(String jobTitle);

    // Example of a more complex query method (optional, just for illustration)
    // List<Vacancy> findByJobTitleContainingIgnoreCaseAndContractType(String jobTitle, com.example.twelvefactorapp.model.enums.ContractType contractType);
}
