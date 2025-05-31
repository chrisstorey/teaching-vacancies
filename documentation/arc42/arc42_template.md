# arc42 Documentation Template for Teaching Vacancies

## 1. Introduction and Goals

### 1.1 Requirements Overview

Teaching Vacancies is a free job-listing service from the Department for Education (DfE) for schools in England. The primary goal of the service is to connect teachers with suitable job opportunities and to help schools and trusts find qualified candidates for their vacancies.

The system allows:
*   **Jobseekers (Teachers):** To search for jobs, view job details, save jobs, set up job alerts, and apply for vacancies.
*   **Publishers (Schools and Trusts):** To post job vacancies, manage their listings, and view applications (if using the in-platform application feature).

The service aims to provide a user-friendly and efficient platform for both Jobseekers and Publishers in the education sector.

### 1.2 Quality Goals

*   **Usability:** The system should be intuitive and easy to use for both Jobseekers and Publishers with varying levels of technical proficiency.
*   **Accessibility:** The platform must adhere to accessibility standards (e.g., WCAG) to ensure it can be used by people with disabilities.
*   **Reliability:** The service should be highly available and perform consistently, especially during peak usage times.
*   **Performance:** Job searches and page loads should be fast to provide a good user experience.
*   **Security:** User data and application integrity must be protected through robust security measures.
*   **Maintainability:** The codebase should be well-structured, documented, and testable to facilitate ongoing development and maintenance.

### 1.3 Stakeholders

*   **Department for Education (DfE):** The primary stakeholder and owner of the service.
*   **Teachers (Jobseekers):** Users looking for teaching positions in England.
*   **Schools and Trusts (Publishers/Employers):** Organizations posting job vacancies.
*   **Development Team:** Responsible for building, maintaining, and improving the service.
*   **Support Team:** Assists users and manages operational aspects of the service.
*   **Department for Work and Pensions (DWP):** Integration partner for wider job dissemination via the "Find a job" service.

## 2. Constraints

### 2.1 Technical Constraints

*   **Hosting Platform:** The service is hosted on the Azure Cloud Infrastructure Platform (CIP) using Azure Kubernetes Service (AKS). This is a DfE-wide platform decision.
*   **Primary Technology Stack:** The application is built using Ruby on Rails.
*   **Database:** PostgreSQL is the primary database, with Redis used for caching and background job processing.
*   **Authentication - Hiring Staff:** Must integrate with DfE Sign-in for authenticating Publishers.
*   **Authentication - Jobseekers:** Must integrate with GOV.UK One Login for authenticating Jobseekers.
*   **External Integration - DWP:** Job vacancies must be exported to the Department for Work and Pensions (DWP) "Find a job" service via a daily XML bulk upload to their SFTP server.
*   **External Integration - ATS API:** The system provides a Publisher ATS API to allow Applicant Tracking Systems to post and manage vacancies. This API must be maintained and documented.
*   **Security Standards:** Must comply with DfE security policies and general web application security best practices (e.g., OWASP Top 10).
*   **Accessibility Standards:** The service must meet WCAG (Web Content Accessibility Guidelines) standards, likely Level AA.
*   **GOV.UK Design System:** The user interface should adhere to the GOV.UK Design System for consistency and usability.
*   **Virus Scanning:** Uploaded documents must be scanned for viruses, currently implemented via Google Drive API.
*   **Bot Mitigation:** Mechanisms like Google reCAPTCHA v3 are used to protect public forms from spam and abuse.

### 2.2 Organizational Constraints

*   **Team Structure:** Development, support, and operations are handled by specific teams within DfE or contracted to DfE.
*   **Deployment Process:** Deployments are managed through GitHub Actions, with specific workflows for different environments (Review, QA, Staging, Production).
*   **Change Management:** Changes to production systems likely follow DfE's change management processes.
*   **Budget and Resources:** Development and operational activities are subject to DfE's budgeting and resource allocation.
*   **Service Level Agreements (SLAs):** While not explicitly detailed in the reviewed documents, SLAs for uptime and support response times are likely to exist.
*   **Open Source:** The project is open source (as evidenced by the GitHub repository and LICENSE file), which may influence technology choices and contribution processes.

### 2.3 Conventions

*   **Version Control:** Git is used for version control, with the repository hosted on GitHub.
*   **Documentation:** Existing documentation is primarily in Markdown, located within the `/documentation` directory. Architectural Decision Records (ADRs) are used to document significant decisions.
*   **Testing:** The project includes various levels of testing (unit, integration, system tests), managed via RSpec and other tools.
*   **Infrastructure as Code:** Terraform is used for managing infrastructure components.
*   **Agile Development:** While not explicitly stated, the use of CI/CD, PRs, and iterative development suggests an Agile-like methodology.

## 3. Context and Scope

### 3.1 Business Context

The Teaching Vacancies service operates within the UK's education sector, specifically for England. It serves as a national platform provided by the Department for Education (DfE) to:
*   Help schools and trusts advertise their vacancies effectively and reduce recruitment costs.
*   Provide teachers and other Jobseekers with a comprehensive and easy-to-use portal for finding educational job opportunities.
*   Streamline the application process where possible.
*   Integrate with other government services to provide a cohesive experience for users (e.g., DfE Sign-in, GOV.UK One Login) and to disseminate job information more widely (e.g., DWP Find a Job).

The service supports various types of educational institutions, including individual schools, academies, multi-academy trusts (MATs), and local authorities.

### 3.2 Technical Context

The system is a web-based application developed primarily with Ruby on Rails. It interacts with several external systems for authentication, data exchange, and utility services. Key technical aspects of its context include:
*   **User Interfaces:** Web interface for Jobseekers and Publishers. A separate interface for Support Users.
*   **Data Storage:** PostgreSQL Database for primary application data, Redis for caching and session management.
*   **Integrations:** Real-time and batch integrations with various DfE and third-party services.
*   **Deployment:** Hosted on Azure Kubernetes Service (AKS), managed via Terraform, and deployed through CI/CD pipelines (GitHub Actions).
*   **Security:** Relies on external identity providers (DfE Sign-in, GOV.UK One Login) and implements measures like reCAPTCHA and virus scanning.

### 3.3 External Interfaces

The Teaching Vacancies service interacts with the following external systems and actors:

![System Context Diagram](c4_system_context.puml)
*(The diagram is defined in `documentation/arc42/c4_system_context.puml`)*

*   **Jobseeker (Person):** Searches for jobs, manages their profile, sets up job alerts, and applies for vacancies. Interacts via the main Web Application.
*   **Publisher (Person):** Represents a school or trust. Posts and manages job vacancies, and may review applications. Interacts via the main Web Application, authenticated by DfE Sign-in.
*   **Support User (Person):** DfE staff providing user support, monitoring feedback, and managing certain operational aspects. Interacts via a dedicated support interface.
*   **System Administrator (Person):** DfE staff responsible for system configuration, maintenance, and deeper operational tasks.
*   **DfE Sign-in (External System):** Provides authentication and identity management for Publishers. Teaching Vacancies relies on it to verify the identity of school staff.
    *   *Interaction:* OAuth 2.0 for authentication.
*   **GOV.UK One Login (External System):** Provides authentication and identity management for Jobseekers.
    *   *Interaction:* OpenID Connect for authentication.
*   **DWP Find a Job (External System):** The UK government's national job search platform. Teaching Vacancies pushes vacancy information to this service.
    *   *Interaction:* Daily XML bulk uploads via SFTP.
*   **Applicant Tracking Systems (ATS) (External System):** Used by some schools/trusts to manage their recruitment pipeline. Teaching Vacancies provides a Publisher ATS API for these systems to post and manage vacancies.
    *   *Interaction:* HTTPS/JSON via the Publisher ATS API.
*   **Google Services (External System):**
    *   **Google Drive:** Used for temporary storage and virus scanning of uploaded documents.
        *   *Interaction:* HTTPS/API.
    *   **reCAPTCHA:** Used to protect public forms (e.g., subscriptions, feedback) from bot abuse.
        *   *Interaction:* JavaScript integration and server-side API calls.
