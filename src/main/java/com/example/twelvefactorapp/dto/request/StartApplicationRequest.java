package com.example.twelvefactorapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartApplicationRequest {

    @NotNull(message = "Vacancy ID cannot be null")
    private UUID vacancyId;
}
