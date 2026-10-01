# Cloud Financial Management Platform
The **Cloud Financial Management Platform** is a secure, full-stack banking and transactional  application designed to manage accounts, process ledgers and dynamic budgets, and generate advanced predictive analytics.

Built using microservices as inspiration for it's architecture, the application combines **Java Spring Boot REST API** backend engine, a  responsive **React/TypeScript frontend**, and a **Python FastAPI analytics microservice** using machine learning for spending predictions. **Docker** is used for containerisation and it is built for cloud-native deployment onto **AWS infrastructure** using automated pipelines.


## Features
*   **User Management & Security:** Secure registration and standard authentication using dynamic and salted **BCrypt password hashing**.
*   **JWT Authorization:** Stateless token using a 24-hour expiration cycle to a secure `UserPrincipal`  across protected routes.
*   **Multi-Account Management:** Secure CRUD operations for standard checking, savings, and credit card account activities supporting automated reconciliation.
*   **Transactional Ledger Engine:** Handling of **DEBIT** and **CREDIT** transactions by linking them to tracking matrices.
*   **Advanced Transaction Filtering:** Sorting and database queries parsing transaction data by category names and boundary dates.
*   **Server-Side Pagination:** Database indexing tracking items in distinct data pages via `JpaSpecificationExecutor`.
*   **Dynamic Budget Guards:** Automated tracking parameters monitoring outlays against limits, generating automated **WARNING** (80%) and **EXCEEDED** statuses.
*   **Financial Dashboards:** Complete UI visualizations showing total balances, dynamic month-to-date category rollups, and budget progress components.
*   **Microservice Analytics Engine:** Dedicated asynchronous calculations processing real-time monthly outlays, daily averages, and largest single outlays.
*   **Predictive Financial Forecasting:** A Python machine-learning service training **Scikit-Learn Linear Regression models** to forecast future monthly outlays based on historical trends.
*   **Production Database Persistence:** Relational standard indexing powered by **PostgreSQL** in production with full **Flyway schema migration** tracking.
*   **Docker Containerization:** Isolated environment configuration for the database, backend services, analytics engine, and an Nginx-backed frontend.
*   **Automated CI/CD Pipelines:** GitHub Actions workflow executing formatting checks, multi-language unit suites, compilation checks, and remote host deployments.
*   **AWS Cloud Infrastructure Deployment:** Integrated configuration  using **AWS Secrets Manager** for database credentials and programmatic deployments to **AWS EC2 instances** via SSH automation.
*   **CloudWatch Telemetry & Monitoring:** Production observability pushing customs performance gauges, system workloads, and JVM memory bounds to **AWS CloudWatch** via Micrometer registry updates.


## Architecture

## Technologies
## Technologies

| Area | Technology | Rationale / Implementations |
| :--- | :--- | :--- |
| **Backend** | Java 17 / Spring Boot 3.3 | Core API layer, Spring Security orchestration, JPA repositories. |
| **Frontend** | React 22 / TypeScript / Tailwind CSS | Single Page Application UI served via Nginx reverse proxy containers. |
| **Analytics** | Python 3.12 / FastAPI | Asynchronous data layer microservice computing telemetry calculations. |
| **Data Engine** | Pandas / Scikit-Learn | Structuring algorithmic arrays and training Linear Regression models. |
| **Database** | PostgreSQL / H2 Database / Flyway | Production persistent engine, local dev in-memory databases, schema evolution. |
| **Containers** | Docker / Docker Compose | Cross-platform runtime configuration and application isolation. |
| **Cloud Hosting** | AWS (EC2, Secrets Manager) | Infrastructure targeting, credential masking, secure instance routing. |
| **CI/CD** | GitHub Actions | Testing automation and delivery using appleboy SSH action configurations. |
| **Testing Frameworks** | JUnit 5 / Mockito / Pytest / Vitest | Custom integration suites, mock boundaries, and React testing libraries. |
| **Monitoring** | AWS CloudWatch / Spring Actuator | Custom registry namespace updates tracking processing telemetry. |
| **Version Control** | Git / GitHub | Source management and delivery pipeline trigger foundation. |


## Project Structure


## Architecture
The platform is built on a distributed, containerized microservices architecture designed for high availability, isolated scalability, and asynchronous analytics processing:
*   **Web Ingress / Frontend:** A React Single Page Application (SPA) served via Nginx, functioning as the user gateway.
*   **Core Backend API Engine:** A Java Spring Boot application managing stateful business logic, transactional verification, and persistent database queries.
*   **Data Processing Microservice:** A FastAPI python engine running mathematical operations and predictive model evaluation isolated from the core API.
*   **Data Tier:** An isolated, persistent relational relational management service.