*   **GOV.UK Notify (External System):** A DfE/government service for sending emails, SMS, and letters. Teaching Vacancies uses it for sending notifications like job alerts and application confirmations.
    *   *Interaction:* HTTPS/API.
*   **ONS ArcGIS (External System):** Office for National Statistics service that provides geographical polygon data. Used by Teaching Vacancies to support location-based searches (e.g., "jobs in Essex").
    *   *Interaction:* HTTPS/API for data import.

## 4. Solution Strategy

### 4.1 Technical Decisions

The Teaching Vacancies service is built as a monolithic Ruby on Rails application. This approach was likely chosen for its rapid development capabilities, strong conventions, and extensive ecosystem of libraries (gems) that can accelerate development.

Key technical decisions influencing the solution include:
*   **Web Framework:** Ruby on Rails is the core framework, dictating much of the architecture (MVC pattern, ActiveRecord for ORM, etc.).
*   **Database:** PostgreSQL was chosen for its robustness, support for geospatial queries (via PostGIS), and full-text search capabilities.
*   **Frontend:** Server-rendered views (Slim templating engine) are augmented with JavaScript (StimulusJS) for dynamic UI elements. Adherence to GOV.UK Design System components is a priority.
*   **Background Jobs:** Sidekiq is used for processing asynchronous tasks like sending email alerts and importing data from external sources.
*   **Authentication:** Leverages established government identity platforms (DfE Sign-in for Publishers, GOV.UK One Login for Jobseekers) rather than building a bespoke authentication system. This enhances security and user trust.
*   **Search:** Initially used external services like Elasticsearch/Algolia, but later migrated to PostgreSQL's built-in full-text search and PostGIS for location-based searches (ADR #0010). This simplifies the tech stack and reduces external dependencies.
*   **Integrations:** A combination of API integrations (ATS API, Google Services, GOV.UK Notify) and batch processing (DWP Find a Job SFTP) is used to interact with external systems.

### 4.2 System Decomposition

As a monolithic application, the primary decomposition is along the lines of the Model-View-Controller (MVC) pattern inherent in Ruby on Rails:
*   **Models (`app/models`):** Represent business entities (e.g., Vacancy, Jobseeker, Organisation) and encapsulate data logic and business rules. ActiveRecord is used for ORM.
*   **Views (`app/views`):** Responsible for presenting data to the user. Slim is the primary templating language. ViewComponents are used for reusable UI elements.
*   **Controllers (`app/controllers`):** Handle incoming web requests, interact with models to retrieve or modify data, and select appropriate views to render.
*   **Services (`app/services`):** Encapsulate complex business logic or interactions with external services, keeping controllers and models leaner.
*   **Forms (`app/form_models`):** Handle form validation and data processing for complex forms, often used in multi-step processes.
*   **Jobs (`app/jobs`):** Define background tasks processed by Sidekiq (e.g., sending emails, data synchronization).
*   **Mailers (`app/mailers`):** Manage the creation and sending of emails, often in conjunction with GOV.UK Notify.
*   **Assets (`app/assets`):** Include stylesheets (SCSS), JavaScript (StimulusJS), and images.

While it's a monolith, logical separation exists for different user journeys (Jobseeker, Publisher, Support User) often handled by dedicated controllers and namespaces.

### 4.3 Fundamental Design Concepts

*   **Convention over Configuration:** Leveraging Ruby on Rails defaults and conventions to speed up development and ensure consistency.
*   **User-Centered Design:** Adherence to GOV.UK Design System principles and a focus on accessibility (WCAG) to ensure the service is usable by a wide range of users.
*   **Progressive Enhancement:** Using JavaScript for enhancing user experience while ensuring core functionality remains accessible without it.
*   **Asynchronous Operations:** Utilizing background jobs for tasks that could impact request-response times (e.g., email sending, data imports, analytics aggregation).
*   **Layered Security:** Relying on established IdPs for authentication, implementing bot mitigation, virus scanning for uploads, and following secure coding practices.
*   **Data-Driven Functionality:** Features like location-based search and landing pages are heavily reliant on data (geospatial data from ONS, configuration files).
*   **Iterative Development:** The presence of ADRs and a CI/CD pipeline suggests an iterative approach to development, allowing the system to evolve based on feedback and changing requirements.

## 5. Building Block View

This section describes the key architectural building blocks (containers in C4 model terminology) of the Teaching Vacancies system.

### 5.1 Level 1: System Containers

The Teaching Vacancies service is comprised of the following main containers:

![Container Diagram](c4_container_diagram.puml)
*(The diagram is defined in `documentation/arc42/c4_container_diagram.puml`)*

*   **Web Application (Container):**
    *   **Description:** The core of the system, implemented as a Ruby on Rails monolithic application.
    *   **Responsibilities:**
        *   Handling all incoming HTTP requests from users (Jobseekers, Publishers, Support Users).
        *   Implementing the business logic for all user stories.
        *   Rendering HTML user interfaces using server-side views (Slim templates with ViewComponents).
        *   Serving static assets (CSS, JavaScript, images).
        *   Interacting with the PostgreSQL Database for data persistence.
        *   Interacting with Redis for caching and session storage.
        *   Enqueuing background jobs into Sidekiq.
        *   Integrating with external services for authentication (DfE Sign-in, GOV.UK One Login), email sending (GOV.UK Notify), and other utilities (Google Services).
        *   Providing the Publisher ATS API endpoints.
    *   **Technology:** Ruby on Rails, Puma (web server).

*   **PostgreSQL Database (Container):**
    *   **Description:** A relational database storing all persistent data for the application.
    *   **Responsibilities:**
        *   Storing information about vacancies, job applications, user accounts (Jobseekers, Publishers), organisations (schools, trusts), subscriptions, feedback, etc.
        *   Managing data integrity through schemas, constraints, and relationships.
        *   Providing transaction capabilities.
        *   Supporting geospatial queries using the PostGIS extension (for location-based search).
        *   Supporting full-text search capabilities.
    *   **Technology:** PostgreSQL with PostGIS extension.

*   **Redis (Container):**
    *   **Description:** An in-memory data store.
    *   **Responsibilities:**
        *   Caching frequently accessed data to improve performance (e.g., page fragments, configuration data).
        *   Storing user session information.
        *   Acting as a message broker for Sidekiq, holding queues of background jobs to be processed.
    *   **Technology:** Redis.

*   **Sidekiq Processor (Container):**
    *   **Description:** A background job processing system that runs alongside the Web Application.
    *   **Responsibilities:**
        *   Executing long-running or asynchronous tasks outside of the web request-response cycle. Examples include:
            *   Sending email notifications and job alerts (via GOV.UK Notify).
            *   Importing data from external sources (e.g., ONS ArcGIS for polygons).
            *   Exporting data to external systems (e.g., DWP Find a Job via SFTP).
            *   Aggregating analytics data.
            *   Performing database maintenance tasks.
    *   **Technology:** Ruby, Sidekiq gem.

### 5.2 Level 2: Components (Illustrative for Web Application)

Given the monolithic nature of the Ruby on Rails **Web Application**, its internal components are primarily organized by Rails conventions (MVC). A detailed C4 Component diagram for the entire monolith would be extensive. However, we can describe its logical components:

*   **Authentication Components:**
    *   Handles integration with DfE Sign-in (for Publishers) and GOV.UK One Login (for Jobseekers).
    *   Manages session creation and termination.
    *   Includes fallback mechanisms for authentication if primary IdPs are unavailable.
*   **Vacancy Management Components:**
    *   Logic for creating, updating, publishing, and expiring vacancies. Handles vacancy lifecycle (e.g., draft, pending, published, expired, closed early).
    *   Includes logic for validating vacancy data against school/trust policies (e.g., salary ranges, required fields). Manages application deadlines and job start dates.
    *   Search and filtering functionality for vacancies (keyword, location, role, etc.).
    *   Integration with the Publisher ATS API.
