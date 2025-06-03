package com.example.twelvefactorapp.dto.request;

import jakarta.validation.constraints.Size; // Optional validation
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompletedStepsRequest {

    // Assuming completedSteps is a string, potentially CSV or JSON.
    // Validation can be added based on expected format or length.
    @Size(max = 2000, message = "Completed steps string cannot exceed 2000 characters") // Example validation
    private String completedSteps;
}
