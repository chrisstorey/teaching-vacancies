# arc42 Documentation Template for Teaching Vacancies

## 1. Introduction and Goals

### 1.1 Requirements Overview

Teaching Vacancies is a free job-listing service from the Department for Education (DfE) for schools in England. The primary goal of the service is to connect teachers with suitable job opportunities and to help schools and trusts find qualified candidates for their vacancies.

The system allows:
*   **Jobseekers (Teachers):** To search for jobs, view job details, save jobs, set up job alerts, and apply for vacancies.
*   **Publishers (Schools and Trusts):** To post job vacancies, manage their listings, and view applications (if using the in-platform application feature).

The service aims to provide a user-friendly and efficient platform for both jobseekers and employers in the education sector.

### 1.2 Quality Goals

*   **Usability:** The system should be intuitive and easy to use for both jobseekers and publishers with varying levels of technical proficiency.
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
*   **Authentication - Hiring Staff:** Must integrate with DfE Sign-in for authenticating hiring staff (publishers).
*   **Authentication - Jobseekers:** Must integrate with GOV.UK One Login for authenticating jobseekers.
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
*   Provide teachers and other jobseekers with a comprehensive and easy-to-use portal for finding educational job opportunities.
*   Streamline the application process where possible.
*   Integrate with other government services to provide a cohesive experience for users (e.g., DfE Sign-in, GOV.UK One Login) and to disseminate job information more widely (e.g., DWP Find a Job).

The service supports various types of educational institutions, including individual schools, academies, multi-academy trusts (MATs), and local authorities.

### 3.2 Technical Context

The system is a web-based application developed primarily with Ruby on Rails. It interacts with several external systems for authentication, data exchange, and utility services. Key technical aspects of its context include:
*   **User Interfaces:** Web interface for jobseekers and publishers. A separate interface for support users.
*   **Data Storage:** PostgreSQL for primary application data, Redis for caching and session management.
*   **Integrations:** Real-time and batch integrations with various DfE and third-party services.
*   **Deployment:** Hosted on Azure Kubernetes Service (AKS), managed via Terraform, and deployed through CI/CD pipelines (GitHub Actions).
*   **Security:** Relies on external identity providers (DfE Sign-in, GOV.UK One Login) and implements measures like reCAPTCHA and virus scanning.

### 3.3 External Interfaces

The Teaching Vacancies service interacts with the following external systems and actors:

![System Context Diagram](c4_system_context.puml)
*(The diagram is defined in `c4_system_context.puml`)*

*   **Jobseeker (Person):** Searches for jobs, manages their profile, sets up job alerts, and applies for vacancies. Interacts via the main web application.
*   **Publisher (Person):** Represents a school or trust. Posts and manages job vacancies, and may review applications. Interacts via the main web application, authenticated by DfE Sign-in.
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
*   **Authentication:** Leverages established government identity platforms (DfE Sign-in for publishers, GOV.UK One Login for jobseekers) rather than building a bespoke authentication system. This enhances security and user trust.
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
*(The diagram is defined in `c4_container_diagram.puml`)*

*   **Web Application (Container):**
    *   **Description:** The core of the system, implemented as a Ruby on Rails monolithic application.
    *   **Responsibilities:**
        *   Handling all incoming HTTP requests from users (Jobseekers, Publishers, Support Users).
        *   Implementing the business logic for all user stories.
        *   Rendering HTML user interfaces using server-side views (Slim templates with ViewComponents).
        *   Serving static assets (CSS, JavaScript, images).
        *   Interacting with the PostgreSQL database for data persistence.
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
    *   **Description:** A background job processing system that runs alongside the web application.
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
    *   Logic for creating, updating, publishing, and expiring vacancies.
    *   Search and filtering functionality for vacancies (keyword, location, role, etc.).
    *   Integration with the Publisher ATS API.
*   **Jobseeker Profile & Application Components:**
    *   Manages jobseeker profiles, saved jobs, and job alerts.
    *   Handles the job application process (if applying directly on the platform).