*   **Jobseeker Profile & Application Components:**
    *   Manages Jobseeker profiles, saved jobs, and job alerts. Profile includes sections for personal details, qualifications, work history, job preferences (roles, phases, key stages, location).
    *   Handles the job application process (if applying directly on the platform). Application process involves steps like personal information, equal opportunities monitoring, declarations, and attaching supporting documents.
*   **Publisher/Organisation Components:**
    *   Manages organisation profiles (schools, trusts).
    *   Dashboard for Publishers to view their vacancies and applications.
*   **Geospatial Search Components:**
    *   Integrates with PostGIS.
    *   Handles location lookups (geocoding via Google Services or internal polygon data).
    *   Calculates distances and filters vacancies based on location and radius.
*   **Notification Components:**
    *   Manages the generation and sending of emails via GOV.UK Notify (e.g., job alerts, confirmation emails).
*   **External Integration Components:**
    *   Specific modules/services for interacting with DWP Find a Job, Google Drive (virus scan), etc.
*   **Support User Components:**
    *   Provides administrative interfaces for support staff (e.g., viewing feedback, managing users/data).

Further C4 Component diagrams could be developed for specific critical or complex areas if deeper analysis is required (e.g., the vacancy search mechanism or the job application process).

## 6. Runtime View

This section describes the interactions between the building blocks for important runtime scenarios.

### 6.1 Key Scenarios

#### Scenario 1: Jobseeker Searches for a Vacancy

1.  **Jobseeker Submits Search Request:**
    *   The Jobseeker enters search criteria (e.g., keyword "Maths Teacher", location "London", radius "10 miles") into the search form on the **Web Application**.
    *   The browser sends an HTTP GET request to the **Web Application**.

2.  **Web Application Handles Request:**
    *   The relevant Rails controller action receives the request.
    *   If a location is specified, the **Web Application** may:
        *   Query the **PostgreSQL Database** (PostGIS) for a known location polygon.
        *   If not a polygon search or if the location is not a known polygon, it may call an external geocoding service (e.g., Google Geocoding API via **Google Services**) to get coordinates for the location string. These results might be cached in **Redis** or **PostgreSQL Database**.
    *   The **Web Application** constructs a search query based on all criteria.

3.  **Database Executes Search:**
    *   The **Web Application** executes a query against the **PostgreSQL Database**. This query utilizes:
        *   Full-text search capabilities for keywords.
        *   PostGIS functions for geospatial filtering (e.g., `ST_DWithin` to find vacancies within the specified radius of the location coordinates or expanded polygon).
        *   Standard SQL clauses for other filters (job role, education phase, etc.).
        *   Business rule: Only 'published' and 'not expired' vacancies are returned in search results.

4.  **Web Application Processes Results:**
    *   The **PostgreSQL Database** returns a list of matching vacancies.
    *   The **Web Application** processes these results, potentially transforming data for display and paginating the results.
    *   Frequently accessed parts of the search results or filter options might be cached in **Redis** to speed up subsequent requests.

5.  **Web Application Renders Response:**
    *   The **Web Application** renders an HTML page displaying the search results.
    *   The browser displays the page to the Jobseeker.

*(Potential Sequence Diagram: A PlantUML sequence diagram could be added here later if more detail is needed on component interaction, e.g., `documentation/arc42/c4_runtime_jobseeker_search.puml`)*

```plantuml
@startuml JobseekerSearchScenario
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Sequence: Jobseeker Searches for Vacancy

actor Jobseeker
participant "Web Application" as WebApp
participant "PostgreSQL DB" as PostgreSQL
participant "Redis" as RedisCache
participant "Google Geocoding" as GoogleGeo

Jobseeker -> WebApp: Submits search (e.g., keyword, location)
activate WebApp

alt Location specified and not cached/polygon
    WebApp -> GoogleGeo: Get coordinates for location
    activate GoogleGeo
    GoogleGeo --> WebApp: Coordinates
    deactivate GoogleGeo
    WebApp -> RedisCache: Cache coordinates (optional)
end

WebApp -> PostgreSQL: Query vacancies (keywords, location filters, etc. \nOnly 'published' and 'not expired' vacancies)
activate PostgreSQL
PostgreSQL --> WebApp: Matching vacancies
deactivate PostgreSQL

WebApp -> RedisCache: Cache search results/facets (optional)

WebApp --> Jobseeker: Displays search results page
deactivate WebApp
@enduml
```
*(This diagram is illustrative. A more detailed one might be created as `documentation/arc42/c4_runtime_jobseeker_search.puml`)*


#### Scenario 2: Publisher Posts a New Vacancy

1.  **Publisher Authenticates:**
    *   The Publisher accesses the "Create Vacancy" section of the **Web Application**.
    *   If not already authenticated, the **Web Application** redirects the Publisher to **DfE Sign-in**. Publisher must belong to a recognized school or trust.
    *   **DfE Sign-in** authenticates the Publisher and redirects back to the **Web Application** with an authentication token.
    *   The **Web Application** validates the token and establishes a session for the Publisher, confirming their association with an organisation.

2.  **Publisher Submits Vacancy Details:**
    *   The Publisher fills out the multi-step vacancy creation form on the **Web Application**. Form includes details like job title, description, salary, contract type, application deadline, etc.
    *   Upon final submission, the browser sends an HTTP POST request to the **Web Application**.

3.  **Web Application Validates and Saves Vacancy:**
    *   The relevant Rails controller action receives the request.
    *   Form data is validated (using `app/form_models` or model validations), ensuring all mandatory fields are present and data types are correct. Business rules are applied (e.g., application deadline must be in the future, salary must be within expected range for role).
    *   If valid, the **Web Application** creates a new vacancy record in the **PostgreSQL Database** with a status of 'draft' or 'pending approval' depending on workflow.
    *   If documents are uploaded (e.g., job description), the **Web Application** might interact with **Google Services** (Google Drive) for virus scanning before associating them with the vacancy in the **PostgreSQL Database**.

4.  **Web Application Enqueues Background Jobs:**
    *   The **Web Application** enqueues jobs into **Redis** for the **Sidekiq Processor** to handle, such as:
        *   Sending a confirmation email to the Publisher (via **GOV.UK Notify**).
        *   If the vacancy is published immediately: Indexing the new vacancy for search.
        *   If the vacancy is published immediately: Preparing the vacancy for export to **DWP Find a Job**.

5.  **Web Application Responds to Publisher:**
    *   The **Web Application** redirects the Publisher to a confirmation page or the vacancy dashboard.

6.  **Sidekiq Processor Handles Jobs:**
    *   **Sidekiq Processor** picks up jobs from the **Redis** queue.
    *   For email sending: **Sidekiq Processor** calls **GOV.UK Notify** API.
    *   For DWP export: A scheduled **Sidekiq Processor** job will later query new/updated 'published' vacancies from **PostgreSQL Database**, generate an XML file, and upload it to **DWP Find a Job** via SFTP.

*(Potential Sequence Diagram: A PlantUML sequence diagram could be added here later if more detail is needed, e.g., `documentation/arc42/c4_runtime_publisher_posts_vacancy.puml`)*

