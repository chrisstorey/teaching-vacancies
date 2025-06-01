# Software Requirements Document: Teaching Vacancies

## Table of Contents
- [1. Introduction](#1-introduction)
  - [1.1 Purpose](#11-purpose)
  - [1.2 Scope](#12-scope)
  - [1.3 Definitions, Acronyms, and Abbreviations](#13-definitions-acronyms-and-abbreviations)
  - [1.4 References](#14-references)
  - [1.5 Overview](#15-overview)
- [2. Overall Description](#2-overall-description)
  - [2.1 Product Perspective](#21-product-perspective)
  - [2.2 Product Functions](#22-product-functions)
  - [2.3 User Classes and Characteristics](#23-user-classes-and-characteristics)
  - [2.4 Operating Environment](#24-operating-environment)
  - [2.5 Design and Implementation Constraints](#25-design-and-implementation-constraints)
  - [2.6 Assumptions and Dependencies](#26-assumptions-and-dependencies)
- [3. System Features (Functional Requirements)](#3-system-features-functional-requirements)
  - [3.1 User Authentication and Authorization](#31-user-authentication-and-authorization)
  - [3.2 Vacancy Management (Publisher Facing)](#32-vacancy-management-publisher-facing)
  - [3.3 Job Search and Filtering (Jobseeker Facing)](#33-job-search-and-filtering-jobseeker-facing)
  - [3.4 Job Application Process (Jobseeker and Publisher Facing)](#34-job-application-process-jobseeker-and-publisher-facing)
  - [3.5 Jobseeker Profile Management](#35-jobseeker-profile-management)
  - [3.6 Publisher (Organisation) Profile Management](#36-publisher-organisation-profile-management)
  - [3.7 Notifications and Alerts (System to User)](#37-notifications-and-alerts-system-to-user)
  - [3.8 Support User Functions](#38-support-user-functions)
  - [3.9 System Administration Functions](#39-system-administration-functions)
  - [3.10 Reporting and Analytics (Placeholder)](#310-reporting-and-analytics-placeholder)
- [4. External Interface Requirements](#4-external-interface-requirements)
  - [4.1 User Interfaces](#41-user-interfaces)
  - [4.2 Hardware Interfaces (Not Applicable)](#42-hardware-interfaces-not-applicable)
  - [4.3 Software Interfaces](#43-software-interfaces)
    - [4.3.1 DfE Sign-in](#431-dfe-sign-in)
    - [4.3.2 GOV.UK One Login](#432-govuk-one-login)
    - [4.3.3 DWP Find a Job Service](#433-dwp-find-a-job-service)
    - [4.3.4 Publisher ATS API](#434-publisher-ats-api)
    - [4.3.5 Google Services (reCAPTCHA, Drive)](#435-google-services-recaptcha-drive)
    - [4.3.6 GOV.UK Notify](#436-govuk-notify)
    - [4.3.7 ONS ArcGIS Service](#437-ons-arcgis-service)
  - [4.4 Communications Interfaces](#44-communications-interfaces)
- [5. Non-Functional Requirements](#5-non-functional-requirements)
  - [5.1 Performance Requirements](#51-performance-requirements)
  - [5.2 Scalability Requirements](#52-scalability-requirements)
  - [5.3 Availability and Reliability Requirements](#53-availability-and-reliability-requirements)
  - [5.4 Security Requirements](#54-security-requirements)
  - [5.5 Maintainability Requirements](#55-maintainability-requirements)
  - [5.6 Usability Requirements](#56-usability-requirements)
  - [5.7 Accessibility Requirements](#57-accessibility-requirements)
  - [5.8 Data Integrity Requirements](#58-data-integrity-requirements)
  - [5.9 Localization and Internationalization](#59-localization-and-internationalization)
  - [5.10 Operational Requirements](#510-operational-requirements)
- [6. Data Requirements](#6-data-requirements)
  - [6.1 Data Model Overview (Conceptual)](#61-data-model-overview-conceptual)
  - [6.2 Detailed Data Dictionary (Key Entities)](#62-detailed-data-dictionary-key-entities)
    - [6.2.1 Vacancies (`vacancies`)](#621-vacancies-vacancies)
    - [6.2.2 Jobseekers (`jobseekers`)](#622-jobseekers-jobseekers)
    - [6.2.3 Publishers (`publishers`)](#623-publishers-publishers)
    - [6.2.4 Organisations (`organisations`)](#624-organisations-organisations)
    - [6.2.5 Job Applications (`job_applications`)](#625-job-applications-job_applications)
    - [6.2.6 Jobseeker Profiles (`jobseeker_profiles`)](#626-jobseeker-profiles-jobseeker_profiles)
    - [6.2.7 Job Preferences (`job_preferences`)](#627-job-preferences-job_preferences)
    - [6.2.8 Job Preferences Locations (`job_preferences_locations`)](#628-job-preferences-locations-job_preferences_locations)
    - [6.2.9 Employments (`employments`)](#629-employments-employments)
    - [6.2.10 Qualifications (`qualifications`)](#6210-qualifications-qualifications)
    - [6.2.11 Qualification Results (`qualification_results`)](#6211-qualification-results-qualification_results)
    - [6.2.12 Personal Details (`personal_details`)](#6212-personal-details-personal_details)
    - [6.2.13 References (`references`)](#6213-references-references)
    - [6.2.14 Subscriptions (Job Alerts) (`subscriptions`)](#6214-subscriptions-job-alerts-subscriptions)
    - [6.2.15 Alert Runs (`alert_runs`)](#6215-alert-runs-alert_runs)
    - [6.2.16 Feedbacks (`feedbacks`)](#6216-feedbacks-feedbacks)
    - [6.2.17 Notes (`notes`)](#6217-notes-notes)
    - [6.2.18 Equal Opportunities Reports (`equal_opportunities_reports`)](#6218-equal-opportunities-reports-equal_opportunities_reports)
    - [6.2.19 Organisation Vacancies (`organisation_vacancies`)](#6219-organisation-vacancies-organisation_vacancies)
    - [6.2.20 Organisation Publishers (`organisation_publishers`)](#6220-organisation-publishers-organisation_publishers)
    - [6.2.21 School Group Memberships (`school_group_memberships`)](#6221-school-group-memberships-school_group_memberships)
    - [6.2.22 Local Authority Publisher Schools (`local_authority_publisher_schools`)](#6222-local-authority-publisher-schools-local_authority_publisher_schools)
    - [6.2.23 Publisher Preferences (`publisher_preferences`)](#6223-publisher-preferences-publisher_preferences)
    - [6.2.24 Markers (`markers`)](#6224-markers-markers)
    - [6.2.25 Location Polygons (`location_polygons`)](#6225-location-polygons-location_polygons)
    - [6.2.26 Emergency Login Keys (`emergency_login_keys`)](#6226-emergency-login-keys-emergency_login_keys)
    - [6.2.27 Publisher ATS API Clients (`publisher_ats_api_clients`)](#6227-publisher-ats-api-clients-publisher_ats_api_clients)
    - [6.2.28 Active Storage Attachments (`active_storage_attachments`)](#6228-active-storage-attachments-active_storage_attachments)
    - [6.2.29 Active Storage Blobs (`active_storage_blobs`)](#6229-active-storage-blobs-active_storage_blobs)
    - [6.2.30 Friendly ID Slugs (`friendly_id_slugs`)](#6230-friendly-id-slugs-friendly_id_slugs)
    - [6.2.31 Sessions (`sessions`)](#6231-sessions-sessions)
    - [6.2.32 Versions (`versions`)](#6232-versions-versions)
    - [6.2.33 Noticed Events (`noticed_events`)](#6233-noticed-events-noticed_events)
    - [6.2.34 Noticed Notifications (`noticed_notifications`)](#6234-noticed-notifications-noticed_notifications)
  - [6.3 Data Retention and Archival](#63-data-retention-and-archival)
- [7. Use Cases](#7-use-cases)
  - [7.1 UC-001: Jobseeker Searches for Vacancy](#71-uc-001-jobseeker-searches-for-vacancy)
  - [7.2 UC-002: Publisher Posts New Vacancy](#72-uc-002-publisher-posts-new-vacancy)
  - [7.3 UC-003: Jobseeker Applies for Vacancy](#73-uc-003-jobseeker-applies-for-vacancy)
  - [7.4 UC-004: Jobseeker Creates Job Alert](#74-uc-004-jobseeker-creates-job-alert)
  - *(Others to be added)*
- [Appendix A: Business Rules Catalogue (Placeholder)](#appendix-a-business-rules-catalogue-placeholder)
- [Appendix B: Data Migration Considerations (Placeholder for Rewrite Context)](#appendix-b-data-migration-considerations-placeholder-for-rewrite-context)

## 1. Introduction

### 1.1 Purpose
This Software Requirements Document (SRD) specifies the requirements for the Teaching Vacancies service. The Teaching Vacancies service is a platform provided by the Department for Education (DfE) for schools and trusts in England to advertise their job vacancies, and for jobseekers (primarily teachers and education professionals) to find and apply for these positions.

This document is intended for a variety of stakeholders, including:
*   **Development Team:** To understand the features, business logic, and constraints for designing, building, and maintaining the software.
*   **Quality Assurance Team:** To develop test plans and test cases.
*   **Product Owners/Managers:** To define the scope and functionalities of the service.
*   **Future Development Teams:** To understand the system for potential rewrites, major enhancements, or ongoing maintenance.

This SRD aims to provide a comprehensive overview of the functional and non-functional requirements necessary to develop or re-implement the Teaching Vacancies service.

### 1.2 Scope
The scope of the Teaching Vacancies service includes:
*   Allowing authenticated publishers (school and Multi-Academy Trust staff) to create, manage, and publish job vacancies.
*   Allowing authenticated jobseekers to search for vacancies using various filters (location, role, subject, etc.), view vacancy details, save vacancies, and create job alerts.
*   Allowing jobseekers to apply for vacancies, either through an in-platform application process or by redirecting to an external application system (e.g., an ATS).
*   Providing user profile management for both jobseekers and publishers.
*   Integrating with DfE Sign-in for publisher authentication and GOV.UK One Login for jobseeker authentication.
*   Exporting vacancy data to the DWP "Find a job" service.
*   Providing an API (Publisher ATS API) for Applicant Tracking Systems to integrate with the service.
*   Sending notifications to users (e.g., job alerts, application confirmations) via GOV.UK Notify.
*   Providing administrative and support functionalities for DfE staff.

This document describes the existing system's requirements as a baseline for understanding its intended behavior, which would inform any future development or rewrite.

### 1.3 Definitions, Acronyms, and Abbreviations
Refer to the Glossary section in the [arc42 System Documentation](../arc42/arc42_template.md#12-glossary) for a comprehensive list of definitions, acronyms, and abbreviations. Key terms will also be defined within this document where first used if critical for immediate context.

### 1.4 References
*   [arc42 System Documentation](../arc42/arc42_template.md)
*   Existing source code for the Teaching Vacancies application.
*   Existing Architectural Decision Records (ADRs) in `/documentation/adr/`.
*   (Other DfE policies or standards documentation would be listed here if known and applicable).

### 1.5 Overview
The remainder of this document details the overall product description, specific functional requirements (system features), external interface requirements, non-functional requirements, data requirements, and key use cases. It aims to provide a clear and comprehensive specification for the Teaching Vacancies service.

## 2. Overall Description

### 2.1 Product Perspective
The Teaching Vacancies service is a key component of the Department for Education's digital offerings, designed to facilitate recruitment in the education sector in England. It replaces or complements previous methods of job advertising, aiming to be a central, free-to-use platform for schools and jobseekers.

The system is a standalone web application but integrates with critical national identity platforms (DfE Sign-in, GOV.UK One Login) and other government services (DWP Find a Job, GOV.UK Notify). It is an evolution of previous DfE job listing services and has undergone several technical iterations (e.g., in its search technology and integration approaches).

From a technical perspective, the current system is a Ruby on Rails monolithic application, hosted on Azure Kubernetes Service (AKS) within the DfE's Cloud Infrastructure Platform (CIP).

### 2.2 Product Functions
The major functions of the Teaching Vacancies service are:
*   **Vacancy Publication:** Enabling schools and trusts to advertise teaching and education-related roles.
*   **Job Search:** Allowing jobseekers to find relevant vacancies based on a wide range of criteria.
*   **Job Application (where applicable):** Facilitating the application process for jobseekers.
*   **Alerts and Notifications:** Keeping users informed about new vacancies or application status.
*   **User Account Management:** Allowing users to manage their profiles, preferences, and activities.
*   **Third-Party Integration:** Interfacing with external ATS and other government services.
*   **Support and Administration:** Providing tools for DfE support staff to manage the service and assist users.

These functions are detailed further in Section 3 (System Features).

### 2.3 User Classes and Characteristics

The Teaching Vacancies service caters to several distinct user classes:

*   **Jobseekers (e.g., Teachers, Teaching Assistants, Education Professionals):**
    *   **Characteristics:**
        *   Varying levels of technical proficiency, from novice to experienced computer users.
        *   Primarily motivated to find suitable job opportunities in educational institutions in England.
        *   May be actively looking for a new role or passively monitoring opportunities.
        *   Access the service from various devices (desktops, laptops, tablets, smartphones).
        *   Expect a user-friendly interface, efficient search tools, and clear information about vacancies.
        *   May require accessibility features.
    *   **Primary Activities:**
        *   Registering and managing their accounts (via GOV.UK One Login).
        *   Searching and browsing for job vacancies using various filters (location, subject, job role, working pattern, education phase, etc.).
        *   Viewing detailed information about specific vacancies.
        *   Saving vacancies of interest for later review.
        *   Creating and managing job alerts (subscriptions) to be notified of new relevant vacancies.
        *   Applying for vacancies, either through the platform's built-in application process or by being redirected to an external application site.
        *   Managing their jobseeker profile, including personal details, qualifications, work experience, and job preferences.

*   **Publishers (e.g., School Hiring Managers, School Administrators, Trust HR Staff, LA Recruitment Staff):**
    *   **Characteristics:**
        *   Typically possess moderate technical proficiency, using computers regularly for administrative tasks.
        *   Motivated to advertise job vacancies effectively to attract qualified candidates.
        *   Represent an educational institution (e.g., school, academy, Multi-Academy Trust, Local Authority).
        *   Require tools to easily create, manage, and track their job listings.
        *   Authenticated via DfE Sign-in.
    *   **Primary Activities:**
        *   Registering their organisation(s) (if not already present or managed centrally).
        *   Creating new job vacancy listings, providing detailed information (job title, description, salary, contract type, key dates, application process, etc.).
        *   Editing and updating existing vacancy listings.
        *   Closing or extending vacancy listings.
        *   Viewing applications submitted through the platform (for vacancies configured for in-platform applications).
        *   Managing their organisation's profile information.
        *   Utilizing a dashboard to view their active and past vacancies.
        *   Potentially interacting with features related to Applicant Tracking System (ATS) integrations.

*   **Support Users (DfE Staff):**
    *   **Characteristics:**
        *   DfE employees responsible for user support and operational oversight of the service.
        *   Possess good technical understanding of the service's functionality and administration.
        *   Authenticated via a DfE internal mechanism (likely DfE Sign-in with elevated privileges).
    *   **Primary Activities:**
        *   Accessing a dedicated support dashboard/interface.
        *   Viewing and managing user feedback (e.g., general feedback, job alert feedback).
        *   Assisting jobseekers and publishers with issues they encounter.
        *   Managing Publisher ATS API client access and configurations.
        *   Viewing service data and analytics (e.g., number of users, vacancies, applications).
        *   Potentially performing limited data correction or user account management tasks under strict guidelines.
        *   Monitoring system health and escalating issues to the technical team.

*   **System Administrators (DfE Technical Staff / Development Team):**
    *   **Characteristics:**
        *   Highly technically skilled individuals responsible for the maintenance, deployment, and advanced configuration of the system.
        *   Deep understanding of the application architecture, infrastructure, and codebase.
        *   Authenticated via secure DfE internal mechanisms with high levels of privilege.
    *   **Primary Activities:**
        *   Deploying new versions of the application.
        *   Managing and configuring the Azure infrastructure (AKS, PostgreSQL, Redis).
        *   Monitoring system performance, security, and logs at a detailed level.
        *   Performing database administration and maintenance tasks.
        *   Troubleshooting and resolving complex technical issues.
        *   Managing system secrets and security configurations.
        *   Implementing and testing disaster recovery procedures.
        *   Managing integrations with external services at a technical level.

### 2.4 Operating Environment
The Teaching Vacancies service operates in the following environment:
*   **Platform:** Azure Cloud Infrastructure Platform (CIP).
*   **Runtime:** Azure Kubernetes Service (AKS).
*   **Database:** Azure PostgreSQL Flexible Server.
*   **Caching/Job Queue:** Azure Cache for Redis.
*   **Web Browser Compatibility:** The service must be accessible and functional on modern, commonly used web browsers (e.g., latest versions of Chrome, Firefox, Edge, Safari) on desktop and mobile devices. Specific browser version support would need to be defined by DfE policy or based on user analytics.
*   **Accessibility:** The environment must support users with disabilities, requiring compliance with WCAG 2.1 Level AA.

### 2.5 Design and Implementation Constraints
Key design and implementation constraints are documented in the [arc42 System Documentation](../arc42/arc42_template.md#2-constraints). These include:
*   Mandatory use of DfE Sign-in for publishers and GOV.UK One Login for jobseekers.
*   Integration with DWP Find a Job service.
*   Adherence to GOV.UK Design System and accessibility standards.
*   Hosting on Azure CIP.
*   The current system is a Ruby on Rails monolith. A rewrite might choose a different stack (e.g., Java), which would then become a new implementation constraint for that version.

*(Further details on constraints specific to a rewrite, such as choice of Java framework, would be added here if this SRD were solely for a Java rewrite project.)*

### 2.6 Assumptions and Dependencies
*   **Availability of External Services:** The system depends on the continuous availability and correct functioning of DfE Sign-in, GOV.UK One Login, GOV.UK Notify, DWP Find a Job service, ONS ArcGIS, and Google Services.
*   **User Internet Access:** Users (jobseekers, publishers) are assumed to have reliable internet access and a compatible web browser.
*   **Data Accuracy from Publishers:** The accuracy of vacancy information relies on publishers providing correct and up-to-date details.
*   **DfE Policies:** The system must comply with prevailing DfE policies regarding data security, privacy, and service design.
*   **(For a rewrite context) Feature Parity Assumption:** A primary assumption for a rewrite would be the need to achieve functional parity with the existing system, unless specific deviations are explicitly approved.

## 3. System Features (Functional Requirements)

This section outlines the major functional capabilities of the Teaching Vacancies service, grouped by feature area or epic. Detailed functional requirements for each feature will be elaborated in subsequent analysis phases by examining specific controllers, models, and services.

### 3.1 User Authentication and Authorization
*   **Description:** Governs how users access the system and what actions they are permitted to perform.
*   **Jobseeker Specific Requirements:**
    *   **FR-JS-AUTH-001:** Jobseekers MUST be able to register and log in to the service using GOV.UK One Login.
    *   **FR-JS-AUTH-002:** Upon successful authentication via GOV.UK One Login, the system MUST create a local Jobseeker account if one does not already exist, linking it to the GOV.UK One Login unique identifier.
    *   **FR-JS-AUTH-003:** The system MUST manage Jobseeker sessions (e.g., session creation, timeouts, secure logout).
    *   **FR-JS-AUTH-004:** Jobseekers MUST only be able to access their own profiles, applications, and saved jobs/alerts. They MUST NOT be able to access publisher-specific or admin-specific functionalities.
    *   **FR-JS-AUTH-005:** The system SHOULD provide a fallback authentication mechanism for Jobseekers if GOV.UK One Login is unavailable (e.g., magic link to registered email). Business Rule: Fallback should require additional verification or have limited session duration.
*   **Publisher Specific Requirements:**
    *   **FR-PUB-AUTH-001:** Publishers MUST be able to register and log in to the service using DfE Sign-in.
    *   **FR-PUB-AUTH-002:** Upon successful authentication via DfE Sign-in, the system MUST associate the Publisher with their designated organisation(s) based on DfE Sign-in roles and organisation URNs. Business Rule: A publisher must be associated with at least one school, trust, or LA to post vacancies.
    *   **FR-PUB-AUTH-003:** The system MUST manage Publisher sessions (e.g., session creation, timeouts, secure logout).
    *   **FR-PUB-AUTH-004:** Publishers MUST only be able to manage vacancies and view applications for the organisation(s) they are explicitly associated with.
    *   **FR-PUB-AUTH-005:** The system SHOULD provide a fallback authentication mechanism for Publishers if DfE Sign-in is unavailable (e.g., magic link to registered email, potentially with reduced privileges or requiring re-verification). Business Rule: Fallback access rules to be defined.
*   **Support User Specific Requirements:**
    *   **FR-SUP-AUTH-001:** Support Users MUST authenticate using their DfE credentials, likely integrated with DfE Sign-in or a similar internal identity provider, to access the support interface.
    *   **FR-SUP-AUTH-002:** The system MUST differentiate Support Users with appropriate roles/permissions to access support-specific functionalities. Business Rule: Specific roles and permissions for support users to be defined (e.g., Level 1 Support, Level 2 Support with different capabilities).
    *   **FR-SUP-AUTH-003:** Support User sessions MUST be managed securely, including appropriate timeout periods.

### 3.2 Vacancy Management (Publisher Facing)
*   **Description:** Enables publishers to create, advertise, and manage job vacancies within their organisations.
*   **Vacancy Creation Process (Multi-Step Form):**
    *   **FR-PUB-VAC-C001:** The system MUST allow authenticated Publishers to create a new job vacancy. Business Rule: Publisher must be associated with an active organisation.
    *   **FR-PUB-VAC-C002:** The vacancy creation process MUST be a multi-step form, allowing Publishers to save drafts and complete later.
    *   **Step 1: Job Details:**
        *   **FR-PUB-VAC-DET-001:** Publisher MUST select the organisation (school/trust central office/LA) for which the vacancy is being created. Business Rule: List of organisations is restricted to those the Publisher is associated with.
        *   **FR-PUB-VAC-DET-002:** Publisher MUST enter a 'Job title' (mandatory, text, max 100 characters).
        *   **FR-PUB-VAC-DET-003:** Publisher MUST select 'Job roles' (mandatory, multi-select from predefined list: e.g., teacher, leadership, SEN specialist, teaching assistant, education support, administration, etc.). Business Rule: At least one job role must be selected.
        *   **FR-PUB-VAC-DET-004:** Publisher MUST select 'Subjects' if a 'teacher' or related role is selected (conditional mandatory, multi-select from predefined list).
        *   **FR-PUB-VAC-DET-005:** Publisher MUST select 'Working patterns' (mandatory, multi-select from predefined list: e.g., full-time, part-time, flexible, job_share, compressed_hours, staggered_hours). Business Rule: At least one working pattern must be selected.
        *   **FR-PUB-VAC-DET-006:** Publisher MUST select a 'Contract type' (mandatory, single select from predefined list: e.g., permanent, fixed-term, maternity_cover).
        *   **FR-PUB-VAC-DET-007:** If 'Fixed-term' or 'Maternity cover' is selected, 'Fixed term contract duration' text field becomes mandatory.
        *   **FR-PUB-VAC-DET-008:** Publisher MUST select 'Phases' (Education Phase) (mandatory, multi-select from predefined list: e.g., nursery, primary, secondary, middle, sixth_form_or_college, through_school).
        *   **FR-PUB-VAC-DET-009:** Publisher MAY select 'Key stages' (conditional, multi-select based on selected phases).
        *   **FR-PUB-VAC-DET-010:** Publisher MAY indicate if the role is 'Suitable for NQTs/ECTs'.
    *   **Step 2: Pay and Benefits:**
        *   **FR-PUB-VAC-PAY-001:** Publisher MUST provide 'Salary' information (mandatory, text, can describe range or specific figure, e.g., "£30,000 - £35,000 per year", "MPS/UPS").
        *   **FR-PUB-VAC-PAY-002:** Publisher MAY provide details of 'Benefits' (e.g., relocation package, TLR payments) (text).
    *   **Step 3: Role Description and Responsibilities:**
        *   **FR-PUB-VAC-DESC-001:** Publisher MUST provide 'About the role' (job summary/description) (mandatory, rich text, max Y characters).
        *   **FR-PUB-VAC-DESC-002:** Publisher MUST provide 'What the school offers' (information about the school/organisation) (mandatory, rich text, max Y characters).
        *   **FR-PUB-VAC-DESC-003:** Publisher MUST provide 'What we're looking for' (person specification/candidate requirements) (mandatory, rich text, max Y characters).
    *   **Step 4: Application Process:**
        *   **FR-PUB-VAC-APP-001:** Publisher MUST select an 'Application method' (e.g., through Teaching Vacancies, via school website, by email).
        *   **FR-PUB-VAC-APP-002:** If applying via school website, 'Link to application form or website' MUST be provided (mandatory, valid URL).
        *   **FR-PUB-VAC-APP-003:** If applying by email, 'Application email address' MUST be provided (mandatory, valid email format).
        *   **FR-PUB-VAC-APP-004:** Publisher MUST set a 'Closing date' for applications (mandatory, date). Business Rule: Closing date must be in the future.
        *   **FR-PUB-VAC-APP-005:** Publisher MAY set a 'Closing time' for applications. Default: 11:59 PM on closing date.
        *   **FR-PUB-VAC-APP-006:** Publisher MAY provide 'Contact email' for enquiries.
        *   **FR-PUB-VAC-APP-007:** Publisher MAY provide 'Contact number' for enquiries.
        *   **FR-PUB-VAC-APP-008:** Publisher MAY upload 'Supporting documents' for the vacancy (e.g., job description pack, application form). Business Rule: Max file size and allowed types (e.g., PDF, DOCX) apply. Virus scanning is performed.
    *   **Step 5: Review and Publish:**
        *   **FR-PUB-VAC-REV-001:** Publisher MUST be able to review all entered vacancy details before publishing.
        *   **FR-PUB-VAC-REV-002:** Publisher MUST be able to save the vacancy as a draft at any step.
        *   **FR-PUB-VAC-REV-003:** Publisher MUST be able to publish the vacancy. Business Rule: All mandatory fields across all steps must be completed.
        *   **FR-PUB-VAC-REV-004:** Upon publishing, the vacancy status changes to 'published' and becomes visible in Jobseeker searches.
*   **Vacancy Management Dashboard:**
    *   **FR-PUB-VAC-DASH-001:** Publishers MUST have a dashboard to view their vacancies, categorized by status (e.g., draft, published, pending, expired, closed early).
    *   **FR-PUB-VAC-DASH-002:** Publishers MUST be able to edit draft or published vacancies from the dashboard. Business Rule: Certain fields may become uneditable after publication or after applications are received.
    *   **FR-PUB-VAC-DASH-003:** Publishers MUST be able to view a published vacancy as a Jobseeker would see it.
    *   **FR-PUB-VAC-DASH-004:** Publishers MUST be able to end a vacancy early (change status to 'closed_early'). Business Rule: Confirmation required.
    *   **FR-PUB-VAC-DASH-005:** Publishers MUST be able to extend the closing date of a published vacancy. Business Rule: New closing date must be in the future.
    *   **FR-PUB-VAC-DASH-006:** Publishers MUST be able to copy an existing vacancy (draft, published, or expired) to create a new draft vacancy pre-filled with details from the copied vacancy.
    *   **FR-PUB-VAC-DASH-007:** Publishers SHOULD see basic statistics for their published vacancies (e.g., view count, number of applications if in-platform).
*   **ATS Integration (Publisher Perspective):**
    *   **FR-PUB-VAC-ATS-001:** For Publishers whose organisation uses an integrated ATS, vacancies created via the ATS API MUST appear in their dashboard.
    *   **FR-PUB-VAC-ATS-002:** Publishers MAY have limited editing capabilities for ATS-originated vacancies directly in the Teaching Vacancies interface if the ATS is the system of record. Business Rule: Sync logic with ATS determines editability.

### 3.3 Job Search and Filtering (Jobseeker Facing)
*   **Description:** Allows jobseekers to find relevant job vacancies based on various criteria.
*   **General Search & Display:**
    *   **FR-JS-SEARCH-001:** The system MUST allow jobseekers to view a list of job vacancies. Business Rule: Only 'published', 'not expired', and not 'closed_early' vacancies are displayed in search results.
    *   **FR-JS-SEARCH-002:** Search results MUST be paginated to ensure manageable data display and performance. Default page size: X vacancies (e.g. 20).
    *   **FR-JS-SEARCH-003:** Jobseekers MUST be able to view detailed information for a specific vacancy. This includes job title, description, salary, school details, contract type, working patterns, key dates (publish date, closing date, start date), application method, and supporting documents.
    *   **FR-JS-SEARCH-004:** The system MUST clearly indicate how a jobseeker can apply for a vacancy (e.g., link to internal application form, link to external site, or instructions for email application).
*   **Keyword Search:**
    *   **FR-JS-KEY-001:** The system MUST allow jobseekers to search for vacancies by keywords.
    *   **FR-JS-KEY-002:** Keyword search SHOULD match against fields like job title, job description, school name, subjects, and other relevant textual content. Business Rule: Specific fields for keyword matching are defined by search configuration (e.g., PostgreSQL full-text search dictionary and weighting).
    *   **FR-JS-KEY-003:** The system MAY support phonetic matching or stemming for keyword searches to improve relevance.
*   **Location Search:**
    *   **FR-JS-LOC-001:** The system MUST allow jobseekers to search for vacancies by a specific location input (postcode, town, city, county, specific school URN/name).
    *   **FR-JS-LOC-002:** If a postcode is entered, the system MUST validate its format. Business Rule: UK postcode format (e.g., AN NAA, ANN NAA, AAN NAA, AANN NAA, ANA NAA, AANA NAA).
    *   **FR-JS-LOC-003:** The system MUST geocode valid location inputs to latitude/longitude coordinates using the configured geocoding service (e.g., Google Geocoding API).
    *   **FR-JS-LOC-004:** The system MUST allow jobseekers to specify a search radius (e.g., 1 mile, 5 miles, 10, 20, 30, 50, 100, 200 miles) for location searches. Default radius: 20 miles.
    *   **FR-JS-LOC-005:** Location search results MUST include vacancies whose school locations fall within the specified radius of the geocoded search location. Business Rule: Uses PostGIS `ST_DWithin` for calculation.
    *   **FR-JS-LOC-006:** If the location input matches a known administrative area polygon (e.g., county name from `LocationPolygon`), the search MUST consider vacancies within that polygon. If a radius is also specified, the search expands by the radius from the polygon's border.
    *   **FR-JS-LOC-007:** The system MUST provide location suggestions as the jobseeker types into the location field (e.g., via `LocationSuggestion` lookup).
    *   **FR-JS-LOC-008:** The system MUST allow jobseekers to search for "nationwide" vacancies (i.e., those flagged as supporting relocation or remote working, or where location is not a primary filter).
*   **Filtering:**
    *   **FR-JS-FILT-001:** The system MUST allow jobseekers to filter search results by Job Role (e.g., teacher, leadership, SEN specialist, teaching assistant). Business Rule: Filter values based on predefined list.
    *   **FR-JS-FILT-002:** The system MUST allow jobseekers to filter search results by Education Phase (e.g., nursery, primary, secondary, middle, sixth form/college, through school). Business Rule: Filter values based on predefined list.
    *   **FR-JS-FILT-003:** The system MUST allow jobseekers to filter search results by Working Pattern (e.g., full-time, part-time, flexible, job share). Business Rule: Filter values based on predefined list.
    *   **FR-JS-FILT-004:** The system MUST allow jobseekers to filter search results by Subject or Specialism. Business Rule: Filter values based on predefined list of subjects.
    *   **FR-JS-FILT-005:** The system MUST allow jobseekers to filter by "Suitable for NQTs/ECTs" (Newly Qualified Teachers / Early Career Teachers).
    *   **FR-JS-FILT-006:** The system MUST allow jobseekers to filter by Contract Type (e.g., permanent, fixed-term, maternity cover).
    *   **FR-JS-FILT-007:** The system MUST allow jobseekers to filter by Pay Scale/Salary Band (if structured salary data is available).
    *   **FR-JS-FILT-008:** Applied filters MUST be clearly displayed and allow for easy removal or modification.
*   **Sorting:**
    *   **FR-JS-SORT-001:** The system MUST allow jobseekers to sort search results by relevance (default), closing date (most recent first), and publication date (most recent first).
*   **Map View (Geospatial Visualization):**
    *   **FR-JS-MAP-001:** The system SHOULD provide an option to display search results on a map.
    *   **FR-JS-MAP-002:** Map markers SHOULD indicate the location of schools with vacancies matching the search criteria.
    *   **FR-JS-MAP-003:** Clicking a map marker SHOULD display summary information for the vacancy/school and provide a link to the detailed vacancy page.

### 3.4 Job Application Process (Jobseeker and Publisher Facing)
*   **Description:** Facilitates the process for jobseekers to apply for vacancies and for publishers to receive/manage these applications (if using the in-platform feature).
*   **Jobseeker Specific Requirements:**
    *   **FR-JS-APP-001:** For vacancies with an external application method, the system MUST clearly display the link or instructions to the external application system/email.
    *   **FR-JS-APP-002:** For vacancies with in-platform applications, Jobseekers MUST be able to start a new application. Business Rule: Jobseeker must be logged in. Business Rule: Vacancy must be currently accepting applications (i.e., not expired or closed early).
    *   **FR-JS-APP-003:** The in-platform application process MUST be multi-step, allowing Jobseekers to save drafts and return later.
    *   **FR-JS-APP-004:** The application form MUST include sections for: Personal Information, Education & Qualifications, Work Experience/Employment History, Personal Statement/Letter of Application, References, Equal Opportunities Monitoring Information, and Declarations. Business Rule: Specific fields within these sections are defined by DfE policy and best practice.
    *   **FR-JS-APP-005:** Jobseekers MUST be able to upload supporting documents (e.g., CV, cover letter, qualification certificates). Business Rule: Supported file types and size limits apply. Uploaded files MUST be virus scanned.
    *   **FR-JS-APP-006:** Jobseekers MUST be able to review their completed application before submission.
    *   **FR-JS-APP-007:** Upon submission, the system MUST confirm successful submission to the Jobseeker.
    *   **FR-JS-APP-008:** Jobseekers MUST be able to view their submitted applications and their high-level status (e.g., submitted, viewed by school, shortlisted, unsuccessful).
    *   **FR-JS-APP-009:** Jobseekers MAY be able to withdraw a submitted application before a certain stage (e.g., before closing date, or if not yet viewed). Business Rule: Specific conditions for withdrawal to be defined.
*   **Publisher Specific Requirements:**
    *   **FR-PUB-APP-001:** For vacancies configured for in-platform applications, Publishers MUST be able to view a list of received applications.
    *   **FR-PUB-APP-002:** Publishers MUST be able to view the full details of each application, including all form data and any uploaded supporting documents.
    *   **FR-PUB-APP-003:** Publishers SHOULD be able to change the status of an application (e.g., New, Shortlisted, Interviewing, Offer Made, Rejected, Withdrawn by candidate). Business Rule: Available statuses and transitions to be defined.
    *   **FR-PUB-APP-004:** Publishers MAY be able to add internal notes or comments to an application, visible only to other publishers within their organisation.
    *   **FR-PUB-APP-005:** Publishers SHOULD be able to sort and filter applications (e.g., by status, date received).
    *   **FR-PUB-APP-006:** The system MAY provide functionality for Publishers to bulk download applications or selected application data.

### 3.5 Jobseeker Profile Management
*   **Description:** Allows jobseekers to create and manage a personal profile to aid in their job search and application process. This profile can be used to pre-fill parts of job applications.
*   **Key Capabilities (High-Level):**
    *   **FR-JS-PROF-001:** Jobseekers MUST be able to create and update a personal profile after registering via GOV.UK One Login.
    *   **FR-JS-PROF-002:** The profile MUST allow storage of Personal Details (e.g., name, contact information, address).
    *   **FR-JS-PROF-003:** The profile MUST allow management of Qualifications (e.g., degrees, teaching certificates, other relevant certifications), including awarding body, subject, and year obtained.
    *   **FR-JS-PROF-004:** The profile MUST allow management of Work Experience/Employment History (e.g., employer, job title, dates, responsibilities).
    *   **FR-JS-PROF-005:** The profile MUST allow Jobseekers to define Job Preferences (e.g., preferred roles, education phases, key stages, subjects, working patterns, desired locations/regions). These preferences MAY be used to tailor search results or job alert suggestions.
    *   **FR-JS-PROF-006:** The profile MUST allow Jobseekers to upload and manage a library of supporting documents (e.g., generic CV, template cover letter) for re-use in applications. Business Rule: File type and size limits apply; virus scanning is mandatory.
    *   **FR-JS-PROF-007:** Jobseekers MUST be able to manage their account settings, including email address (if different from GOV.UK One Login primary) and notification preferences.
    *   **FR-JS-PROF-008:** Jobseekers MUST have an option to request deletion of their account and associated personal data, in line with GDPR.
    *   **FR-JS-PROF-009:** The system SHOULD use information from the Jobseeker Profile to pre-fill relevant sections of an in-platform job application.

### 3.6 Publisher (Organisation) Profile Management
*   **Description:** Enables publishers to manage information about their organisation(s) (schools, trusts, LAs).
*   **Key Capabilities (High-Level):**
    *   **FR-PUB-ORG-001:** The system MUST display organisation information (e.g., name, address, type, website, contact details, description) on vacancy pages and potentially on dedicated organisation pages. Business Rule: Organisation data is primarily sourced from GIAS and updated periodically.
    *   **FR-PUB-ORG-002:** Authenticated Publishers associated with a Multi-Academy Trust (MAT) or Local Authority (LA) MUST be able to select which specific school(s) within their trust/LA to post a vacancy for, or post for the central trust/LA itself. Business Rule: Publisher permissions are derived from DfE Sign-in roles and associated organisation URNs/UIDs.
    *   **FR-PUB-ORG-003:** Publishers MAY be able to add or edit certain descriptive content about their specific school or organisation that appears on vacancy listings (e.g., "About our school" section, logo). Business Rule: Editable fields to be defined; core data from GIAS is not directly editable by publishers through TVS.
    *   **FR-PUB-ORG-004:** Publishers MUST be able to manage a list of contacts within their organisation for vacancy enquiries.
    *   **FR-PUB-ORG-005:** The system MUST allow Publishers to manage users associated with their organisation(s) within Teaching Vacancies (e.g., invite new users, remove users who have left). Business Rule: User management capabilities depend on Publisher's role/permissions within DfE Sign-in and their organisation structure.

### 3.7 Notifications and Alerts (System to User)
*   **Description:** Proactively informs users about relevant events or new information.
*   **Jobseeker Specific Requirements:**
    *   **FR-JS-ALERT-001 (Job Alerts):** Jobseekers MUST be able to create and save job alert subscriptions based on their search criteria (keywords, location, radius, filters).
    *   **FR-JS-ALERT-002:** The system MUST allow Jobseekers to manage their job alert subscriptions (e.g., view, edit criteria, delete, pause/resume).
    *   **FR-JS-ALERT-003:** The system MUST send email notifications (via GOV.UK Notify) to subscribed Jobseekers when new vacancies matching their alert criteria are published. Business Rule: Frequency of alerts (e.g., daily, instant) to be configurable or system-defined (e.g., daily digest).
    *   **FR-JS-ALERT-004 (Application Status Updates):** Jobseekers SHOULD receive email notifications when the status of their submitted in-platform application changes (e.g., when shortlisted by a publisher).
    *   **FR-JS-ALERT-005 (Account Notifications):** Jobseekers MUST receive email notifications for critical account activities (e.g., registration confirmation if applicable beyond GOV.UK One Login, password reset if fallback is used, confirmation of account deletion).
*   **Publisher Specific Requirements:**
    *   **FR-PUB-NOTIF-001 (New Application):** Publishers MUST receive an email notification (or a daily digest) when a new application is submitted for one of their vacancies via the in-platform process.
    *   **FR-PUB-NOTIF-002 (Vacancy Expiry Warning):** Publishers SHOULD receive an email notification a configurable number of days (e.g., 7 days) before their active vacancy is due to expire.
    *   **FR-PUB-NOTIF-003 (Vacancy Expired):** Publishers MAY receive an email notification when their vacancy has expired.
    *   **FR-PUB-NOTIF-004 (Feedback Prompt):** Publishers SHOULD receive an email prompting them to provide feedback on a vacancy after it has expired or been filled (e.g., number of applications, quality, if hired).

### 3.8 Support User Functions
*   **Description:** Provides DfE support staff with tools to manage the service and assist users.
*   **Authentication and Access:**
    *   **FR-SUP-AUTH-001:** Support Users MUST authenticate using their DfE credentials, likely integrated with DfE Sign-in or a similar internal identity provider, to access the support interface.
    *   **FR-SUP-AUTH-002:** The system MUST differentiate Support Users with appropriate roles/permissions to access support-specific functionalities. Business Rule: Specific roles and permissions for support users to be defined (e.g., Level 1 Support, Level 2 Support with different capabilities).
    *   **FR-SUP-AUTH-003:** Support User sessions MUST be managed securely, including appropriate timeout periods.
*   **Support Dashboard:**
    *   **FR-SUP-DASH-001:** Support Users MUST have access to a dashboard providing an overview of key service metrics and links to support functionalities.
    *   **FR-SUP-DASH-002:** Key metrics MAY include: total active vacancies, new vacancies today, total jobseekers, new jobseeker registrations today, total publishers, number of job alerts, number of applications submitted today/this week.
*   **Feedback Management:**
    *   **FR-SUP-FDBK-001:** The system MUST allow authenticated Support Users to view a list of all user-submitted feedback (from Jobseekers and Publishers).
    *   **FR-SUP-FDBK-002:** The feedback list MUST be filterable by feedback type (e.g., 'general', 'job_alert_unsubscribe', 'vacancy_feedback', 'account_closure', 'report_abuse/spam').
    *   **FR-SUP-FDBK-003:** The feedback list MUST be filterable by user type (e.g., 'Jobseeker', 'Publisher', 'Anonymous').
    *   **FR-SUP-FDBK-004:** The feedback list MUST display key information for each item (e.g., date submitted, type, user identifier (if available), summary of feedback content, associated vacancy ID if applicable).
    *   **FR-SUP-FDBK-005:** Support Users MUST be able to view the full details of an individual piece of feedback.
    *   **FR-SUP-FDBK-006:** The system MAY allow Support Users to mark feedback items with a status (e.g., 'Open', 'Investigating', 'Resolved', 'Archived', 'No Action Required').
    *   **FR-SUP-FDBK-007:** The system MUST allow Support Users to export feedback data (e.g., to CSV) for further analysis. Business Rule: Export format and included fields to be defined, ensuring PII is handled appropriately.
*   **Publisher ATS API Client Management:**
    *   **FR-SUP-ATS-001:** Support Users (with appropriate permissions) MUST be able to view a list of registered Publisher ATS API clients.
    *   **FR-SUP-ATS-002:** Support Users (with appropriate permissions) MUST be able to create a new API client, generating an API key for a publisher/ATS provider. Business Rule: Process for approving and provisioning API clients to be defined.
    *   **FR-SUP-ATS-003:** Support Users (with appropriate permissions) MUST be able to regenerate/re-issue an API key for an existing client if compromised or requested.
    *   **FR-SUP-ATS-004:** Support Users (with appropriate permissions) MUST be able to deactivate/revoke an API client's access.
*   **Service Data Viewing & Reporting:**
    *   **FR-SUP-DATA-001:** Support Users SHOULD have access to a performance dashboard displaying key metrics related to job listings (e.g., number of live jobs, jobs expiring soon, jobs per region), user sign-ins, and job alert activity.
    *   **FR-SUP-DATA-002:** Support Users MUST be able to download an aggregated Equal Opportunities Report based on anonymized data from in-platform job applications. Business Rule: Report content and format to be defined by DfE policy.
    *   **FR-SUP-DATA-003:** Support Users MAY be able to download other operational reports, such as a list of unfilled vacancies after a certain period, or a list of schools that have not posted recently.
*   **User Account Assistance (Limited):**
    *   **FR-SUP-USER-001:** Support Users MAY be able to look up Jobseeker or Publisher accounts by email address or other identifiers for verification purposes. Business Rule: Access to PII must be strictly controlled and logged.
    *   **FR-SUP-USER-002:** Support Users MAY have the ability to assist with common account issues, such as triggering a password reset email for fallback authentication methods or helping users understand DfE Sign-in/GOV.UK One Login processes. Business Rule: Direct password changes by Support Users are prohibited.

### 3.9 System Administration Functions
*   **Description:** Enables technical staff to maintain and operate the system. These are typically not user-facing application features but backend operational capabilities.
*   **Key Capabilities (High-Level):** (Admin-focused, to be detailed in a subsequent step)
    *   Deployment of new application versions...
    *   ...

### 3.10 Reporting and Analytics (Placeholder)
*   **Description:** While detailed analytics might reside in external systems (e.g., BigQuery), the application itself might provide some basic reporting for publishers or internal DfE use.
*   **Key Capabilities (High-Level):**
    *   For Publishers: Basic view counts or application numbers for their vacancies.
    *   For DfE/Support: Overall service usage statistics (e.g., number of vacancies, jobseekers, applications over time).
    *(This area requires further definition based on actual system capabilities vs. what's handled by external analytics platforms like Google BigQuery/Looker Studio mentioned in arc42).*

## 4. External Interface Requirements

### 4.1 User Interfaces
### 4.2 Hardware Interfaces (Not Applicable)
### 4.3 Software Interfaces
    - [4.3.1 DfE Sign-in](#431-dfe-sign-in)
    - [4.3.2 GOV.UK One Login](#432-govuk-one-login)
    - [4.3.3 DWP Find a Job Service](#433-dwp-find-a-job-service)
    - [4.3.4 Publisher ATS API](#434-publisher-ats-api)
    - [4.3.5 Google Services (reCAPTCHA, Drive)](#435-google-services-recaptcha-drive)
    - [4.3.6 GOV.UK Notify](#436-govuk-notify)
    - [4.3.7 ONS ArcGIS Service](#437-ons-arcgis-service)
### 4.4 Communications Interfaces

## 5. Non-Functional Requirements

### 5.1 Performance Requirements
### 5.2 Scalability Requirements
### 5.3 Availability and Reliability Requirements
### 5.4 Security Requirements
### 5.5 Maintainability Requirements
### 5.6 Usability Requirements
### 5.7 Accessibility Requirements
### 5.8 Data Integrity Requirements
### 5.9 Localization and Internationalization
### 5.10 Operational Requirements

## 6. Data Requirements

### 6.1 Data Model Overview (Conceptual)
This section provides a high-level conceptual data model illustrating the main entities of the Teaching Vacancies service and their primary relationships.

```plantuml
@startuml ConceptualDataModel
!define ENTITY_FONT_SIZE 12
!define ATTRIBUTE_FONT_SIZE 10

hide stereotype
skinparam defaultTextAlignment center
skinparam linetype ortho
skinparam shadowing false
skinparam roundcorner 10
skinparam class {
    BackgroundColor PaleTurquoise
    ArrowColor RoyalBlue
    BorderColor RoyalBlue
    FontSize ENTITY_FONT_SIZE
}
skinparam classAttribute {
    FontSize ATTRIBUTE_FONT_SIZE
}

entity "Jobseeker" as jobseeker {
  id (UUID)
  email
  govuk_one_login_id
  --
  *Manages profile, applies for jobs, creates subscriptions*
}

entity "Publisher" as publisher {
  id (UUID)
  email
  oid (DfE Sign-in ID)
  --
  *Manages organisation vacancies*
}

entity "Organisation" as organisation {
  id (UUID)
  name
  type (School, SchoolGroup, LA)
  urn / uid
  --
  *School, Trust, or LA that lists vacancies*
}

entity "Vacancy" as vacancy {
  id (UUID)
  job_title
  status (published, draft, expired)
  expires_at
  --
  publisher_id (FK)
  publisher_organisation_id (FK to Organisation)
  --
  *Job listing created by a Publisher*
}

entity "JobApplication" as job_application {
  id (UUID)
  status (submitted, shortlisted)
  submitted_at
  --
  jobseeker_id (FK to Jobseeker)
  vacancy_id (FK to Vacancy)
  --
  *Application by a Jobseeker for a Vacancy*
}

entity "JobseekerProfile" as jobseeker_profile {
  id (UUID)
  active
  --
  jobseeker_id (FK to Jobseeker)
  --
  *Jobseeker's professional profile*
}

entity "Employment" as employment {
  id (UUID)
  job_title
  organisation
  --
  job_application_id (FK)
  jobseeker_profile_id (FK)
  --
  *Work history for Jobseeker Profile or Application*
}

entity "Qualification" as qualification {
  id (UUID)
  name
  grade
  --
  job_application_id (FK)
  jobseeker_profile_id (FK)
  --
  *Educational qualification for Profile or Application*
}

entity "Subscription" as subscription {
  id (UUID)
  email
  frequency
  search_criteria (JSON)
  --
  *Job alert created by a Jobseeker or anonymous user*
}

entity "Feedback" as feedback {
  id (UUID)
  feedback_type
  comment
  --
  jobseeker_id (FK, optional)
  publisher_id (FK, optional)
  vacancy_id (FK, optional)
  --
  *User feedback on various aspects of the service*
}

entity "Document (ActiveStorage)" as document {
  (Conceptual)
  filename
  content_type
  --
  record_id (Polymorphic FK)
  record_type (Polymorphic FK)
  --
  *Represents uploaded files (CVs, vacancy docs)*
}

jobseeker "1" --o{ "many" job_application : "submits"
vacancy "1" --o{ "many" job_application : "receives"
jobseeker "1" --o| "1" jobseeker_profile : "has"
jobseeker "1" --o{ "many" subscription : "creates"

jobseeker_profile "1" --o{ "many" employment : "has work history"
jobseeker_profile "1" --o{ "many" qualification : "has qualifications"
job_application "1" --o{ "many" employment : "details work history"
job_application "1" --o{ "many" qualification : "details qualifications"

publisher "1" --o{ "many" vacancy : "creates/manages"
organisation "1" --o{ "many" vacancy : "associated with"  // via organisation_vacancies or publisher_organisation_id

publisher "many" --o{ "many" organisation : "manages (via OrganisationPublisher)"

vacancy "1" --o{ "many" feedback : "can have"
jobseeker "0..1" --o{ "many" feedback : "can provide"
publisher "0..1" --o{ "many" feedback : "can provide"

' Conceptual Link for Documents
vacancy "1" ..o{ "many" document : "has supporting"
job_application "1" ..o{ "many" document : "has supporting"

@enduml
```

*(Note: This ERD is a conceptual, simplified representation. Not all entities or detailed relationships from `db/schema.rb` are shown to maintain clarity at this overview level. For example, join tables like `organisation_vacancies` are represented by direct many-to-many style implications where appropriate for a conceptual model.)*

### 6.2 Detailed Data Dictionary (Key Entities)

This section provides a more detailed breakdown of key database tables (entities) and their columns, derived from the `db/schema.rb` file.

#### 6.2.1 Vacancies (`vacancies`)
Stores information about job vacancies advertised on the service.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `job_title` (`string`): Job title of the vacancy.
*   `slug` (`string`): `NOT NULL`. URL-friendly identifier for the vacancy, indexed.
*   `job_advert` (`text`): Main content of the job advertisement.
*   `benefits_details` (`text`): Details of benefits offered with the job.
*   `starts_on` (`date`): Proposed start date for the job.
*   `contact_email` (`string`): Email address for enquiries related to the vacancy.
*   `status` (`integer`): Current status of the vacancy (e.g., draft, published, expired), indexed.
*   `publish_on` (`date`): Date on which the vacancy is scheduled to be published, indexed.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `application_link` (`string`): URL for external application, if applicable.
*   `working_patterns` (`integer[]`): `array: true`. Array of applicable working patterns (e.g., full_time, part_time).
*   `listed_elsewhere` (`integer`): Indicates if the job is listed on other platforms.
*   `hired_status` (`integer`): Status regarding whether the position has been filled.
*   `stats_updated_at` (`datetime`): `precision: nil`. Timestamp for when view statistics were last updated.
*   `publisher_id` (`uuid`): Foreign key to `publishers.id`, indexed. Identifies the publisher who created the vacancy.
*   `expires_at` (`datetime`): `precision: nil`. Date and time when the vacancy listing expires, indexed.
*   `salary` (`string`): Salary information for the job.
*   `about_school` (`text`): Information about the school/organisation offering the vacancy.
*   `subjects` (`string[]`): `array: true`. Array of subjects related to the vacancy.
*   `school_visits_details` (`text`): Information about arranging school visits.
*   `how_to_apply` (`text`): Instructions on how to apply for the vacancy.
*   `job_location` (`integer`): Type of job location (e.g., at_one_school, at_multiple_schools, central_office).
*   `readable_job_location` (`string`): Human-readable job location.
*   `job_roles` (`integer[]`): `array: true`. Array of job roles associated with the vacancy.
*   `contact_number` (`string`): Phone number for enquiries.
*   `publisher_organisation_id` (`uuid`): Foreign key to `organisations.id`, indexed. The organisation on whose behalf the vacancy is published.
*   `starts_asap` (`boolean`): Indicates if the job starts as soon as possible.
*   `contract_type` (`integer`): Type of contract (e.g., permanent, fixed_term).
*   `fixed_term_contract_duration` (`string`): Duration of a fixed-term contract.
*   `personal_statement_guidance` (`text`): Guidance for writing a personal statement.
*   `enable_job_applications` (`boolean`): Flag to enable in-platform job applications.
*   `completed_steps` (`string[]`): `default: []`, `NOT NULL`, `array: true`. Tracks completed steps in the vacancy creation form.
*   `actual_salary` (`string`): Detailed or specific salary figure.
*   `working_patterns_details` (`text`): Additional details about working patterns.
*   `key_stages` (`integer[]`): `array: true`. Array of applicable key stages.
*   `geolocation` (`geography`): `limit: {srid: 4326, type: "geometry", geographic: true}`. Geographic coordinates of the primary job location, indexed (gist).
*   `readable_phases` (`string[]`): `default: []`, `array: true`. Human-readable education phases.
*   `searchable_content` (`tsvector`): For full-text search, indexed (gin).
*   `google_index_removed` (`boolean`): `default: false`. Flag if removed from Google index.
*   `parental_leave_cover_contract_duration` (`string`): Duration if it's a parental leave cover.
*   `expired_vacancy_feedback_email_sent_at` (`datetime`): `precision: nil`. When feedback email for expired vacancy was sent.
*   `external_source` (`string`): Source if imported externally (e.g., an ATS name), indexed.
*   `external_reference` (`string`): Reference ID from the external source, indexed.
*   `external_advert_url` (`string`): URL of the original advert on an external source.
*   `ect_status` (`integer`): Suitability for Early Career Teachers (formerly NQTs).
*   `pay_scale` (`string`): Pay scale for the role.
*   `benefits` (`boolean`): Indicates if benefits are offered.
*   `full_time_details` (`text`): Details if full-time.
*   `part_time_details` (`text`): Details if part-time.
*   `phases` (`integer[]`): `array: true`. Array of education phases.
*   `start_date_type` (`integer`): Type of start date (e.g., specific_date, asap, other).
*   `earliest_start_date` (`date`): Earliest possible start date.
*   `latest_start_date` (`date`): Latest possible start date.
*   `other_start_date_details` (`text`): Textual details for non-specific start dates.
*   `receive_applications` (`integer`): How applications are received (e.g., email, website, internal).
*   `application_email` (`string`): Email address for receiving applications.
*   `school_visits` (`boolean`): Indicates if school visits are encouraged/possible.
*   `contact_number_provided` (`boolean`): True if a contact number is provided.
*   `skills_and_experience` (`string`): Required skills and experience.
*   `school_offer` (`string`): What the school offers to the candidate.
*   `safeguarding_information_provided` (`boolean`): True if safeguarding information is provided.
*   `safeguarding_information` (`string`): Specific safeguarding information text.
*   `further_details_provided` (`boolean`): True if further details are provided.
*   `further_details` (`string`): Additional details for the vacancy.
*   `include_additional_documents` (`boolean`): Flag to include additional documents.
*   `visa_sponsorship_available` (`boolean`): Indicates if visa sponsorship is available.
*   `is_parental_leave_cover` (`boolean`): True if this is a parental leave cover role.
*   `hourly_rate` (`string`): Hourly rate if applicable.
*   `is_job_share` (`boolean`): True if job share is an option.
*   `flexi_working` (`string`): Details about flexible working arrangements.
*   `extension_reason` (`integer`): Reason for extending a vacancy.
*   `other_extension_reason_details` (`string`): Textual details for other extension reasons.
*   `publisher_ats_api_client_id` (`uuid`): Foreign key to `publisher_ats_api_clients.id`, indexed. Identifies the ATS client if vacancy was posted via API.
*   `religion_type` (`integer`): Religious character of the school/vacancy, if applicable.
*   `flexi_working_details_provided` (`boolean`): True if flexible working details are provided.
*   `discarded_at` (`datetime`): Timestamp if the vacancy was discarded (soft delete), indexed.

#### 6.2.2 Jobseekers (`jobseekers`)
Stores information about job-seeking users.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `email` (`string`): `default: ""`, `NOT NULL`, `indexed unique`. Jobseeker's email address.
*   `sign_in_count` (`integer`): `default: 0`, `NOT NULL`. Number of times the jobseeker has signed in.
*   `current_sign_in_at` (`datetime`): `precision: nil`. Timestamp of the current sign-in.
*   `last_sign_in_at` (`datetime`): `precision: nil`. Timestamp of the last sign-in.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `account_closed_on` (`date`): Date when the account was closed.
*   `current_sign_in_ip_ciphertext` (`text`): Encrypted field storing current sign-in IP. Actual data type before encryption is string.
*   `last_sign_in_ip_ciphertext` (`text`): Encrypted field storing last sign-in IP. Actual data type before encryption is string.
*   `govuk_one_login_id` (`string`): `indexed unique`. Unique identifier from GOV.UK One Login.
*   `account_merge_confirmation_code` (`string`): Code used for merging accounts.
*   `account_merge_confirmation_code_generated_at` (`datetime`): Timestamp when merge code was generated.
*   `email_opt_out` (`boolean`): `default: false`, `NOT NULL`. Indicates if the jobseeker opted out of emails.
*   `email_opt_out_reason` (`integer`): Reason for opting out of emails.
*   `email_opt_out_comment` (`text`): Additional comment for opting out.

#### 6.2.3 Publishers (`publishers`)
Stores information about users who publish vacancies (school/trust staff).
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `oid` (`string`): `indexed unique`. Unique identifier from DfE Sign-in.
*   `accepted_terms_at` (`datetime`): `precision: nil`. Timestamp when the publisher accepted terms and conditions.
*   `email` (`string`): `indexed`. Publisher's email address.
*   `last_activity_at` (`datetime`): `precision: nil`. Timestamp of the publisher's last activity.
*   `created_at` (`datetime`): Timestamp of creation.
*   `updated_at` (`datetime`): Timestamp of last update.
*   `family_name_ciphertext` (`text`): Encrypted field storing family name. Actual data type before encryption is string.
*   `given_name_ciphertext` (`text`): Encrypted field storing given name. Actual data type before encryption is string.
*   `dismissed_new_features_page_at` (`datetime`): `precision: nil`. Timestamp when new features page was dismissed.
*   `unsubscribed_from_expired_vacancy_prompt_at` (`datetime`): `precision: nil`. Timestamp when unsubscribed from expired vacancy prompts.
*   `acknowledged_candidate_profiles_interstitial` (`boolean`): `default: false`, `NOT NULL`. Flag indicating if the publisher acknowledged the candidate profiles interstitial.

#### 6.2.4 Organisations (`organisations`)
Stores information about schools, trusts, and local authorities.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `type` (`string`): `indexed`. Type of organisation (e.g., School, SchoolGroup, LocalAuthority).
*   `name` (`string`): Name of the organisation.
*   `description` (`text`): Description of the organisation.
*   `urn` (`string`): `indexed unique`. Unique Reference Number (for schools).
*   `uid` (`string`): `indexed unique`. Unique Identifier (for trusts/LAs).
*   `phase` (`integer`): Education phase (e.g., primary, secondary).
*   `url` (`string`): Website URL of the organisation.
*   `minimum_age` (`integer`): Minimum age of pupils.
*   `maximum_age` (`integer`): Maximum age of pupils.
*   `address` (`string`): Street address.
*   `town` (`string`): Town or city.
*   `county` (`string`): County.
*   `postcode` (`string`): Postcode.
*   `locality` (`text`): Locality.
*   `address3` (`text`): Additional address line.
*   `gias_data` (`json`): Raw data imported from GIAS.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `url_override` (`string`): Overridden website URL.
*   `region` (`string`): Region of the organisation.
*   `detailed_school_type` (`string`): Detailed school type from GIAS.
*   `school_type` (`string`): General school type from GIAS.
*   `local_authority_code` (`string`): `indexed unique`. Local authority code.
*   `group_type` (`string`): Type of group (e.g., Multi-academy trust).
*   `local_authority_within` (`string`): Name of the local authority the organisation is within.
*   `establishment_status` (`string`): Establishment status (e.g., Open, Closed).
*   `geopoint` (`geography`): `limit: {srid: 4326, type: "st_point", geographic: true}`, `indexed (gist)`. Geographic coordinates of the organisation.
*   `gias_data_hash` (`text`): Hash of GIAS data for detecting changes.
*   `slug` (`string`): `indexed unique`. URL-friendly identifier.
*   `email` (`string`): Contact email for the organisation.
*   `safeguarding_information` (`string`): Default safeguarding information text.
*   `searchable_content` (`tsvector`): For full-text search, indexed (gin).

#### 6.2.5 Job Applications (`job_applications`)
Stores applications submitted by jobseekers for vacancies.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `status` (`integer`): Current status of the application (e.g., draft, submitted, shortlisted).
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `jobseeker_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `jobseekers.id`.
*   `vacancy_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `vacancies.id`.
*   `completed_steps` (`integer[]`): `default: []`, `NOT NULL`, `array: true`. Tracks completed steps in the application form.
*   `submitted_at` (`datetime`): `precision: nil`. Timestamp when the application was submitted.
*   `draft_at` (`datetime`): `precision: nil`. Timestamp when the application was last saved as draft.
*   `shortlisted_at` (`datetime`): `precision: nil`. Timestamp when the application was shortlisted.
*   `unsuccessful_at` (`datetime`): `precision: nil`. Timestamp when the application was marked unsuccessful.
*   `withdrawn_at` (`datetime`): `precision: nil`. Timestamp when the application was withdrawn by the jobseeker.
*   `qualified_teacher_status` (`string`): `default: ""`, `NOT NULL`. Jobseeker's QTS status.
*   `qualified_teacher_status_year` (`string`): `default: ""`, `NOT NULL`. Year QTS was obtained.
*   `qualified_teacher_status_details` (`text`): `default: ""`, `NOT NULL`. Details about QTS.
*   `disability` (`string`): `default: ""`, `NOT NULL`. Disability information (Equal Ops).
*   `gender` (`string`): `default: ""`, `NOT NULL`. Gender information (Equal Ops).
*   `gender_description` (`string`): `default: ""`, `NOT NULL`. Other gender description (Equal Ops).
*   `orientation` (`string`): `default: ""`, `NOT NULL`. Sexual orientation (Equal Ops).
*   `orientation_description` (`string`): `default: ""`, `NOT NULL`. Other orientation description (Equal Ops).
*   `ethnicity` (`string`): `default: ""`, `NOT NULL`. Ethnicity (Equal Ops).
*   `ethnicity_description` (`string`): `default: ""`, `NOT NULL`. Other ethnicity description (Equal Ops).
*   `religion` (`string`): `default: ""`, `NOT NULL`. Religion (Equal Ops).
*   `religion_description` (`string`): `default: ""`, `NOT NULL`. Other religion description (Equal Ops).
*   `reviewed_at` (`datetime`): `precision: nil`. Timestamp when the application was reviewed by a publisher.
*   `country` (`string`): `default: ""`, `NOT NULL`. Country of residence.
*   `age` (`string`): `default: ""`, `NOT NULL`. Age bracket (Equal Ops).
*   `email_address` (`string`): `default: ""`, `NOT NULL`. Jobseeker's email at time of application.
*   `withdrawn_by_closing_account` (`boolean`): `default: false`, `NOT NULL`. If application was withdrawn due to account closure.
*   `first_name_ciphertext` (`text`): Encrypted field storing first name. Actual data type before encryption: string.
*   `last_name_ciphertext` (`text`): Encrypted field storing last name. Actual data type before encryption: string.
*   `previous_names_ciphertext` (`text`): Encrypted field storing previous names. Actual data type before encryption: string.
*   `street_address_ciphertext` (`text`): Encrypted field storing street address. Actual data type before encryption: string.
*   `city_ciphertext` (`text`): Encrypted field storing city. Actual data type before encryption: string.
*   `postcode_ciphertext` (`text`): Encrypted field storing postcode. Actual data type before encryption: string.
*   `phone_number_ciphertext` (`text`): Encrypted field storing phone number. Actual data type before encryption: string.
*   `teacher_reference_number_ciphertext` (`text`): Encrypted field storing TRN. Actual data type before encryption: string.
*   `national_insurance_number_ciphertext` (`text`): Encrypted field storing NIN. Actual data type before encryption: string.
*   `personal_statement_ciphertext` (`text`): Encrypted field storing personal statement. Actual data type before encryption: text.
*   `support_needed_details_ciphertext` (`text`): Encrypted field storing support needs details. Actual data type before encryption: text.
*   `close_relationships_details_ciphertext` (`text`): Encrypted field storing close relationships details. Actual data type before encryption: text.
*   `further_instructions_ciphertext` (`text`): Encrypted field storing further instructions. Actual data type before encryption: text.
*   `rejection_reasons_ciphertext` (`text`): Encrypted field storing rejection reasons. Actual data type before encryption: text.
*   `gaps_in_employment_details_ciphertext` (`text`): Encrypted field storing employment gap details. Actual data type before encryption: text.
*   `in_progress_steps` (`integer[]`): `default: []`, `NOT NULL`, `array: true`. Tracks application steps currently in progress.
*   `safeguarding_issue_details` (`text`): Details of any safeguarding issues declared.
*   `imported_steps` (`integer[]`): `default: []`, `NOT NULL`, `array: true`. Tracks steps imported from jobseeker profile.
*   `interviewing_at` (`datetime`): Timestamp for interview if scheduled.
*   `statutory_induction_complete_details` (`string`): Details if statutory induction is complete.
*   `following_religion` (`boolean`): Indicates if the applicant follows a religion (for faith schools).
*   `religious_reference_type` (`integer`): Type of religious reference provided.
*   `faith_ciphertext` (`string`): Encrypted field storing faith details. Actual data type before encryption: string.
*   `place_of_worship_ciphertext` (`string`): Encrypted field storing place of worship. Actual data type before encryption: string.
*   `religious_referee_name_ciphertext` (`string`): Encrypted field storing religious referee's name. Actual data type before encryption: string.
*   `religious_referee_address_ciphertext` (`string`): Encrypted field storing religious referee's address. Actual data type before encryption: text.
*   `religious_referee_role_ciphertext` (`string`): Encrypted field storing religious referee's role. Actual data type before encryption: string.
*   `religious_referee_email_ciphertext` (`string`): Encrypted field storing religious referee's email. Actual data type before encryption: string.
*   `religious_referee_phone_ciphertext` (`string`): Encrypted field storing religious referee's phone. Actual data type before encryption: string.
*   `baptism_address_ciphertext` (`string`): Encrypted field storing baptism address. Actual data type before encryption: text.
*   `baptism_date_ciphertext` (`string`): Encrypted field storing baptism date. Actual data type before encryption: string.
*   `ethos_and_aims_ciphertext` (`string`): Encrypted field storing ethos and aims statement. Actual data type before encryption: text.
*   `working_patterns` (`integer[]`): `array: true`. Jobseeker's preferred working patterns for this application.
*   `working_pattern_details` (`string`): Details about working pattern preferences.
*   `qts_age_range_and_subject` (`string`): QTS age range and subject details.
*   `is_statutory_induction_complete` (`boolean`): Is statutory induction complete.
*   `is_support_needed` (`boolean`): Is support needed.
*   `has_close_relationships` (`boolean`): Any close relationships.
*   `has_right_to_work_in_uk` (`boolean`): Right to work in UK status.
*   `has_safeguarding_issue` (`boolean`): Any safeguarding issues declared.

#### 6.2.6 Jobseeker Profiles (`jobseeker_profiles`)
Stores extended profile information for jobseekers.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `jobseeker_id` (`uuid`): `NOT NULL`, `indexed unique`. Foreign key to `jobseekers.id`.
*   `about_you` (`string`): A short bio or summary about the jobseeker.
*   `qualified_teacher_status` (`integer`): Jobseeker's QTS status.
*   `qualified_teacher_status_year` (`string`): Year QTS was obtained.
*   `active` (`boolean`): `default: false`, `NOT NULL`. Indicates if the profile is active and potentially visible to publishers.
*   `requested_hidden_profile` (`boolean`): If the jobseeker has requested their profile to be hidden.
*   `teacher_reference_number_ciphertext` (`text`): Encrypted field storing TRN. Actual data type before encryption: string.
*   `statutory_induction_complete_details` (`string`): Details if statutory induction is complete.
*   `qts_age_range_and_subject` (`string`): QTS age range and subject details.
*   `qualified_teacher_status_details` (`text`): Further details about QTS.
*   `is_statutory_induction_complete` (`boolean`): Is statutory induction complete.

#### 6.2.7 Job Preferences (`job_preferences`)
Stores job preferences for jobseekers, used for job alerts and potentially tailoring search.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `roles` (`string[]`): `default: []`, `array: true`. Preferred job roles.
*   `phases` (`string[]`): `default: []`, `array: true`. Preferred education phases.
*   `key_stages` (`string[]`): `default: []`, `array: true`. Preferred key stages.
*   `subjects` (`string[]`): `default: []`, `array: true`. Preferred subjects.
*   `working_patterns` (`string[]`): `default: []`, `array: true`. Preferred working patterns.
*   `completed_steps` (`json`): `default: {}`. Tracks completed steps in the preference builder form.
*   `builder_completed` (`boolean`): `default: false`, `NOT NULL`. Indicates if the preference builder was completed.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `jobseeker_profile_id` (`uuid`): `NOT NULL`, `indexed unique`. Foreign key to `jobseeker_profiles.id`.
*   `working_pattern_details` (`string`): Additional details about working pattern preferences.

#### 6.2.8 Job Preferences Locations (`job_preferences_locations`)
Stores preferred locations and radii for job preferences.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `job_preferences_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `job_preferences.id`.
*   `name` (`string`): `NOT NULL`. Name of the location (e.g., town, postcode).
*   `radius` (`integer`): `NOT NULL`. Search radius around the location in miles.
*   `area` (`geography`): `limit: {srid: 4326, type: "geometry", geographic: true}`, `NOT NULL`, `indexed (gist)`. Geographic area representing the location preference.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.9 Employments (`employments`)
Stores employment history for job applications or jobseeker profiles.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `subjects` (`string`): `default: ""`, `NOT NULL`. Subjects taught or related to the role.
*   `started_on` (`date`): Start date of employment.
*   `ended_on` (`date`): End date of employment.
*   `job_application_id` (`uuid`): `indexed`. Foreign key to `job_applications.id` (if part of an application).
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `organisation_ciphertext` (`text`): Encrypted field storing organisation name. Actual data type before encryption: string.
*   `job_title_ciphertext` (`text`): Encrypted field storing job title. Actual data type before encryption: string.
*   `main_duties_ciphertext` (`text`): Encrypted field storing main duties. Actual data type before encryption: text.
*   `employment_type` (`integer`): `default: 0`. Type of employment (e.g., job, break).
*   `reason_for_break` (`text`): `default: ""`. Reason for a break in employment.
*   `jobseeker_profile_id` (`uuid`): `indexed`. Foreign key to `jobseeker_profiles.id` (if part of a profile).
*   `reason_for_leaving` (`text`): Reason for leaving the employment.
*   `is_current_role` (`boolean`): `default: false`, `NOT NULL`. Indicates if this is the current role.

#### 6.2.10 Qualifications (`qualifications`)
Stores educational qualifications for job applications or jobseeker profiles.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `category` (`integer`): Category of qualification (e.g., degree, A-level, GCSE).
*   `finished_studying` (`boolean`): Indicates if studying for this qualification is finished.
*   `grade` (`string`): `default: ""`, `NOT NULL`. Grade obtained.
*   `institution` (`string`): `default: ""`, `NOT NULL`. Awarding institution.
*   `name` (`string`): `default: ""`, `NOT NULL`. Name of the qualification.
*   `subject` (`string`): `default: ""`, `NOT NULL`. Subject of the qualification.
*   `year` (`integer`): Year qualification was obtained/expected.
*   `job_application_id` (`uuid`): `indexed`. Foreign key to `job_applications.id`.
*   `finished_studying_details_ciphertext` (`text`): Encrypted field storing details if not finished studying. Actual data type before encryption: text.
*   `jobseeker_profile_id` (`uuid`): `indexed`. Foreign key to `jobseeker_profiles.id`.
*   `awarding_body` (`string`): Name of the awarding body.
*   `month` (`integer`): Month qualification was obtained/expected.

#### 6.2.11 Qualification Results (`qualification_results`)
Stores individual subject results for a qualification (e.g. GCSE subjects and grades).
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `qualification_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `qualifications.id`.
*   `subject` (`string`): `NOT NULL`. Subject name.
*   `grade` (`string`): `NOT NULL`. Grade achieved for the subject.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `awarding_body` (`string`): Awarding body for this specific result (if different from parent qualification).

#### 6.2.12 Personal Details (`personal_details`)
Stores personal details as part of a Jobseeker's profile.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `jobseeker_profile_id` (`uuid`): `NOT NULL`, `indexed unique`. Foreign key to `jobseeker_profiles.id`.
*   `phone_number_provided` (`boolean`): Flag indicating if phone number is provided.
*   `completed_steps` (`json`): `default: {}`, `NOT NULL`. Tracks completed steps in the personal details form.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `first_name_ciphertext` (`text`): Encrypted field storing first name. Actual data type before encryption: string.
*   `last_name_ciphertext` (`text`): Encrypted field storing last name. Actual data type before encryption: string.
*   `phone_number_ciphertext` (`text`): Encrypted field storing phone number. Actual data type before encryption: string.
*   `has_right_to_work_in_uk` (`boolean`): Indicates if the jobseeker has the right to work in the UK.

#### 6.2.13 References (`references`)
Stores referee details for job applications.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `relationship` (`string`): `default: ""`, `NOT NULL`. Relationship of the referee to the applicant.
*   `job_application_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `job_applications.id`.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `name_ciphertext` (`text`): Encrypted field storing referee's name. Actual data type before encryption: string.
*   `job_title_ciphertext` (`text`): Encrypted field storing referee's job title. Actual data type before encryption: string.
*   `organisation_ciphertext` (`text`): Encrypted field storing referee's organisation. Actual data type before encryption: string.
*   `email_ciphertext` (`text`): Encrypted field storing referee's email. Actual data type before encryption: string.
*   `phone_number_ciphertext` (`text`): Encrypted field storing referee's phone number. Actual data type before encryption: string.
*   `is_most_recent_employer` (`boolean`): Indicates if this referee is from the most recent employer.

#### 6.2.14 Subscriptions (Job Alerts) (`subscriptions`)
Stores job alert subscriptions created by jobseekers or anonymous users.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `email` (`string`): `indexed`. Email address for receiving alerts.
*   `frequency` (`integer`): Frequency of alerts (e.g., daily).
*   `search_criteria` (`jsonb`): Stores the search criteria for the alert as JSON.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `recaptcha_score` (`float`): Score from reCAPTCHA validation.
*   `active` (`boolean`): `default: true`. Indicates if the subscription is active.
*   `unsubscribed_at` (`datetime`): `precision: nil`. Timestamp when unsubscribed.

#### 6.2.15 Alert Runs (`alert_runs`)
Tracks the execution of job alerts.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `subscription_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `subscriptions.id`.
*   `run_on` (`date`): `indexed`. Date the alert was run.
*   `job_id` (`string`): Identifier for the background job that processed the alert.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `status` (`integer`): `default: 0`. Status of the alert run (e.g., processed, failed).

#### 6.2.16 Feedbacks (`feedbacks`)
Stores user feedback on various aspects of the service.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.
*   `feedback_type` (`integer`): Type of feedback (e.g., general, job_alert, vacancy).
*   `rating` (`integer`): User rating (e.g., 1-5 stars).
*   `comment` (`text`): User's textual feedback.
*   `recaptcha_score` (`float`): Score from reCAPTCHA validation.
*   `relevant_to_user` (`boolean`): If the feedback context was relevant to the user.
*   `search_criteria` (`jsonb`): Search criteria at the time of feedback, if applicable.
*   `job_alert_vacancy_ids` (`uuid[]`): `array: true`. Vacancy IDs related to job alert feedback.
*   `unsubscribe_reason` (`integer`): Reason for unsubscribing from alerts.
*   `other_unsubscribe_reason_comment` (`text`): Comment for 'other' unsubscribe reason.
*   `email` (`string`): User's email, if provided.
*   `user_participation_response` (`integer`): Response to user research participation request.
*   `visit_purpose` (`integer`): Purpose of the user's visit.
*   `visit_purpose_comment` (`text`): Comment for 'other' visit purpose.
*   `job_application_id` (`uuid`): `indexed`. Foreign key to `job_applications.id`, if feedback relates to an application.
*   `jobseeker_id` (`uuid`): `indexed`. Foreign key to `jobseekers.id`, if feedback from a jobseeker.
*   `publisher_id` (`uuid`): `indexed`. Foreign key to `publishers.id`, if feedback from a publisher.
*   `subscription_id` (`uuid`): `indexed`. Foreign key to `subscriptions.id`, if feedback relates to a job alert.
*   `vacancy_id` (`uuid`): `indexed`. Foreign key to `vacancies.id`, if feedback relates to a vacancy.
*   `close_account_reason` (`integer`): Reason for closing an account.
*   `close_account_reason_comment` (`text`): Comment for 'other' account closure reason.
*   `category` (`string`): Feedback category (e.g., general, vacancy, job alert).
*   `occupation` (`text`): User's occupation (for survey-type feedback).
*   `origin_path` (`string`): The path from which the feedback was submitted.
*   `job_found_unsubscribe_reason_comment` (`text`): Comment if unsubscribed because job was found.

#### 6.2.17 Notes (`notes`)
Stores internal notes, typically made by publishers on job applications.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `content` (`string`): Text content of the note.
*   `publisher_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `publishers.id`.
*   `job_application_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `job_applications.id`.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.18 Equal Opportunities Reports (`equal_opportunities_reports`)
Stores aggregated, anonymized equal opportunities data for vacancies.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `vacancy_id` (`uuid`): `NOT NULL`, `indexed unique`. Foreign key to `vacancies.id`.
*   `total_submissions` (`integer`): `default: 0`, `NOT NULL`. Total applications contributing to this report.
*   `disability_no` (`integer`): `default: 0`, `NOT NULL`. Count for 'no' disability.
*   `disability_prefer_not_to_say` (`integer`): `default: 0`, `NOT NULL`. Count for 'prefer not to say' disability.
*   `disability_yes` (`integer`): `default: 0`, `NOT NULL`. Count for 'yes' disability.
*   *(Numerous other columns for gender, orientation, ethnicity, religion, and age categories, all `integer`, `default: 0`, `NOT NULL`.)*
*   `gender_other_descriptions` (`string[]`): `default: []`, `NOT NULL`, `array: true`. Array of 'other' gender descriptions.
*   `orientation_other_descriptions` (`string[]`): `default: []`, `NOT NULL`, `array: true`. Array of 'other' orientation descriptions.
*   `ethnicity_other_descriptions` (`string[]`): `default: []`, `NOT NULL`, `array: true`. Array of 'other' ethnicity descriptions.
*   `religion_other_descriptions` (`string[]`): `default: []`, `NOT NULL`, `array: true`. Array of 'other' religion descriptions.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.19 Organisation Vacancies (`organisation_vacancies`)
Join table linking vacancies to one or more organisations (e.g., schools within a MAT).
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `organisation_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id`.
*   `vacancy_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `vacancies.id`.
*   `created_at` (`datetime`): Timestamp of creation.
*   `updated_at` (`datetime`): Timestamp of last update.
*   _Indexes ensure uniqueness of `organisation_id` and `vacancy_id` pairs._

#### 6.2.20 Organisation Publishers (`organisation_publishers`)
Join table linking publishers to the organisations they belong to.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `organisation_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id`.
*   `publisher_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `publishers.id`.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.21 School Group Memberships (`school_group_memberships`)
Join table linking schools to school groups (e.g., MATs).
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `school_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id` (where type is School).
*   `school_group_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id` (where type is SchoolGroup).
*   `do_not_delete` (`boolean`): Flag to prevent deletion.
*   `created_at` (`datetime`): Timestamp of creation.
*   `updated_at` (`datetime`): Timestamp of last update.
*   _Indexes ensure uniqueness of `school_id` and `school_group_id` pairs._

#### 6.2.22 Local Authority Publisher Schools (`local_authority_publisher_schools`)
Join table linking publisher preferences (specifically for LAs) to individual schools.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `publisher_preference_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `publisher_preferences.id`.
*   `school_id` (`uuid`): `NOT NULL`. Foreign key to `organisations.id` (representing a school).
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.23 Publisher Preferences (`publisher_preferences`)
Stores preferences for publishers, particularly which organisation(s) they are acting on behalf of.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `publisher_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `publishers.id`.
*   `organisation_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id`.
*   `created_at` (`datetime`): Timestamp of creation.
*   `updated_at` (`datetime`): Timestamp of last update.

#### 6.2.24 Markers (`markers`)
Stores geographic points for vacancies associated with organisations, likely for map displays.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `vacancy_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `vacancies.id`.
*   `organisation_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `organisations.id`.
*   `geopoint` (`geography`): `limit: {srid: 4326, type: "st_point", geographic: true}`, `indexed (gist)`. Geographic coordinates.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.25 Location Polygons (`location_polygons`)
Stores geographic polygon data for locations (e.g., counties, cities), used in location-based search.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `name` (`string`): `NOT NULL`, `indexed`. Name of the location (e.g., "Essex").
*   `location_type` (`string`): Type of location (e.g., "county").
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `area` (`geography`): `limit: {srid: 4326, type: "geometry", geographic: true}`, `indexed (gist)`. The polygon data.
*   `centroid` (`geography`): `limit: {srid: 4326, type: "st_point", geographic: true}`, `indexed (gist)`. Centroid of the polygon.

#### 6.2.26 Emergency Login Keys (`emergency_login_keys`)
Stores temporary keys for emergency login, associated polymorphically with Jobseekers or Publishers.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `not_valid_after` (`datetime`): `precision: nil`, `NOT NULL`. Expiry timestamp for the key.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`. Timestamp of last update.
*   `owner_type` (`string`): `NOT NULL`, `indexed with owner_id`. Polymorphic owner type (e.g., "Jobseeker", "Publisher").
*   `owner_id` (`uuid`): `NOT NULL`, `indexed with owner_type`. Polymorphic owner ID.

#### 6.2.27 Publisher ATS API Clients (`publisher_ats_api_clients`)
Stores credentials for third-party Applicant Tracking Systems using the Publisher API.
*   `id` (`uuid`): `NOT NULL`, `default: -> { "gen_random_uuid()" }`. Primary key.
*   `name` (`string`): `NOT NULL`. Name of the API client/provider.
*   `api_key` (`string`): `NOT NULL`. The API key for authentication.
*   `last_rotated_at` (`datetime`): `NOT NULL`. Timestamp when the API key was last rotated.
*   `created_at` (`datetime`): `NOT NULL`. Timestamp of creation.
*   `updated_at` (`datetime`): `NOT NULL`. Timestamp of last update.

#### 6.2.28 Active Storage Attachments (`active_storage_attachments`)
Generic Rails Active Storage table for managing file attachments.
*   **Purpose:** Links records (e.g., Vacancy, JobApplication, JobseekerProfile) to their uploaded files (blobs).
*   `id` (`uuid`): Primary key.
*   `name` (`string`): `NOT NULL`. Name of the attachment (e.g., `supporting_documents`, `cv`).
*   `record_type` (`string`): `NOT NULL`, `indexed with record_id, name, blob_id`. The class name of the owning record (e.g., "Vacancy", "JobApplication").
*   `record_id` (`uuid`): `NOT NULL`, `indexed with record_type, name, blob_id`. The ID of the owning record.
*   `blob_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `active_storage_blobs.id`.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`.

#### 6.2.29 Active Storage Blobs (`active_storage_blobs`)
Generic Rails Active Storage table for storing metadata about uploaded files.
*   **Purpose:** Stores metadata about each uploaded file (blob), such as filename, content type, and checksum. The actual file content is stored in the configured service (e.g., Azure Blob Storage).
*   `id` (`uuid`): Primary key.
*   `key` (`string`): `NOT NULL`, `indexed unique`. Unique key for the blob.
*   `filename` (`string`): `NOT NULL`. Original filename of the uploaded file.
*   `content_type` (`string`): MIME type of the file.
*   `metadata` (`text`): Additional metadata (e.g., image dimensions).
*   `service_name` (`string`): `NOT NULL`. Name of the Active Storage service used (e.g., `azure`).
*   `byte_size` (`bigint`): `NOT NULL`. Size of the file in bytes.
*   `checksum` (`string`): Checksum of the file content.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`.

#### 6.2.30 Friendly ID Slugs (`friendly_id_slugs`)
Generic table used by the `friendly_id` gem to create human-readable URL slugs for records (e.g., Vacancies, Organisations).
*   **Purpose:** Stores historical and current slugs for records to maintain SEO-friendly URLs.
*   `id` (`serial`): Primary key (integer).
*   `slug` (`string`): `NOT NULL`, `indexed`. The URL-friendly slug.
*   `sluggable_id` (`uuid`): `NOT NULL`, `indexed`. The ID of the record that owns the slug.
*   `sluggable_type` (`string`): `limit: 50`, `indexed`. The class name of the record that owns the slug (e.g., "Vacancy").
*   `scope` (`string`): `indexed`. Used if slugs are scoped (e.g., unique within a parent).
*   `created_at` (`datetime`): `precision: nil`.

#### 6.2.31 Sessions (`sessions`)
Standard Rails table for storing user session data.
*   **Purpose:** Manages user sessions for the web application.
*   `id` (`serial`): Primary key (integer).
*   `session_id` (`string`): `NOT NULL`, `indexed unique`. Unique ID for the session.
*   `data` (`text`): Serialized session data.
*   `created_at` (`datetime`): `precision: nil`, `NOT NULL`.
*   `updated_at` (`datetime`): `precision: nil`, `NOT NULL`, `indexed`.

#### 6.2.32 Versions (`versions`)
Used by the `paper_trail` gem for tracking changes to models (auditing/versioning).
*   **Purpose:** Stores historical versions of records (e.g., Vacancies) to enable auditing and rollbacks.
*   `id` (`serial`): Primary key (integer).
*   `item_type` (`string`): `NOT NULL`, `indexed with item_id`. The class name of the versioned item.
*   `item_id` (`uuid`): `NOT NULL`, `indexed with item_type`. The ID of the versioned item.
*   `event` (`string`): `NOT NULL`. The event that triggered the version (e.g., "create", "update", "destroy").
*   `whodunnit` (`string`): Identifier for the user who made the change.
*   `object` (`text`): Serialized representation of the item's state *before* the change.
*   `created_at` (`datetime`): `precision: nil`. Timestamp of when the version was created.
*   `object_changes` (`jsonb`): Stores a JSON representation of the changes made in this version.

#### 6.2.33 Noticed Events (`noticed_events`)
Part of the `noticed` gem, used for managing notifications.
*   **Purpose:** Stores records of events that can trigger notifications.
*   `id` (`uuid`): Primary key.
*   `type` (`string`): Class name of the event.
*   `record_type` (`string`): `indexed with record_id`. Polymorphic association to the record that caused the event (e.g., a new `JobApplication`).
*   `record_id` (`uuid`): `indexed with record_type`. ID of the record that caused the event.
*   `params` (`jsonb`): Additional parameters associated with the event.
*   `created_at` (`datetime`): `NOT NULL`.
*   `updated_at` (`datetime`): `NOT NULL`.
*   `notifications_count` (`integer`): Counter cache for associated notifications.

#### 6.2.34 Noticed Notifications (`noticed_notifications`)
Part of the `noticed` gem, stores individual notifications generated by events.
*   **Purpose:** Represents a specific notification sent to a recipient.
*   `id` (`uuid`): Primary key.
*   `type` (`string`): Class name of the notification.
*   `event_id` (`uuid`): `NOT NULL`, `indexed`. Foreign key to `noticed_events.id`.
*   `recipient_type` (`string`): `NOT NULL`, `indexed with recipient_id`. Polymorphic association to the recipient (e.g., "Jobseeker", "Publisher").
*   `recipient_id` (`uuid`): `NOT NULL`, `indexed with recipient_type`. ID of the recipient.
*   `read_at` (`datetime`): `precision: nil`. Timestamp when the notification was marked as read.
*   `seen_at` (`datetime`): `precision: nil`. Timestamp when the notification was seen (e.g., displayed in a UI).
*   `created_at` (`datetime`): `NOT NULL`.
*   `updated_at` (`datetime`): `NOT NULL`.

### 6.3 Data Retention and Archival
*(This section will detail policies and requirements for data retention, archival, and deletion, in compliance with GDPR and DfE policies.)*

## 7. Use Cases

### 7.1 UC-001: Jobseeker Searches for Vacancy
### 7.2 UC-002: Publisher Posts New Vacancy
### 7.3 UC-003: Jobseeker Applies for Vacancy
### 7.4 UC-004: Jobseeker Creates Job Alert
  - *(Others to be added)*
- [Appendix A: Business Rules Catalogue (Placeholder)](#appendix-a-business-rules-catalogue-placeholder)
- [Appendix B: Data Migration Considerations (Placeholder for Rewrite Context)](#appendix-b-data-migration-considerations-placeholder-for-rewrite-context)
