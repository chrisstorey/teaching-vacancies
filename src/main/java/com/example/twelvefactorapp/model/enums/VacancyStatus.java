package com.example.twelvefactorapp.model.enums;

public enum VacancyStatus {
    PUBLISHED,  // Actively visible
    DRAFT,      // Incomplete, not yet published
    TRASHED,    // Marked for deletion, not visible
    SCHEDULED,  // Approved, waiting for publish_on date
    EXPIRED     // Past its expires_at date
    // Add other statuses as relevant
}
