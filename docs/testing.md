# Cloud Financial Management Platform — Testing Strategy

## 1. Backend Unit Tests
The core Java application uses JUnit 5 and Mockito to perform isolated, low-level testing of the business logic layer without triggering the active network or database.

Structure:
Service Layer --> Mock Repositories --> Verify Business Logic

Implementation Details:
* The database layer is replaced with fake data using Mockito, so the tests always get the exact same results
* The tests inject these mocks into the service layer using `@InjectMocks`. This allows us to test different scenarios, like balance calculations and transaction rules
* • The assertions strictly check boundary limits. This ensures that errors like negative amounts or empty data fail exactly as expected.

## 2. Integration Tests
Integration testing checks how the entire Spring Boot system works together from end to end using a fast, temporary test environment.

Structure:
Controller --> Service --> Repository --> Database (H2)

Implementation Details:
* Test Environment: Uses `@SpringBootTest` and `@AutoConfigureMockMvc` to simulate a running web server without launching a real one.
* Database: Uses an isolated, in-memory H2 database that builds a fresh schema for each test run instead of using the live database.
*What is Tested: Uses `MockMvc` to send fake web requests to test JWT security filters, HTTP status codes, list pagination, and whether data successfully saves to the database.


## 3. Analytics Tests

The Python FastAPI service uses Pytest to check data math, formatting, and machine learning prediction accuracy.
Implementation Details:
* Test Data: Uses fixed NumPy arrays and Pandas DataFrames to make sure test data never changes.
* Math Logic: Tests historical averages, month-to-date spending, and edge cases like calculating averages with zero transactions.
* Machine Learning: Verifies that the Scikit-Learn Linear Regression model reads data shapes correctly and predicts reasonable decimal numbers for future months.

The Python FastAPI microservice relies on Pytest to validate analytics aggregations, data normalization logic, and machine learning prediction consistency.


## 4. Frontend Tests
The React frontend uses Vitest and React Testing Library to make sure the layout, user actions, and app state behave consistently.
Implementation Details:
* UI Isolation: Renders and tests individual UI components by themselves in a simulated browser environment.
* Network Mocking: Intercepts real network requests using Mock Service Worker (MSW) to test how the screen handles different backend responses.
* User Actions: Checks form inputs, button toggles, and specific alerts—like the warning that triggers when a budget passes 80% of its limit.

## 5. CI Testing
Continuous Integration (CI) automated checks prevent broken code from being merged into production branches.
Implementation Details:
* Automation: Every code push triggers a GitHub Actions workflow.
* Environments: The runner sets up Java, Python, and Node environments at the same time to test all parts of the app.
* Guardrails: Code cannot be merged until it passes code formatting rules (linters), compiles without errors, and passes every single test.

## 6. Test Suite Execution Logs
All tests successfully passed during the local run. Here is the summary of the command-line outputs:

### Backend Architecture Tests (Java/Spring Boot)
Command: ./mvnw test
[INFO] Scanning for projects...
[INFO] Running com.finance.service.TransactionServiceTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.842 s
[INFO] Running com.finance.controller.AccountControllerIntegrationTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.103 s
[INFO] 
[INFO] BUILD SUCCESS
[INFO] Total time:  12.481 s
[INFO] Finished at: 2026-10-01T15:52:14Z

### Analytics Service Tests (Python/FastAPI)
Command: pytest
============================= test session starts ==============================
platform linux -- Python 3.12.3, pytest-8.1.1, pluggy-1.4.0
rootdir: /app/analytics
collected 14 items

tests/test_calculations.py ............                                  [ 85%]
tests/test_forecasting.py ..                                             [100%]

============================== 14 passed in 0.98s ==============================

### Frontend Interface Tests (React/TypeScript)
Command: npm run test:run
> vitest run

 RUN  v1.4.0 /app/frontend

 ✓ src/components/Dashboard.test.tsx (3 tests)
 ✓ src/hooks/useAuth.test.ts (4 tests)
 ✓ src/components/TransactionForm.test.tsx (5 tests)

 Test Files  3 passed (3)
      Tests  12 passed (12)
   Start at  15:53:02
   Duration  2.41s (transform 410ms, setup 180ms)
