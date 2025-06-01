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
    - [6.2.1 Vacancy](#621-vacancy)
    - [6.2.2 Jobseeker / User](#622-jobseeker--user)
    - [6.2.3 Publisher / User](#623-publisher--user)
    - [6.2.4 Organisation (School, Trust, LA)](#624-organisation-school-trust-la)
    - [6.2.5 JobApplication](#625-jobapplication)
    - [6.2.6 Subscription (Job Alert)](#626-subscription-job-alert)
    - [6.2.7 JobseekerProfile](#627-jobseekerprofile)
    - *(Others to be added as identified)*
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
*   **Support/Admin Specific Requirements:** (To be detailed in a subsequent step)

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
*   **Key Capabilities (High-Level):** (Support-focused, to be detailed in a subsequent step)
    *   Access to a support dashboard...
    *   ...

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
### 6.2 Detailed Data Dictionary (Key Entities)
    - [6.2.1 Vacancy](#621-vacancy)
    - [6.2.2 Jobseeker / User](#622-jobseeker--user)
    - [6.2.3 Publisher / User](#623-publisher--user)
    - [6.2.4 Organisation (School, Trust, LA)](#624-organisation-school-trust-la)
    - [6.2.5 JobApplication](#625-jobapplication)
    - [6.2.6 Subscription (Job Alert)](#626-subscription-job-alert)
    - [6.2.7 JobseekerProfile](#627-jobseekerprofile)
    - *(Others to be added as identified)*
### 6.3 Data Retention and Archival

## 7. Use Cases

### 7.1 UC-001: Jobseeker Searches for Vacancy
### 7.2 UC-002: Publisher Posts New Vacancy
### 7.3 UC-003: Jobseeker Applies for Vacancy
### 7.4 UC-004: Jobseeker Creates Job Alert
  - *(Others to be added)*
- [Appendix A: Business Rules Catalogue (Placeholder)](#appendix-a-business-rules-catalogue-placeholder)
- [Appendix B: Data Migration Considerations (Placeholder for Rewrite Context)](#appendix-b-data-migration-considerations-placeholder-for-rewrite-context)
