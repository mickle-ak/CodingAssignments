# Task Management System - CRUD Solution

## Project Overview

This is "The CRUD Solution" - a manually created simple CRUD implementation for a take-home assignment. The system is built with an API-first approach using OpenAPI specifications and Spring Boot.

### Assignment Requirements
Full requirements are in `../README.md`:
- Task Management System with tasks having: title, description, status, priority, author, assignee
- User authentication/authorization via email and password, JWT-based API access
- Users can manage their tasks; view other users' tasks; assignees can change task status
- Comments on tasks
- Filtering and pagination for tasks by author/assignee
- API documented using OpenAPI/Swagger, development environment using Docker Compose
- Technologies: Java 17+, Spring Boot, PostgreSQL/MySQL, Spring Security

---

## Build Commands (Maven)

**CRITICAL**: All Maven commands must be run from the `server/` directory, not the project root.

```bash
# Navigate to server directory first
cd server

# Build project and run tests
./mvnw clean install

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ClassName

# Run specific test method
./mvnw test -Dtest=ClassName#methodName

# Package without tests
./mvnw clean package -DskipTests

# Run application
./mvnw spring-boot:run

# Regenerate OpenAPI code (happens automatically during build)
./mvnw clean install
```

### Running Specific Test Types

```bash
cd server

# All tests
./mvnw test

# Only WebMvc tests
./mvnw test -Dtest=*WebMvcTest

# Only integration tests
./mvnw test -Dtest=*OverNetworkTest

# Only Specmatic contract tests
./mvnw test -Dtest=*SpecmaticTest
```

---

## Architecture

### API-First Development

- **OpenAPI Specifications**: YAML specs in `api/v1/` drive all code generation
- **Code Generation Pipeline**: OpenAPI Generator Maven Plugin creates Spring interfaces during build
- **Controllers**: Implement generated interfaces, ensuring compile-time contract enforcement
- **Contract Enforcement**: Any change to OpenAPI YAML requires implementation changes (interface updates cause compile errors)

### Core Architectural Principles

- **JWT Authentication**: Stateless, token validated per-request via `JwtAuthenticationFilter`
- **Business Domain Layer**: Core domain entities (Task, Comment) and business logic (TasksService) represent the problem domain, not infrastructure concerns
- **Multi-Module Maven Project**: `server/` module contains the main Spring Boot application

---

## API Specifications

### Location and Structure

- **Directory**: `api/v1/`
- **Root Spec**: `TaskManagementSystem.yaml` (orchestrates all specs via `$ref`)
- **Domain Specs**:
  - `tasks.yaml` - Task management endpoints
  - `login.yaml` - Authentication endpoints
  - `comments.yaml` - Comment endpoints
  - `error-responses.yaml` - Error response schemas

### Generated Code

- **Location**: `target/generated-sources/openapi/`
- **Package**: `org.mickleak.taskmanagementsystem.server.api.v1.*`
- **Generated Artifacts**: API interfaces, model POJOs, delegate pattern interfaces

### Workflow

1. Modify OpenAPI YAML specifications
2. Run `./mvnw clean install` (from `server/` directory)
3. Generated interfaces update automatically
4. Implement/update controllers to match new interfaces
5. Compiler enforces contract compliance

---

## Testing Strategy

Four-tier testing approach for comprehensive coverage:

### Tier 1: Unit Tests (`*Test.java`)
- Pure Mockito-based tests
- No Spring context
- Fast execution
- Focus on business logic

### Tier 2: WebMvc Tests (`*WebMvcTest.java`)
- Use `@WebMvcTest` annotation
- MockMvc for HTTP layer testing
- Mocked services
- Security configuration included

### Tier 3: Integration Tests (`*OverNetworkTest.java`)
- `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- REST Assured for real HTTP requests
- No database (excludes `DataSourceAutoConfiguration` for speed)
- Full application context

### Tier 4: Contract Tests (`*SpecmaticTest.java`)
- Validates OpenAPI contract with generative testing
- Ensures implementation matches specification
- Catches contract violations

---

## Specmatic Contract Testing

### Configuration

- **Framework**: Specmatic 2.7.6 with JUnit 5 integration
- **Test Class**: `OpenApiSpecificationSpecmaticContractTest`
- **Config File**: `server/specmatic.yaml`

### How It Works

1. Reads OpenAPI YAML specifications
2. Generates random request combinations based on schema
3. Sends requests to running application
4. Validates responses match OpenAPI specification

### Environment Variables

```bash
# Enable generative testing
SPECMATIC_GENERATIVE_TESTS=true