## Database
The platform has two databases for reliability:
*   **Production Environment:** **PostgreSQL** handles persistent user records, ledger balances, and  transactional history. Database state changes, schemas, and structural alterations are completely controlled using  **Flyway migrations**.
*   **Development / Testing Environments:** A  **H2 Database** is utilized for ephemeral unit testing and automated local isolation verification.

## REST API
The core Java application uses a  structured RESTful interface that acts as the messaging pipeline for the client side interface. 
*   Uses HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`) for clear CRUD operations.
*   Uses JSON formatting exclusively for exchange payloads.
*   Has structural server-side query manipulation using `JpaSpecificationExecutor` for optimized transaction filtering along with strict pagination payloads.

## Authentication & Security
System boundaries are hardened using standard enterprise defense protocols:
*   **Password Cryptography:** User authentication credentials go through intense one-way salted hashing algorithms using the **BCrypt standard** before hitting the persistence layer.
*   **Stateless Authorization:** Secure endpoints enforce an incoming **JWT (JSON Web Token)** validation architecture. Successfull users receive a cryptographically signed token with a fixed 24-hour expiration window which is mapped directly into the security framework (`UserPrincipal`).
*   **Context Isolation:** Application requests pass through custom interceptor chains preventing unauthenticated entity resource cross-contamination.

## Analytics
Advanced data calculation and predictive calculations are fully outsourced to an asynchronous Python environment:
*   **Rolling Aggregations:** Rapid calculations of month spending totals, variable daily spending metrics, and  peak layout alerts.
*   **Machine Learning Forecasting:** Employs a pre-trained **Scikit-Learn Linear Regression model** built on historical data arrays (`Pandas`) to predict, and graph anticipated upcoming monthly expenditures based on personal spending trajectories.

## Testing
Validation is achieved across all application microservices before deployment:
*   **Backend:** Verified via **JUnit 5** integration suites coupled with **Mockito** to securely mock downstream service behaviors.
*   **Analytics Layer:** Unit tested with isolated data frames through the **Pytest** engine.
*   **Frontend UI:** Interaction patterns, hook lifecycles, and functional UI components are evaluated using **Vitest** alongside the **React Testing Library**.

## Docker
The system relies entirely on Docker virtualization for no dependency on local crossplatforms operation:
*   **Service Isolation:** Each layer runs within a minimized container environment mapping only necessary exposed internal networks.
*   **Multi-Container Orchestration:** Standard execution models link the environment configurations, external networking topologies, volume mapping rules, and runtime initialization steps through a single multi-service architecture script.

## CI/CD
Automated pipelines are managed through **GitHub Actions** workflows triggered on every repo push:
*   Runs automatic syntax, formatting, and linting tasks.
*   Executes language test runners across all modules.
*   Triggers build compilation verification.
*   On passing builds, activates remote deployment using automated `appleboy/ssh-action` workflows to run clean server upgrades.

## AWS Deployment
Production target nodes run inside securely architected enterprise cloud foundations:
*   **Compute Engine:** Hosted directly on virtualized **AWS EC2 instances** managed through private security group matrices.
*   **Secrets Masking:** Production tokens, raw system database access combinations, and sensitive parameters are stored and accessed using **AWS Secrets Manager**, avoiding any exposure in hardcoded repository logs.

## Monitoring
Operational metrics, error traces, and real time variables are fully monitored:
*   System runtime status and internal endpoints are exposed via **Spring Boot Actuator**.
*   Metrics collection tools extract and format processing parameters dynamically.
*   Performance variables, application errors, and business graphs are sent directly into **AWS CloudWatch** with customized namespace configurations for a unified system visibility.

## Running Locally

### Prerequisites
Ensure you have the following components installed globally on your machine:
*   **Git**
*   **Docker Desktop** (includes Docker Compose)

### Execution Steps
To build, compile, bundle, and start the entire multi-service ecosystem locally, execute these terminal commands:

```bash
# 1. Clone the repository down to your computer
git clone https://github.com/Aoife-Griffin/cloud-financial-platform.git

# 2. Enter into the root of the project folder
cd cloud-financial-platform

# 3. Fire up the entire ecosystem in detached/background mode with clean container builds
docker compose up --build -d
```

### Expose Port Matrix & Accessible Services
Once your containers initialize successfully, the following microservices will be live and ready for traffic:

| Service Name | Local Port Mapping | Purpose / Target Access |
| :--- | :--- | :--- |
| **Frontend Web App** | `http://localhost:80` | User Web Interface dashboard gateway. |
| **Backend Core Engine** | `http://localhost:8080` | Core Spring Boot REST API database interaction node. |
| **Analytics Service** | `http://localhost:8000` | FastAPI Python engine executing predictive financial analytics. |
| **Database Instance** | `localhost:5432` | Relational PostgreSQL container holding user ledgers (Internal mapping only). |