```plantuml
@startuml PublisherPostsVacancyScenario
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Sequence: Publisher Posts a New Vacancy

actor Publisher
participant "Web Application" as WebApp
participant "DfE Sign-in" as DfeSignIn
participant "PostgreSQL DB" as PostgreSQL
participant "Redis (Sidekiq Queue)" as RedisQueue
participant "Sidekiq Processor" as Sidekiq
participant "GOV.UK Notify" as GovUKNotify
participant "Google Drive (Virus Scan)" as GoogleDrive

Publisher -> WebApp: Accesses Create Vacancy
activate WebApp

alt Not Authenticated or not associated with an org
    WebApp -> DfeSignIn: Redirect for Authentication
    activate DfeSignIn
    DfeSignIn --> Publisher: Authenticates
    Publisher -> DfeSignIn: Confirms
    DfeSignIn --> WebApp: Returns Auth Token
    deactivate DfeSignIn
    WebApp -> WebApp: Validates token, creates session, confirms org association
end

Publisher -> WebApp: Submits Vacancy Details (multi-step form)
WebApp -> WebApp: Validates form data (mandatory fields, data types, business rules)

alt Documents Uploaded
    WebApp -> GoogleDrive: Upload for virus scan
    activate GoogleDrive
    GoogleDrive --> WebApp: Scan result
    deactivate GoogleDrive
end

WebApp -> PostgreSQL: Save new vacancy record (e.g., status 'draft')
activate PostgreSQL
PostgreSQL --> WebApp: Confirmation
deactivate PostgreSQL

WebApp -> RedisQueue: Enqueue job (e.g., confirmation email)
opt Vacancy Published
    WebApp -> RedisQueue: Enqueue job (e.g., index for search)
    WebApp -> RedisQueue: Enqueue job (e.g., prepare DWP export)
end

WebApp --> Publisher: Shows confirmation page / dashboard
deactivate WebApp

Sidekiq -> RedisQueue: Dequeue email job
activate Sidekiq
Sidekiq -> GovUKNotify: Send confirmation email
deactivate Sidekiq
' ... other jobs like DWP export would follow a similar pattern via Sidekiq ...
@enduml
```
*(This diagram is illustrative. A more detailed one might be created as `documentation/arc42/c4_runtime_publisher_posts_vacancy.puml`)*

### 6.2 Interactions between Components

The primary interactions are:
*   **Web Application to PostgreSQL Database:** For all CRUD (Create, Read, Update, Delete) operations on persistent data. This is synchronous within a web request or background job.
*   **Web Application to Redis:** For caching, session management, and enqueuing background jobs. This is synchronous.
*   **Sidekiq Processor to Redis:** For dequeuing jobs and storing job state. This is synchronous for Redis operations.
*   **Sidekiq Processor to PostgreSQL Database:** For reading/writing data required by background jobs.
*   **Web Application/Sidekiq Processor to External Services:** These are typically synchronous API calls (e.g., to GOV.UK Notify, Google Services, DfE Sign-in, GOV.UK One Login) or file transfers (SFTP to DWP Find a Job). The success or failure of these interactions can influence the flow (e.g., retry mechanisms in Sidekiq).

## 7. Deployment View

This section outlines the infrastructure and deployment strategy for the Teaching Vacancies service.

### 7.1 Infrastructure Overview

The Teaching Vacancies service is hosted on the **Azure Cloud Infrastructure Platform (CIP)**. The primary components of the infrastructure are:

*   **Azure Kubernetes Service (AKS):** The application containers (Web Application, Sidekiq Processor) are deployed and managed within AKS clusters. This provides scalability, resilience, and efficient resource management.
*   **Azure PostgreSQL Flexible Server:** Managed database service providing the PostgreSQL Database.
*   **Azure Cache for Redis:** Managed service providing the Redis in-memory data store.
*   **Other Azure Services:** Likely includes services for load balancing, networking (VNETs, subnets), monitoring (Azure Monitor), and security (Key Vault for secrets).

Infrastructure is managed using **Terraform**, promoting an Infrastructure as Code (IaC) approach.

### 7.2 Deployment Environments

The service utilizes multiple environments, each hosted within specific AKS clusters and namespaces:

| Environment | URL Prefix                                      | Code Branch | CI/CD Workflow      | AKS Cluster                         | AKS Namespace   | Purpose                                                                 |
|-------------|-------------------------------------------------|-------------|---------------------|-------------------------------------|-----------------|-------------------------------------------------------------------------|
| Production  | `https://teaching-vacancies.service.gov.uk`     | `main`      | `build_and_deploy.yml` | s189-teacher-services-cloud-production | tv-production   | Live service used by all users.                                         |
| Staging     | `https://staging.teaching-vacancies.service.gov.uk` | `main`      | `build_and_deploy.yml` | s189-teacher-services-cloud-test    | tv-staging      | Pre-production environment for final testing before promoting to Production. |
| QA          | `https://qa.teaching-vacancies.service.gov.uk`    | `main`      | `build_and_deploy.yml` | s189-teacher-services-cloud-test    | tv-development  | General testing by the team on a production-like environment.             |
| Review      | `https://teaching-vacancies-review-pr-xxxx...`  | PR branch   | `build_and_deploy.yml` | s189-teacher-services-cloud-test    | tv-development  | Ephemeral apps created per Pull Request for isolated testing of changes. |

*   **Production Environment:**
    *   Runs on the `s189-teacher-services-cloud-production` AKS cluster within the `tv-production` namespace.
    *   Uses dedicated Azure PostgreSQL and Redis instances.
*   **Staging Environment:**
    *   Runs on the `s189-teacher-services-cloud-test` AKS cluster within the `tv-staging` namespace.
    *   Uses its own dedicated Azure PostgreSQL and Redis instances.
*   **QA Environment:**
    *   Runs on the `s189-teacher-services-cloud-test` AKS cluster within the `tv-development` namespace.
    *   Shares Azure PostgreSQL and Redis instances with Review Apps, but typically uses a separate database within PostgreSQL.
*   **Review Apps:**
    *   Run on the `s189-teacher-services-cloud-test` AKS cluster within the `tv-development` namespace.
    *   Are ephemeral and created for each Pull Request. They are destroyed when the PR is merged.
    *   Share PostgreSQL and Redis instances with the QA environment.

The following diagram illustrates the typical deployment setup, focusing on the Production environment and showing the relationship with other environments:

![Deployment Diagram](c4_deployment_diagram.puml)
*(The diagram is defined in `documentation/arc42/c4_deployment_diagram.puml`. It provides a simplified view, especially for shared resources in non-production environments.)*

### 7.3 Deployment Process

*   **Continuous Integration/Continuous Deployment (CI/CD):** Deployments are automated using GitHub Actions, defined in workflows like `build_and_deploy.yml`.
*   **Branching Strategy:** Changes are typically made on feature branches, then merged into `main` via Pull Requests.
*   **Deployment to Staging and Production:** Merging a PR to `main` triggers a deployment first to Staging. After successful smoke tests on Staging, the same build is promoted to Production.
*   **Review App Deployment:** Creating a Pull Request automatically triggers the build and deployment of a Review App.
*   **Containerization:** The application is packaged into Docker containers, which are then deployed to AKS.
*   **Secrets Management:** Application secrets (e.g., API keys, database credentials) are managed using Azure Key Vault and accessed by the application at runtime. Environment variables specific to each environment (e.g., database hostnames, feature flags) are managed through `_app_env.yml` files versioned in Terraform workspace variables.

## 8. Cross-cutting Concepts

This section describes concepts that are relevant to multiple parts of the Teaching Vacancies architecture.

### 8.1 Architecture and Design Patterns

*   **Model-View-Controller (MVC):** As a Ruby on Rails application, MVC is the fundamental architectural pattern.
    *   Models (`app/models`) manage data and business logic.
    *   Views (`app/views`) handle presentation.
    *   Controllers (`app/controllers`) orchestrate requests and responses.
