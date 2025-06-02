package com.example.twelvefactorapp.dto;

import com.example.twelvefactorapp.model.enums.ContractType; // Assuming this enum exists
// Assuming WorkingPattern and KeyStage (or Phases) enums exist if they are to be strongly typed
// For now, using String for jobRoles, workingPatterns, phases as per typical DTO flexibility
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtsVacancyDto {

    private UUID id;
    private String jobTitle;
    private String salary; // Could be a more complex object if needed
    private ContractType contractType;
    private LocalDate publishOn;
    private OffsetDateTime expiresAt;
    private String externalReference; // Assuming Vacancy entity has this field
    private List<String> jobRoles; // Example: "teacher", "head_of_year"
    private List<String> workingPatterns; // Example: "FULL_TIME", "PART_TIME"
    private List<String> phases; // Example: "primary", "secondary" (maps to KeyStage or similar concept)
    private List<OrganisationSummaryDto> organisations;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganisationSummaryDto {
        private UUID id;
        private String name;
        private String urn;
        // Add other fields like address, postcode if needed for the summary
    }
}
