package com.example.twelvefactorapp.model.enums;

public enum ApplicationStatus {
    DRAFT,      // Application started but not yet submitted
    SUBMITTED,  // Application submitted by the jobseeker
    WITHDRAWN,  // Application withdrawn by the jobseeker
    REVIEWED,   // Application has been reviewed by a publisher
    SHORTLISTED,// Candidate has been shortlisted for this vacancy
    INTERVIEWING, // Candidate is in the interview process
    OFFERED,    // Candidate has been offered the position
    OFFER_ACCEPTED, // Candidate accepted the offer
    OFFER_DECLINED, // Candidate declined the offer
    UNSUCCESSFUL // Candidate was not selected
    // Add other relevant statuses as per the application workflow
}