*   **Service Objects (`app/services`):** Used to encapsulate complex business logic or operations that don't fit neatly into a single model or controller, promoting separation of concerns.
*   **Form Objects (`app/form_models`):** Handle validation and processing for complex forms, especially those spanning multiple steps or models. This keeps controllers and models cleaner.
*   **Background Job Processing:** Sidekiq is used to offload long-running tasks (e.g., email sending, data synchronization, analytics aggregation) from the synchronous web request cycle, improving responsiveness. This is an example of the Asynchronous Task Pattern.
*   **Convention over Configuration:** A core principle of Ruby on Rails, reducing boilerplate and promoting consistency.
*   **Monolithic Architecture:** The system is primarily a single, large application rather than a distributed set of microservices.
*   **ViewComponents (`app/components`):** Reusable, testable UI elements with their own templates and Ruby backing classes, promoting DRY (Don't Repeat Yourself) principles in the frontend.

### 8.2 Security

*   **Authentication:**
    *   **Publishers:** Delegated to DfE Sign-in (OAuth 2.0).
    *   **Jobseekers:** Delegated to GOV.UK One Login (OpenID Connect).
    *   **Support Users:** Likely DfE Sign-in or a similar internal mechanism.
    *   **Fallback Authentication:** Mechanisms exist to allow login via email links if primary IdPs are down.
*   **Authorization:** Handled within the application, likely using controller filters and model-level checks to ensure users can only access appropriate resources and perform permitted actions. Specific gems like Pundit or CanCanCan might be used, or custom logic.
*   **Input Validation:** Performed at multiple levels (form objects, models, controller params) to prevent common web vulnerabilities (e.g., XSS, SQL injection).
*   **CSRF Protection:** Ruby on Rails provides built-in Cross-Site Request Forgery protection.
*   **Secrets Management:** API keys, database credentials, and other secrets are managed via Azure Key Vault and accessed through environment variables or Rails credentials.
*   **HTTPS:** All communication is over HTTPS.
*   **Content Security Policy (CSP):** Configured to mitigate XSS and other injection attacks.
*   **Permissions Policy:** Configured to control browser features.
*   **Virus Scanning:** Documents uploaded by users are scanned for viruses using Google Drive API.
*   **Bot Mitigation:** Google reCAPTCHA v3 is used on public forms to identify and block suspicious traffic.
*   **Dependency Management:** Tools like Dependabot are used to monitor and update dependencies, mitigating risks from vulnerable libraries.
*   **Rack::Attack:** Used for rate limiting and blocking malicious requests.

### 8.3 Logging and Monitoring

*   **Logging:**
    *   Application logging is handled by `SemanticLogger`.
    *   Logs are likely aggregated and made searchable via a service like Logit.io (Kibana).
*   **Monitoring:**
    *   **Sentry:** Used for real-time error tracking and reporting.
    *   **Skylight:** Used for performance monitoring and diagnostics.
    *   **Google Analytics & Tag Manager:** For tracking user behavior and website analytics.
    *   **Azure Monitor:** For monitoring the underlying Azure infrastructure (AKS, PostgreSQL Database, Redis).
    *   **StatusCake:** For uptime monitoring and public status pages.
    *   **Prometheus/Grafana:** Potentially used within AKS for more detailed metrics, though not explicitly confirmed in all docs.
*   **Alerting:** Alerts are likely configured in Sentry, Azure Monitor, and StatusCake to notify the team of errors, performance issues, or outages. An alert runbook exists.

### 8.4 Performance and Scalability

*   **Caching:** Redis is used for caching session data, page fragments, and frequently accessed query results.
*   **Background Jobs:** Sidekiq offloads tasks to prevent blocking web requests.
*   **Database Optimization:**
    *   Use of appropriate database indexes (including spatial indexes for PostGIS).
    *   Simplification of complex polygons for geospatial queries.
    *   Pre-computation of data like centroids.
*   **Asset Pipeline:** `jsbundling-rails` and `cssbundling-rails` with `esbuild` optimize frontend assets. `Propshaft` manages assets.
*   **Scalability:** Horizontal scaling of web and worker pods within Azure Kubernetes Service (AKS). Database and Redis instances can also be scaled up.
*   **Content Delivery Network (CDN):** While not explicitly stated for all assets, a CDN might be used for serving static assets to reduce latency.

### 8.5 Data Management

*   **Data Persistence:** PostgreSQL Database is the primary data store.
*   **Data Integrity:** Enforced through database constraints, model validations, and transactional operations.
*   **Database Migrations:** Rails migrations are used to manage schema changes incrementally.
*   **Data Seeding:** `db/seeds.rb` is used to populate initial data for development and testing.
*   **Backups:** Regular database backups are performed (details in `documentation/operations/maintenance/database-backups.md`).
*   **Data Import/Export:** Mechanisms exist for importing data (e.g., ONS polygons, ATS vacancies) and exporting data (e.g., DWP Find a Job XML).
*   **Geospatial Data:** Handled by PostGIS extension in PostgreSQL Database.

### 8.6 Error Handling and Resilience

*   **Error Tracking:** Sentry captures and reports exceptions.
*   **Fallback Mechanisms:**
    *   Authentication fallback for DfE Sign-in and GOV.UK One Login.
*   **Timeouts and Retries:** Likely implemented for external API calls and background jobs. Sidekiq has built-in retry mechanisms.
*   **Graceful Degradation:** Some non-critical features might degrade gracefully if external services are unavailable.
*   **Idempotency:** Important for background jobs and API integrations to ensure operations can be safely retried.
*   **Maintenance Mode:** A mechanism exists to put the site into maintenance mode.

### 8.7 Internationalization and Localization (I18n)

*   The application uses Rails I18n framework for managing translations.
*   Locale files are stored in `config/locales`.
*   Content, including form labels, error messages, and page titles, is internationalized.
*   Primarily focused on English (`en`) as the service is for England.

## 9. Design Decisions

This section outlines key architectural and design decisions made throughout the project's lifecycle. Many of these are formally documented in Architectural Decision Records (ADRs) located in the `/documentation/adr` directory.

### 9.1 Key Architectural Decisions

*   **ADR 0010: Migrate Search from Algolia to PostgreSQL**
    *   **Status:** Accepted and Implemented. (Decided 2021-09-22)
    *   **Context:** The service initially used Algolia for its search functionality (after migrating from Elasticsearch, see ADR 0005 and ADR 0008). While performant, Algolia introduced an external dependency, cost, and complexity in keeping the search index synchronized with the primary PostgreSQL Database.
    *   **Decision:** To migrate the search functionality entirely to PostgreSQL Database, leveraging its built-in full-text search capabilities and PostGIS for geospatial searches.
    *   **Consequences:**
        *   Reduced external dependencies and associated costs.
        *   Simplified data synchronization logic, as the primary database is now the source of truth for search.
        *   Potentially increased load on the PostgreSQL Database, requiring careful monitoring and optimization.
        *   Full control over search algorithms and ranking.
        *   Ability to integration test search results.
        *   Synonym logic and typo tolerance/fuzzy search would need custom implementation.

*   **ADR 0009: Build Job Applications Functionality In-House Rather Than Buy COTS**
    *   **Status:** Accepted and Implemented. (Proposed 2020-11-16)
    *   **Context:** Need for a system to allow Jobseekers to apply for vacancies. Options were to build this functionality within Teaching Vacancies or integrate a Commercial Off-The-Shelf (COTS) product.
    *   **Decision:** To build the job application functionality directly within the Teaching Vacancies platform.
    *   **Consequences:**
        *   Full control over the user experience and application workflow, meeting specific user needs and accessibility requirements.
        *   Tighter integration with existing vacancy and Jobseeker data.
        *   Avoided cumbersome integration with external ATS, issues with DfE Sign-in, and data extraction for analytics.
        *   Increased development and maintenance effort compared to buying a COTS solution.
        *   Avoided potential integration complexities and costs associated with a COTS product.

*   **ADR 0007: Use Devise for Authentication**
    *   **Status:** Partially superseded by DfE Sign-in and GOV.UK One Login integrations. (Decided 2020-11-16)
    *   **Context:** Initial need for user authentication (specifically for Jobseekers to manage applications) before the broader DfE identity solutions were fully adopted or mandated for this service.
    *   **Decision:** To use the Devise gem for managing user authentication.
    *   **Consequences:**
        *   Provided a standard, well-tested authentication solution within Rails.
        *   Later, the primary authentication for Publishers was shifted to DfE Sign-in, and for Jobseekers to GOV.UK One Login, making direct Devise usage for primary login less central. However, Devise likely still underpins session management, password encryption for imported/fallback accounts, or other authentication-related features.

*   **ADR 0008: Continue to use Algolia as Search Engine (Intermediate Decision)**
    *   **Status:** Deprecated (superseded by ADR 0010). (Decided 2020-11-16)
    *   **Context:** Review of search engine choice after migrating from Elasticsearch to Algolia. Options included staying with Algolia, migrating back to Elasticsearch, or using PostgreSQL Database.
    *   **Decision:** To continue using Algolia at that time, primarily due to lower financial cost compared to Elasticsearch and to avoid the opportunity cost of another migration.
    *   **Consequences:**
        *   Acknowledged limitations in testing and knowledge sharing (Algolia not widely used in DfE).
        *   Postponed the move to a more integrated or open solution.

*   **ADR 0005: Elasticsearch to Algolia Migration (Search - Initial Step)**
    *   **Status:** Deprecated (superseded by ADR 0008, then ADR 0010). (Discussing 08/04/2020)
    *   **Context:** Issues with the existing Elasticsearch setup, including suboptimal search results and job alerts, and complexity in managing rankings.
    *   **Decision:** To migrate the search functionality from Elasticsearch to Algolia.
    *   **Consequences:**
        *   Algolia offered easier 'out of the box' functionality, faster performance, and a UI for managing rankings.
        *   Required re-implementation of UI search (using `instantSearch.js`) and job alert queries.
        *   This decision reflects an iterative approach to finding the best search solution.

*   **ADR 0011: Use Devcontainers for Development Environment**
    *   **Status:** Accepted. (Decided 2021-09-22)
    *   **Context:** Frustrating and time-consuming process to set up local development environments, with multiple dependencies and manually updated documentation.
    *   **Decision:** To adopt Development Containers (Devcontainers) with VS Code as an option, aiming for it to become the default.
    *   **Consequences:**
        *   Simplified onboarding and setup (dependencies reduced to Git, Docker, VS Code).
        *   Consistent and reproducible development environment across developers and OS types.
        *   Environment definition is version-controlled ("executable documentation").
        *   Potential for minor performance overhead on non-Linux systems, and need for Docker layer pruning.

*   **ADR 0004: Define Environments After GOV.UK PaaS Migration**
    *   **Status:** Accepted (contextually relevant to the previous PaaS hosting). (Discussing 10/02/2020)
    *   **Context:** Migration to GOV.UK PaaS prompted a rethink of delivery process and environments, aiming for simplification alongside a move away from Gitflow.
    *   **Decision:** To reduce environments to **staging** (for UAT, QA, demos) and **production**. Developers and user researchers would use local environments.
    *   **Consequences:**
        *   Reduced maintenance overhead for environments on PaaS.
        *   Emphasized ease of spinning up temporary environments on PaaS if needed.
        *   The current Azure environment setup (Review, QA, Staging, Production) is a later evolution documented in `documentation/operations/infrastructure/hosting.md`.

*   **ADR 0003: Replace Gitflow with Simple Git Workflow**
    *   **Status:** Accepted. (Discussing 23/01/2020)
    *   **Context:** Gitflow was considered too complex and hindered ambitions for continuous delivery.
    *   **Decision:** To adopt a simpler Git workflow: remove long-lived branches except `main`, require PRs and CI for `main`, use feature flags for unfinished work, and adapt staging pipeline for on-demand builds.
    *   **Consequences:**
        *   Simplified branching and merging strategy, easier for developers.
        *   Better alignment with CI/CD.
        *   Anticipated potential for more defects reaching production initially, but faster fixes.
        *   Required changes to AWS Codebuild/Codepipeline setup at the time.

*   **ADR 0002: Replace Google Sheets with BigQuery for Analytics Data**
    *   **Status:** Accepted. (Approved 20/01/2020)
    *   **Context:** Google Sheets used as a reporting database was hitting cell limits, prone to accidental edits, and difficult to maintain.
    *   **Decision:** To use Google BigQuery as the reporting database.
    *   **Consequences:**
        *   More scalable and performant solution for data analytics, operating within free tier initially.
        *   Allows for more complex querying and reporting.
        *   Integrates with tools like Google Data Studio.
        *   Eliminated issues with Google Sheets limits and maintenance.

*   **ADR 0001: Get Postcode from Coordinates (Geocoding Strategy)**
    *   **Status:** Accepted. (Approved 10/10/2019)
    *   **Context:** Need to get a postcode from geographical coordinates obtained from the browser.
    *   **Decision:** To use postcodes.io via a simple AJAX call from the browser, instead of using the geocoder gem and a server endpoint.
    *   **Consequences:**
        *   Avoided load on the server.
        *   Relies on an external open-source service (postcodes.io).
        *   (Note: The primary geocoding for location search now uses Google Geocoding API, this ADR might be specific to a particular feature or initial approach).

*   **Inferred Decision: Choice of Ruby on Rails**
    *   **Status:** Accepted (foundational).
    *   **Context:** Need for a web application framework to build the service.
    *   **Decision:** Ruby on Rails was chosen. This is inferred from the entire codebase structure and common practices for UK government digital services at the time of initial development.
    *   **Consequences:**
        *   Rapid development through conventions and extensive gem ecosystem.
        *   Strong community support.
        *   Well-understood MVC pattern.
        *   Requires expertise in the Ruby/Rails ecosystem.

*   **Inferred Decision: Monolithic Architecture**
    *   **Status:** Accepted (foundational).
    *   **Context:** Initial development of a new service.
    *   **Decision:** The system was built as a monolith rather than a microservices architecture.
    *   **Consequences:**
        *   Simpler to develop, test, and deploy initially.
        *   Can become complex to maintain and scale as the application grows.
        *   All components are tightly coupled.

### 9.2 Alternatives Considered

Information on alternatives considered is primarily within the ADRs themselves. For example:
*   **Search Solution:** Elasticsearch, Algolia, and finally PostgreSQL Database were iteratively adopted. Each was chosen based on the understanding and trade-offs known at the time (performance, cost, complexity, maintainability).
*   **Job Applications Functionality:** Building in-house vs. buying a COTS product (ADR 0009).
*   **Authentication:** Devise vs. other Rails authentication gems (ADR 0007). The broader strategy then evolved to use DfE Sign-in and GOV.UK One Login.
*   **Development Workflow:** Gitflow vs. a simpler trunk-based development model (ADR 0003).
*   **Reporting Database:** Google Sheets vs. read replica, API streaming, new PostgreSQL DB, BigQuery, AWS Redshift (ADR 0002).
*   **Local Development Setup:** Manual setup vs. Devcontainers (ADR 0011).

For many foundational choices (like Ruby on Rails), explicit documentation of alternatives might not exist if the choice was standard practice at the project's inception or deemed self-evident given the team's skills and project goals at that point.

## 10. Quality Requirements

This section details the quality attributes and non-functional requirements crucial for the Teaching Vacancies service. These are derived from the Quality Goals (Section 1.2), operational context, and best practices for public sector web services.

### 10.1 Quality Scenarios

These scenarios illustrate desired quality attributes in specific contexts. Where explicit metrics are not documented, they are based on common expectations for similar services.

*   **Usability & Accessibility:**
    *   **Scenario:** A Jobseeker with visual impairments using a screen reader can successfully search for a vacancy, understand the job details, and initiate an application (or navigate to an external application site) without undue difficulty.
    *   **Metric:** Full compliance with WCAG 2.1 Level AA guidelines. Regular accessibility audits (e.g., annually or post-major feature releases).
    *   **Scenario:** A Publisher can post a new vacancy within 10 minutes, assuming they have all required information ready.
    *   **Metric:** User satisfaction surveys and direct feedback indicate ease of use.

*   **Performance & Scalability:**
    *   **Scenario:** During peak hours (e.g., Monday mornings, post-holiday periods), 95% of job search results pages load within 3 seconds.
    *   **Metric:** Application Performance Monitoring (APM) tools (e.g., Skylight, Azure Monitor) track page load times and server response times.
    *   **Scenario:** The system can handle a 50% increase in concurrent users compared to the average daily peak without significant degradation in performance or stability.
    *   **Metric:** Load testing performed periodically or before anticipated high-traffic events. AKS auto-scaling configured and tested.

*   **Reliability & Availability:**
    *   **Scenario:** The service achieves 99.9% uptime, excluding scheduled maintenance.
    *   **Metric:** Uptime monitored by tools like StatusCake and Azure Monitor.
    *   **Scenario:** In case of a database connection failure, the system displays a user-friendly error page and recovers automatically once the database is available, with no data loss for in-progress user sessions where feasible.
    *   **Metric:** Documented disaster recovery and failover procedures. Regular testing of these procedures.
    *   **Scenario:** Background jobs (e.g., DWP XML export, email alerts) complete successfully within their expected timeframe (e.g., daily for DWP export).
    *   **Metric:** Sidekiq monitoring shows low job failure rates and timely processing. Alerts for high failure rates.

*   **Security:**
    *   **Scenario:** An attempt to exploit a common web vulnerability (e.g., SQL injection, XSS) on a public form is prevented.
    *   **Metric:** Regular penetration testing (e.g., annually) and vulnerability scanning. No critical or high-severity vulnerabilities in production.
    *   **Scenario:** Sensitive user data (e.g., PII in job applications or profiles) is encrypted at rest and in transit.
    *   **Metric:** Compliance with data protection regulations (e.g., GDPR). Secure configurations for database and application servers.

*   **Maintainability & Evolvability:**
    *   **Scenario:** A new developer can set up their development environment and understand the basic codebase structure to fix a minor bug within their first week.
    *   **Metric:** Up-to-date onboarding documentation (including use of Devcontainers). Code complexity metrics (e.g., RuboCop, Code Climate) maintained within acceptable thresholds.
    *   **Scenario:** Deploying a new feature or bug fix to production can be done multiple times a week with minimal manual intervention and low risk of failure.
    *   **Metric:** High success rate for automated deployments. Short deployment lead times.

### 10.2 Non-Functional Requirements (Summary)

This summarizes key non-functional requirements based on the above scenarios and system context.

*   **Performance:**
    *   Average page load time (server-side): < 500ms for 95% of requests.
    *   Job search response time (database query): < 2 seconds for 95% of typical searches.
    *   System should support at least [Specify target, e.g., 1000] concurrent users during peak load without performance degradation beyond defined thresholds. *(Assumption: Based on best practice for a national service; actual target may vary).*
*   **Scalability:**
    *   The application (Web Application and Sidekiq Processor pods) must be horizontally scalable in AKS.
    *   Database and Redis services should be scalable to meet increasing load.
*   **Availability:**
    *   Target uptime: 99.9% (excluding planned maintenance).
    *   Planned maintenance windows should be communicated in advance and scheduled outside of peak usage hours.
*   **Reliability:**
    *   Background jobs must be reliable with robust error handling and retry mechanisms.
    *   Data integrity must be maintained across the system.
*   **Security:**
    *   Compliance with DfE security policies and relevant government standards.
    *   Protection against OWASP Top 10 vulnerabilities.
    *   Secure handling and storage of any PII.
    *   Regular security audits and penetration tests.
*   **Maintainability:**
    *   Codebase should be well-documented (inline comments, READMEs, arc42).
    *   High test coverage (unit, integration, system tests).
    *   Adherence to coding standards (enforced by linters like RuboCop).
    *   Modular design where appropriate (e.g., service objects, ViewComponents).
*   **Usability:**
    *   Intuitive navigation and user workflows for both Jobseekers and Publishers.
    *   Clear and concise content.
*   **Accessibility:**
    *   WCAG 2.1 Level AA compliance for all user-facing interfaces.
*   **Interoperability:**
    *   Reliable integration with all specified external systems (DfE Sign-in, GOV.UK One Login, DWP, ATS API, etc.).
    *   API endpoints should be well-documented and versioned where appropriate.
*   **Recoverability:**
    *   In case of system failure, data should be recoverable to a recent point in time (RPO defined by DfE policy, e.g., 1 hour). *(Assumption: Based on best practice).*
    *   Time to recover service (RTO) should be within defined limits (e.g., 4 hours for critical failures). *(Assumption: Based on best practice).*

*(Note: Specific metrics for some NFRs, particularly those marked with "Assumption," should be confirmed against DfE operational targets and policies if available. The values provided are placeholders based on common industry best practices for such systems.)*

## 11. Risks and Technical Debt

This section outlines known and potential risks to the project, as well as identified areas of technical debt.

### 11.1 Identified Risks

*   **External Service Dependencies:**
    *   **Risk:** Outages or changes in external services (DfE Sign-in, GOV.UK One Login, DWP Find a Job, Google Services, GOV.UK Notify, ONS ArcGIS) can impact Teaching Vacancies functionality.
    *   **Mitigation:** Fallback mechanisms (e.g., for authentication), asynchronous processing for non-critical integrations, robust error handling, monitoring of external service health, clear communication channels with service providers.
*   **Security Vulnerabilities:**
    *   **Risk:** New vulnerabilities in application code, dependencies, or infrastructure could be exploited.
    *   **Mitigation:** Regular security testing (pen tests, vulnerability scans), dependency monitoring (Dependabot), adherence to secure coding practices, timely patching, Web Application Firewall (WAF) capabilities via Azure.
*   **Data Quality Issues:**
    *   **Risk:** Inaccurate or incomplete data from schools/trusts (e.g., vacancy details) or imported sources (e.g., ONS polygons) can lead to poor user experience or search results.
    *   **Mitigation:** Input validation, clear guidance for Publishers, monitoring of data import processes, mechanisms for data correction.
*   **Performance Degradation at Scale:**
    *   **Risk:** As user numbers and data volumes grow, specific parts of the system (e.g., complex searches, database queries) might not scale efficiently.
    *   **Mitigation:** Ongoing performance monitoring, load testing, database optimization, horizontal scaling of application instances in AKS, potential for further caching strategies.
*   **Technology Obsolescence:**
    *   **Risk:** Key components of the tech stack (Ruby, Rails, specific gems) may become outdated or unsupported over time.
    *   **Mitigation:** Regular review and updates of dependencies, proactive planning for major version upgrades.
*   **Staffing and Knowledge Retention:**
    *   **Risk:** Loss of key personnel with deep knowledge of the system can impact development and maintenance. (Common risk for many projects).
    *   **Mitigation:** Comprehensive documentation (like this arc42 document, ADRs, code comments), knowledge sharing sessions, pair programming, clear onboarding processes (Devcontainers help here).
*   **Changes in Government Policy or Standards:**
    *   **Risk:** New government policies, accessibility standards, or technical directives may require significant changes to the service.
    *   **Mitigation:** Active monitoring of relevant government communications, flexible architecture to accommodate changes where possible.
*   **Complexity of Monolithic Architecture:**
    *   **Risk:** As the monolith grows, it can become harder to maintain, test, and deploy changes quickly and safely. Refactoring or introducing new features can have unintended consequences.
    *   **Mitigation:** Strong adherence to modular design principles within the monolith (service objects, components), comprehensive automated testing, careful planning for large changes. Consideration for future decomposition if complexity becomes unmanageable (though this is a major undertaking).

### 11.2 Technical Debt Register

This is not an exhaustive list but captures potential or known areas of technical debt based on observations from documentation and common software lifecycle issues. A more formal register might be maintained by the development team.

*   **Legacy Integrations (ATS):** The documentation mentions older XML feed-based ATS integrations that are planned to be removed in favor of the Publisher ATS API. These represent technical debt until fully decommissioned.
    *   **Impact:** Maintenance overhead, potential confusion, security surface if not actively managed.
    *   **Remediation:** Complete migration of clients to the Publisher ATS API and decommission old integrations.
*   **Search Evolution:** The history of search (Elasticsearch -> Algolia -> PostgreSQL Database) as documented in ADRs suggests that there might be remnants of older search configurations or code paths that could be cleaned up.
    *   **Impact:** Potential for dead code, slightly increased complexity.
    *   **Remediation:** Code review and refactoring to remove any unused search-related components.
*   **Frontend Complexity:** While StimulusJS and ViewComponents help, complex UIs in a server-rendered Rails monolith can sometimes accumulate JavaScript and CSS that becomes hard to manage over time. *(Assumption: Common issue, not explicitly stated)*.
    *   **Impact:** Increased difficulty in making frontend changes, potential for style conflicts or JavaScript errors.
    *   **Remediation:** Regular refactoring of frontend code, adherence to component-based architecture, style guide enforcement.
*   **Test Coverage Gaps:** While extensive testing is a goal, there might be areas with lower test coverage, especially for older or less frequently modified parts of the system. *(Assumption: Common issue, not explicitly stated)*.
    *   **Impact:** Increased risk of regressions when making changes.
    *   **Remediation:** Ongoing efforts to improve test coverage, particularly for critical paths and new features.
*   **Outdated Dependencies:** Despite tools like Dependabot, some dependencies might be held back due to compatibility issues or the effort required to upgrade. *(Assumption: Common issue, not explicitly stated)*.
    *   **Impact:** Potential security risks, missing out on new features or performance improvements from newer library versions.
    *   **Remediation:** Regular schedule for reviewing and updating dependencies, allocating time for larger upgrades.
*   **Documentation Gaps or Staleness:** While this arc42 document aims to improve the situation, other parts of the documentation might be outdated or incomplete.
    *   **Impact:** Slower onboarding, increased risk of misunderstandings or errors during development and operations.
    *   **Remediation:** Continuous effort to keep documentation up-to-date and comprehensive. Making documentation updates part of the definition of done for new features or changes.

## 12. Glossary

This glossary defines key terms, acronyms, and abbreviations used in the context of the Teaching Vacancies service and its documentation.

### 12.1 Business Terms

*   **ADR (Architectural Decision Record):** A document that captures an important architectural decision, its context, and consequences.
*   **ATS (Applicant Tracking System):** Software used by employers to manage their recruitment processes. Teaching Vacancies provides an API for ATS integration.
*   **DfE (Department for Education):** The UK government department responsible for child protection, education, apprenticeships, and skills. The primary stakeholder for Teaching Vacancies.
*   **DWP (Department for Work and Pensions):** A UK government department. Teaching Vacancies exports job listings to their "Find a job" service.
*   **GIAS (Get Information About Schools):** A DfE service that provides information about educational establishments in England. Often a source for school data.
*   **Jobseeker:** An individual, typically a teacher or education professional, looking for a job.
*   **LA (Local Authority):** Local government bodies that may run schools or have responsibilities for education in their area.
*   **MAT (Multi-Academy Trust):** A group of academy schools that have come together to form a single trust with a board of trustees.
*   **NQT (Newly Qualified Teacher):** A teacher who has recently completed their initial teacher training.
*   **Ofsted (Office for Standards in Education, Children's Services and Skills):** A non-ministerial department of the UK government that inspects and regulates services that care for children and young people, and services providing education and skills for learners of all ages.
*   **Publisher:** An individual representing a school, trust, or LA who is responsible for posting and managing job vacancies on the service.
*   **Safeguarding:** Protecting the welfare of children and vulnerable adults. A key consideration in education recruitment.
*   **SEND (Special Educational Needs and Disabilities):** Refers to support for pupils with special educational needs or disabilities.
*   **SLT (Senior Leadership Team):** Senior management positions within a school or trust (e.g., Headteacher, Deputy Headteacher).
*   **Vacancy:** A job opening advertised on the Teaching Vacancies service.

### 12.2 Technical Terms

*   **AKS (Azure Kubernetes Service):** The managed Kubernetes service on Microsoft Azure used to host the application.
*   **API (Application Programming Interface):** A set of rules and protocols for building and interacting with software applications.
*   **CI/CD (Continuous Integration/Continuous Deployment or Delivery):** Practices that automate the building, testing, and deployment of software.
*   **CIP (Cloud Infrastructure Platform):** DfE's Azure-based hosting platform.
*   **COTS (Commercial Off-The-Shelf):** Software products that are ready-made and available for purchase.
*   **CSRF (Cross-Site Request Forgery):** A type of web security vulnerability.
*   **CSP (Content Security Policy):** A security standard to prevent XSS and other code injection attacks.
*   **Devcontainer (Development Container):** A Docker container configured to provide a consistent development environment.
*   **DfE Sign-in:** The DfE's identity and access management service for professionals in the education sector. Used to authenticate Publishers.
*   **Elasticsearch:** A distributed search and analytics engine (previously used by the service).
*   **Algolia:** A hosted search API service (previously used by the service).
*   **GOV.UK Design System:** A set of design principles, components, and patterns for building government websites in the UK.
*   **GOV.UK Notify:** A UK government service for sending emails, text messages, and letters to users.
*   **GOV.UK One Login:** The UK government's unified sign-in system for accessing government services. Used to authenticate Jobseekers.
*   **IaC (Infrastructure as Code):** Managing and provisioning infrastructure through machine-readable definition files (e.g., using Terraform).
*   **IdP (Identity Provider):** A system that creates, maintains, and manages identity information while providing authentication services (e.g., DfE Sign-in, GOV.UK One Login).
*   **I18n (Internationalization):** Designing software so that it can be adapted to various languages and regions without engineering changes.
*   **JSON (JavaScript Object Notation):** A lightweight data-interchange format.
*   **MVC (Model-View-Controller):** An architectural pattern commonly used in web applications.
*   **OAuth 2.0 (Open Authorization):** An open standard for access delegation, commonly used for authentication and authorization.
*   **OpenID Connect (OIDC):** An identity layer built on top of OAuth 2.0.
*   **ORM (Object-Relational Mapping):** A technique for converting data between incompatible type systems using object-oriented programming languages (e.g., ActiveRecord in Rails).
*   **PaaS (Platform as a Service):** A category of cloud computing services that provides a platform allowing customers to develop, run, and manage applications without the complexity of building and maintaining the infrastructure.
*   **PII (Personally Identifiable Information):** Information that can be used to identify an individual.
*   **PlantUML:** A tool to create UML diagrams from a textual description.
*   **PostGIS:** A spatial database extender for PostgreSQL object-relational database. It adds support for geographic objects allowing location queries to be run in SQL.
*   **PostgreSQL Database:** An open-source object-relational database system. Used as the primary database for Teaching Vacancies.
*   **PR (Pull Request):** A proposed change to a codebase submitted by a developer for review before being merged.
*   **Rake:** A Ruby build program with capabilities similar to make.
*   **Rails (Ruby on Rails):** A web application framework written in Ruby.
*   **Redis (Remote Dictionary Server):** An in-memory data structure store, used as a cache and message broker.
*   **RPO (Recovery Point Objective):** The maximum acceptable amount of data loss after an incident, measured in time.
*   **RTO (Recovery Time Objective):** The target time to restore a service after an incident.
*   **SAML (Security Assertion Markup Language):** An open standard for exchanging authentication and authorization data between parties.
*   **Sentry:** An error tracking and monitoring tool.
*   **SFTP (SSH File Transfer Protocol):** A secure file transfer protocol.
*   **Sidekiq:** A background job processing framework for Ruby. (Often referred to as Sidekiq Processor in this document).
*   **Sidekiq Processor:** The container/process responsible for running Sidekiq background jobs.
*   **Skylight:** An application performance monitoring tool for Ruby on Rails applications.
*   **Slim:** A templating language for Ruby, often used in Rails for writing concise views.
*   **SQL (Structured Query Language):** A standard language for managing and manipulating relational databases.
*   **SSM (AWS Systems Manager Parameter Store):** Used in the past or in other contexts for secrets management. (Note: Azure Key Vault is primary for this project on Azure).
*   **Terraform:** An open-source infrastructure as code software tool.
*   **URN (Unique Reference Number):** Often used to identify schools or other organizations.
*   **WCAG (Web Content Accessibility Guidelines):** Standards for making web content more accessible to people with disabilities.
*   **WAF (Web Application Firewall):** A security system that filters, monitors, and blocks HTTP traffic to and from a web application.
*   **Web Application:** The primary Ruby on Rails application that serves user interfaces and handles business logic.
*   **XSS (Cross-Site Scripting):** A type of web security vulnerability.
*   **XML (Extensible Markup Language):** A markup language designed to carry data.
*   **YAML (YAML Ain't Markup Language):** A human-readable data serialization language.
