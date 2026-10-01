# Cloud Financial Management Platform — Security Review Checklist

## 1. Authentication & Session Security
*   [x] **Cryptographic Password Hashing:** Passwords are safely scrambled using BCrypt before saving. Raw text passwords are never stored or logged.
*   [x] **Stateless JWT Authorization:** Protected application resources require a valid JSON Web Token passed inside the HTTP `Authorization: Bearer <token>` request header. 
*   [x] **Token Lifespan Restrictions:** Access tokens expire exactly after 24 hours to prevent unauthorized session reuse.
*   [x] **Endpoint Route Hardening:** The global `SecurityFilterChain` automatically blocks unauthenticated requests to all financial, transaction, and analytics routes.


## 2. Resource Authorization & Multi-Tenant Isolation
*   [x] **Data Ownership Assertions:** Every data request is tied directly to the currently logged-in user's identity.
*   [x] **Cross-Tenant Request Rejection:** Users cannot view, modify, or delete another person's data. Manipulating IDs in the URL triggers an immediate error.
*   [x] **Account-to-User Linkage:** Financial accounts check ownership boundaries at the database layer before allowing balance transfers or updates.


## 3. Strict Input Validation
*   [x] **Financial Value Bounds:** Transaction amounts and budgets use `@Min` and `@Positive` rules to block negative values or number overflow attacks.
*   [x] **Structural Date Parsing:** Timestamps must match standard ISO formats to block text injection and broken time data.
*   [x] **Payload Presence Guards:** Critical creation forms enforce required fields using `@NotNull` and `@NotBlank` before processing the data.

## 4. Database Layer Defense
*   [x] **SQL Injection Prevention:** Core application persistence relies entirely on Spring Data JPA, `JpaSpecificationExecutor`, and named repository queries. 
*   [x] **Parameterization Enforcement:** Database interactions use binding instead of raw, dynamic SQL strings, completely neutralizing SQL injection vectors.
*   [x] **Private Network Isolation:** The live PostgreSQL database runs inside a private network, communicating only with the backend API instead of being exposed to the internet.

## 5. Secret & Credential Lifecycle Management
*   [x] **Repository Decoupling:** Plain-text cloud keys have been completely removed from configuration files.
*   [x] **Environment Variable Injection:** Local developer setups load keys dynamically with safe fallback defaults. (`${AWS_ACCESS_KEY_ID:mock-local-key}`).
*   [x] **Production Cloud Vaulting:** The live app safely pulls keys, database links, and tokens from AWS Secrets Manager using secure IAM profiles.
*   [x] **Credential Rotation:** Any previously exposed keys have been fully deactivated and replaced in the cloud provider dashboard.


## 6. Network & Transport Security
*   [x] **Cross-Origin Resource Sharing (CORS):** Spring Security restricts browser requests to trusted domains only, blocking data theft from outside websites.
*   [x] **Transport Layer Encryption (HTTPS):**  Public web traffic is encrypted end-to-end using TLS, passing safely through an `Nginx` reverse proxy on port 443.

## 7. Informational Leakage & Error Resilience
*   [x] **Global Exception Masking:**  Internal errors are caught globally by a central controller rule.
*   [x] **Abstraction of Stack Traces:**  Raw database errors, code tracebacks, and system configurations are hidden from the user.
*   [x] **Sanatized API Feedback:** External users only see clean, generic error messages with a tracking ID and timestamp.