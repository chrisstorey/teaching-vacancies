package com.example.twelvefactorapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobseekerLoginResponse {

    private String token; // JWT
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String tokenType = "Bearer"; // Default token type

    // Constructor without tokenType if a default is always used
    public JobseekerLoginResponse(String token, UUID userId, String email, String firstName, String lastName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tokenType = "Bearer"; // Explicitly set default here too
    }

    // Optional: Add expiresIn (long) if needed in the future
    // private long expiresIn;
}