# Parallel test execution
SPECMATIC_TEST_PARALLELISM=auto
```

### JWT Token Injection

- JWT token injected via system property for authenticated endpoints
- Configured in test setup

### Backward Compatibility Check

```bash
cd server
java -jar lib/specmatic-2.7.6.jar backward-compatibility-check
```

---

## Authentication Flow

### JWT-Based Authentication

1. **Login**: POST `/api/v1/login` with username/password (unauthenticated endpoint)
2. **Validation**: `LoginController` validates credentials via Spring Security's `AuthenticationManager`
3. **Token Generation**: On success, `JwtTokenProvider` creates JWT with 10-hour expiration
4. **Subsequent Requests**: Include `Authorization: Bearer <token>` header
5. **Token Validation**: `JwtAuthenticationFilter` extracts/validates token, sets `SecurityContext`

### Test Users (In-Memory)

- **User**: `user` (ROLE_USER), password: `secret`
- **User2**: `user2` (ROLE_USER), password: `secret`
- **Admin**: `admin` (ROLE_ADMIN), password: `secret`

### Key Components

- `WebSecurityConfig.java` - Security rules and filter chain
- `JwtAuthenticationFilter.java` - Token extraction and validation
- `JwtTokenProvider.java` - Token creation and parsing
- `LoginController.java` - Authentication endpoint

---

## Key Architectural Decisions

### Strict Type Deserialization

- **Configuration**: `JacksonConfiguration`
- **Behavior**: Rejects non-string values for String fields
- **Example**: `"title": 123` will fail validation (prevents type confusion)

### Stateless JWT

- **Secret Key**: Generated per-application startup
- **No Session State**: All authentication state in JWT token
- **Expiration**: 10 hours (configurable in `application.properties`)

### Database Tests Disabled by Default

- **Reason**: Speed - most tests exclude `DataSourceAutoConfiguration`
- **When Needed**: Use Testcontainers with `@ServiceConnection` annotation

### Testcontainers Pattern

- **MySQL Container**: Auto-provisioned when database tests needed
- **Annotation**: `@ServiceConnection` for automatic connection configuration
- **Clean State**: Fresh container per test run

---

## Important Files

### Configuration

- `server/pom.xml` - Maven configuration, OpenAPI Generator plugin
- `server/src/main/resources/application.properties` - API version, base path, JWT expiration
- `server/specmatic.yaml` - Specmatic contract testing configuration

### Security

- `server/src/main/java/org/mickleak/taskmanagementsystem/server/configuration/WebSecurityConfig.java` - Security rules
- `server/src/main/java/org/mickleak/taskmanagementsystem/server/configuration/JacksonConfiguration.java` - JSON deserialization rules

### Database

- `server/src/main/resources/db/changelog/db.changelog-master.yaml` - Liquibase migrations (currently empty)
- `server/compose.yaml` - MySQL Docker container definition

### API Specifications

- `api/v1/TaskManagementSystem.yaml` - Root OpenAPI specification
- `api/v1/tasks.yaml` - Task endpoints
- `api/v1/login.yaml` - Authentication endpoints
- `api/v1/comments.yaml` - Comment endpoints
- `api/v1/error-responses.yaml` - Error schemas

---

## Current Implementation Status

### ✅ Completed

- OpenAPI specifications for all endpoints
- Code generation pipeline
- Controller implementations (API contracts)
- JWT authentication and authorization
- Four-tier testing strategy
- Specmatic contract testing
- In-memory user authentication

### 🚧 In Progress / TODO

- **Database Layer**: `TasksServiceImpl` currently returns hardcoded dummy data
- **Database Schema**: Liquibase migrations not yet defined
- **Persistence**: No JPA entities or repositories implemented

### Architecture Note

The current architecture allows database layer implementation without changing controllers. The service layer interface is already defined and used by controllers, so adding real database persistence only requires:

1. Define Liquibase migrations
2. Create JPA entities
3. Create Spring Data repositories
4. Update `TasksServiceImpl` to use repositories instead of dummy data

---

## Project Structure Context

- **Implementation Type**: "The CRUD Solution" - manually created simple CRUD
- **Parent Directory**: `../README.md` contains full assignment specification
- **Multi-Module**: Maven project with `server/` as main module
- **Related Implementations**: This is one of several implementations of the same assignment

---

## Common Development Tasks

### Add New Endpoint

1. Update relevant OpenAPI YAML in `api/v1/`
2. Run `./mvnw clean install` from `server/`
3. Implement new methods in corresponding controller
4. Add tests across all four tiers
5. Verify with Specmatic contract tests

### Modify Existing Endpoint

1. Update OpenAPI YAML specification
2. Rebuild to regenerate interfaces
3. Fix compilation errors in controller
4. Update all test tiers
5. Run contract tests to verify

### Add New Entity

1. Create JPA entity class
2. Create Spring Data repository
3. Update service layer
4. Add to OpenAPI models (if exposed via API)
5. Add database migration in Liquibase changelog

### Run Application Locally

```bash
# Start MySQL container
cd server
docker compose up -d

# Run application
./mvnw spring-boot:run

# Access API
# Base URL: http://localhost:8080/api/v1
# Swagger UI: http://localhost:8080/swagger-ui.html
```

---

## Troubleshooting

### Build Fails After OpenAPI Changes

- Check YAML syntax in `api/v1/*.yaml`
- Verify `$ref` paths are correct
- Look for conflicting schema definitions
- Run `./mvnw clean install -X` for detailed output

### Tests Fail with Authentication Errors

- Verify JWT token is correctly generated in tests
- Check `JwtAuthenticationFilter` configuration
- Ensure test users are configured in `WebSecurityConfig`

### Specmatic Tests Fail

- Check OpenAPI spec matches implementation exactly
- Verify response schemas include all required fields
- Review Specmatic logs for specific violations
- Ensure JWT token is injected for authenticated endpoints

### Maven Commands Not Working

- **Ensure you're in `server/` directory** - this is the most common issue
- Verify Maven wrapper scripts are executable
- Check Java version is 17 or higher