*   **Publisher/Organisation Components:**
    *   Manages organisation profiles (schools, trusts).
    *   Dashboard for publishers to view their vacancies and applications.
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

4.  **Web Application Processes Results:**
    *   The **PostgreSQL Database** returns a list of matching vacancies.
    *   The **Web Application** processes these results, potentially transforming data for display and paginating the results.
    *   Frequently accessed parts of the search results or filter options might be cached in **Redis** to speed up subsequent requests.

5.  **Web Application Renders Response:**
    *   The **Web Application** renders an HTML page displaying the search results.
    *   The browser displays the page to the Jobseeker.

*(Potential Sequence Diagram: A PlantUML sequence diagram could be added here later if more detail is needed on component interaction, e.g., `c4_runtime_jobseeker_search.puml`)*

```plantuml
@startuml JobseekerSearchScenario
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Sequence: Jobseeker Searches for Vacancy

actor Jobseeker
participant WebApp as "Web Application"
participant PostgreSQL as "PostgreSQL DB"
participant RedisCache as "Redis"
participant GoogleGeo as "Google Geocoding"

Jobseeker -> WebApp: Submits search (e.g., keyword, location)
activate WebApp

alt Location specified and not cached/polygon
    WebApp -> GoogleGeo: Get coordinates for location
    activate GoogleGeo
    GoogleGeo --> WebApp: Coordinates
    deactivate GoogleGeo
    WebApp -> RedisCache: Cache coordinates (optional)
end

WebApp -> PostgreSQL: Query vacancies (keywords, location filters, etc.)
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
    *   If not already authenticated, the **Web Application** redirects the Publisher to **DfE Sign-in**.
    *   **DfE Sign-in** authenticates the Publisher and redirects back to the **Web Application** with an authentication token.
    *   The **Web Application** validates the token and establishes a session for the Publisher.

2.  **Publisher Submits Vacancy Details:**
    *   The Publisher fills out the multi-step vacancy creation form on the **Web Application**.
    *   Upon final submission, the browser sends an HTTP POST request to the **Web Application**.

3.  **Web Application Validates and Saves Vacancy:**
    *   The relevant Rails controller action receives the request.
    *   Form data is validated (using `app/form_models` or model validations).
    *   If valid, the **Web Application** creates a new vacancy record in the **PostgreSQL Database**.
    *   If documents are uploaded (e.g., job description), the **Web Application** might interact with **Google Services** (Google Drive) for virus scanning before associating them with the vacancy in the **PostgreSQL Database**.

4.  **Web Application Enqueues Background Jobs:**
    *   The **Web Application** enqueues jobs into **Redis** for the **Sidekiq Processor** to handle, such as:
        *   Sending a confirmation email to the Publisher (via **GOV.UK Notify**).
        *   Indexing the new vacancy for search if not handled synchronously.
        *   Preparing the vacancy for export to **DWP Find a Job**.

5.  **Web Application Responds to Publisher:**
    *   The **Web Application** redirects the Publisher to a confirmation page or the vacancy dashboard.

6.  **Sidekiq Processor Handles Jobs:**
    *   **Sidekiq Processor** picks up jobs from the **Redis** queue.
    *   For email sending: **Sidekiq** calls **GOV.UK Notify** API.
    *   For DWP export: A scheduled **Sidekiq** job will later query new/updated vacancies from **PostgreSQL Database**, generate an XML file, and upload it to **DWP Find a Job** via SFTP.

*(Potential Sequence Diagram: A PlantUML sequence diagram could be added here later if more detail is needed, e.g., `c4_runtime_publisher_posts_vacancy.puml`)*

```plantuml
@startuml PublisherPostsVacancyScenario
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Sequence: Publisher Posts a New Vacancy

actor Publisher
participant WebApp as "Web Application"
participant DfeSignIn as "DfE Sign-in"
participant PostgreSQL as "PostgreSQL DB"
participant RedisQueue as "Redis (Sidekiq Queue)"
participant Sidekiq
participant GovUKNotify as "GOV.UK Notify"
participant GoogleDrive as "Google Drive (Virus Scan)"

