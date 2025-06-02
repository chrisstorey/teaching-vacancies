package com.example.twelvefactorapp.dto;

import com.example.twelvefactorapp.model.Publisher;
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
public class PublisherDto {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static PublisherDto fromEntity(Publisher publisher) {
        if (publisher == null) {
            return null;
        }
        return new PublisherDto(
                publisher.getId(),
                publisher.getEmail(),
                publisher.getFirstName(),
                publisher.getLastName(),
                publisher.getCreatedAt(),
                publisher.getUpdatedAt()
        );
    }
}
