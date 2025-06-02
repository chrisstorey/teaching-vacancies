package com.example.twelvefactorapp.dto;

import com.example.twelvefactorapp.model.Jobseeker;
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
public class JobseekerDto {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static JobseekerDto fromEntity(Jobseeker jobseeker) {
        if (jobseeker == null) {
            return null;
        }
        return new JobseekerDto(
                jobseeker.getId(),
                jobseeker.getEmail(),
                jobseeker.getFirstName(),
                jobseeker.getLastName(),
                jobseeker.getCreatedAt(),
                jobseeker.getUpdatedAt()
        );
    }
}