Publisher -> WebApp: Accesses Create Vacancy
activate WebApp

alt Not Authenticated
    WebApp -> DfeSignIn: Redirect for Authentication
    activate DfeSignIn
    DfeSignIn --> Publisher: Authenticates
    Publisher -> DfeSignIn: Confirms
    DfeSignIn --> WebApp: Returns Auth Token
    deactivate DfeSignIn
    WebApp -> WebApp: Validates token, creates session
end

Publisher -> WebApp: Submits Vacancy Details (multi-step form)
WebApp -> WebApp: Validates form data

alt Documents Uploaded
    WebApp -> GoogleDrive: Upload for virus scan
    activate GoogleDrive
    GoogleDrive --> WebApp: Scan result
    deactivate GoogleDrive
end

WebApp -> PostgreSQL: Save new vacancy record
activate PostgreSQL
PostgreSQL --> WebApp: Confirmation
deactivate PostgreSQL

WebApp -> RedisQueue: Enqueue job (e.g., confirmation email)
WebApp -> RedisQueue: Enqueue job (e.g., prepare DWP export)

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
*   **Web Application to PostgreSQL:** For all CRUD (Create, Read, Update, Delete) operations on persistent data. This is synchronous within a web request or background job.
*   **Web Application to Redis:** For caching, session management, and enqueuing background jobs. This is synchronous.
*   **Sidekiq Processor to Redis:** For dequeuing jobs and storing job state. This is synchronous for Redis operations.
*   **Sidekiq Processor to PostgreSQL:** For reading/writing data required by background jobs.
*   **Web Application/Sidekiq Processor to External Services:** These are typically synchronous API calls (e.g., to GOV.UK Notify, Google Services, DfE Sign-in, GOV.UK One Login) or file transfers (SFTP to DWP Find a Job). The success or failure of these interactions can influence the flow (e.g., retry mechanisms in Sidekiq).

## 7. Deployment View

This section outlines the infrastructure and deployment strategy for the Teaching Vacancies service.

### 7.1 Infrastructure Overview

The Teaching Vacancies service is hosted on the **Azure Cloud Infrastructure Platform (CIP)**. The primary components of the infrastructure are:

*   **Azure Kubernetes Service (AKS):** The application containers (Web Application, Sidekiq Processor) are deployed and managed within AKS clusters. This provides scalability, resilience, and efficient resource management.
*   **Azure PostgreSQL Flexible Server:** Managed database service providing the PostgreSQL database.
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
*(The diagram is defined in `c4_deployment_diagram.puml`. It provides a simplified view, especially for shared resources in non-production environments.)*

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
    *   **Azure Monitor:** For monitoring the underlying Azure infrastructure (AKS, PostgreSQL, Redis).
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

*   **Data Persistence:** PostgreSQL is the primary data store.
*   **Data Integrity:** Enforced through database constraints, model validations, and transactional operations.
*   **Database Migrations:** Rails migrations are used to manage schema changes incrementally.
*   **Data Seeding:** `db/seeds.rb` is used to populate initial data for development and testing.
*   **Backups:** Regular database backups are performed (details in `documentation/operations/maintenance/database-backups.md`).
*   **Data Import/Export:** Mechanisms exist for importing data (e.g., ONS polygons, ATS vacancies) and exporting data (e.g., DWP Find a Job XML).
*   **Geospatial Data:** Handled by PostGIS extension in PostgreSQL.

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

### 9.1 Key Architectural Decisions
### 9.2 Alternatives Considered

## 10. Quality Requirements

### 10.1 Quality Scenarios
### 10.2 Non-Functional Requirements

## 11. Risks and Technical Debt

### 11.1 Identified Risks
### 11.2 Technical Debt Register

## 12. Glossary

### 12.1 Business Terms
### 12.2 Technical Terms
