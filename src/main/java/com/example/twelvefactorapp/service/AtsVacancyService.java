package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.dto.AtsVacancyDto;
import com.example.twelvefactorapp.model.OrganisationVacancy;
import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.model.enums.VacancyStatus;
import com.example.twelvefactorapp.repository.VacancyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AtsVacancyService {

    private final VacancyRepository vacancyRepository;

    public AtsVacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Transactional(readOnly = true)
    public Page<AtsVacancyDto> findVacanciesByApiClient(UUID apiClientId, Pageable pageable) {
        Page<Vacancy> vacancyPage = vacancyRepository.findByPublisherAtsApiClientIdAndStatusNotOrderByPublishOnDescCreatedAtDesc(
                apiClientId,
                VacancyStatus.TRASHED, // Exclude TRASHED status for the index view
                pageable
        );
        return vacancyPage.map(this::convertToAtsVacancyDto);
    }

    @Transactional(readOnly = true)
    public Optional<AtsVacancyDto> findVacancyByIdAndApiClient(UUID vacancyId, UUID apiClientId) {
        // For the "show" endpoint, typically we only want to show PUBLISHED vacancies.
        // This could be configurable or have other statuses if needed (e.g., SCHEDULED if admin access)
        Optional<Vacancy> vacancyOptional = vacancyRepository.findByIdAndPublisherAtsApiClientIdAndStatus(
                vacancyId,
                apiClientId,
                VacancyStatus.PUBLISHED
        );
        return vacancyOptional.map(this::convertToAtsVacancyDto);
    }

    private AtsVacancyDto convertToAtsVacancyDto(Vacancy vacancy) {
        if (vacancy == null) {
            return null;
        }

        AtsVacancyDto dto = new AtsVacancyDto();
        dto.setId(vacancy.getId());
        dto.setJobTitle(vacancy.getJobTitle());
        dto.setSalary(vacancy.getSalary()); // Assuming salary is a String, adjust if complex type
        dto.setContractType(vacancy.getContractType());
        dto.setPublishOn(vacancy.getPublishOn());
        dto.setExpiresAt(vacancy.getExpiresAt());
        dto.setExternalReference(vacancy.getExternalReference());
        dto.setJobRoles(vacancy.getJobRoles() != null ? vacancy.getJobRoles() : Collections.emptyList());

        if (vacancy.getWorkingPatterns() != null) {
            dto.setWorkingPatterns(
                vacancy.getWorkingPatterns().stream()
                       .map(Enum::name) // Convert WorkingPattern enum to String
                       .collect(Collectors.toList())
            );
        } else {
            dto.setWorkingPatterns(Collections.emptyList());
        }

        // Mapping keyStages (List<String>) to phases (List<String>)
        dto.setPhases(vacancy.getKeyStages() != null ? vacancy.getKeyStages() : Collections.emptyList());


        if (vacancy.getOrganisationVacancies() != null) {
            List<AtsVacancyDto.OrganisationSummaryDto> orgSummaries = vacancy.getOrganisationVacancies().stream()
                .map(OrganisationVacancy::getOrganisation) // Get Organisation from OrganisationVacancy
                .distinct() // Ensure unique organisations if multiple OrganisationVacancy point to same Org for this Vacancy
                .map(org -> new AtsVacancyDto.OrganisationSummaryDto(
                        org.getId(),
                        org.getName(),
                        org.getUrn() // Assuming Organisation entity has getUrn()
                        // Map other fields for OrganisationSummaryDto as needed
                ))
                .collect(Collectors.toList());
            dto.setOrganisations(orgSummaries);
        } else {
            dto.setOrganisations(Collections.emptyList());
        }

        return dto;
    }
}
