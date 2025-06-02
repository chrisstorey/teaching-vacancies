package com.example.twelvefactorapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "publisher_ats_api_clients", indexes = {
    @Index(name = "index_publisher_ats_api_clients_on_api_key", columnList = "apiKey", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherAtsApiClient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String apiKey;

    private String clientName;

    // Relationships, if any, to other entities like a PublisherUser or Company can be added here.
}
