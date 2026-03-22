## ADDED Requirements

### Requirement: Generate Safer Application Defaults

Zathuracode SHALL generate application configuration without embedded real credentials or hardcoded JWT secrets.

#### Scenario: Generate development configuration

- WHEN Zathuracode generates `application.properties` and `application-dev.properties`
- THEN datasource credentials SHALL be expressed as placeholders or environment-backed values
- AND JWT issuer, secret and expiration SHALL be configurable from properties
- AND actuator exposure SHALL be limited to operational endpoints by default

### Requirement: Generate REST Controllers with Explicit HTTP Contracts

Zathuracode SHALL generate REST controllers with more explicit HTTP responses.

#### Scenario: Fetch an entity that does not exist

- WHEN a generated controller receives a `findById` request for a missing resource
- THEN it SHALL translate that condition into `404 Not Found`

#### Scenario: Create a new entity

- WHEN a generated controller persists a new resource
- THEN it SHALL return `201 Created`
- AND it SHALL include a `Location` header for the created resource

#### Scenario: Delete an existing entity

- WHEN a generated controller deletes a resource
- THEN it SHALL return `204 No Content`

### Requirement: Generate Consistent Error Responses

Zathuracode SHALL generate a consistent error handling contract for validation, business and unexpected errors.

#### Scenario: A request payload is invalid

- WHEN a generated controller receives invalid input
- THEN the generated exception handler SHALL return a `ProblemDetail` response
- AND it SHALL include structured validation details

#### Scenario: A resource is not found

- WHEN generated business logic raises a resource-not-found condition
- THEN the generated exception handler SHALL return `404 Not Found`

### Requirement: Generate Constructor-Based Services and Controllers

Zathuracode SHALL generate services and controllers using constructor injection and simplified package layout.

#### Scenario: Generate service and controller classes

- WHEN Zathuracode generates service and controller code
- THEN generated classes SHALL live under `service` and `controller`
- AND generated dependencies SHALL be injected via constructors
- AND generated services SHALL not include redundant singleton scope annotations

### Requirement: Generate Minimal Automated Tests

Zathuracode SHALL generate a minimal automated test set that compiles and executes without requiring a real database.

#### Scenario: Build a generated project in CI

- WHEN the generated project runs `mvn test`
- THEN it SHALL include at least one context/configuration smoke test
- AND it SHALL include at least one mapper test
- AND it SHALL include at least one controller test covering `404 Not Found`

### Requirement: Avoid Generated Value Strategies for String IDs

Zathuracode SHALL not generate `@GeneratedValue` for primary keys of type `String`.

#### Scenario: Generate an entity with a String primary key

- WHEN a generated entity has a primary key of type `String`
- THEN the generated entity SHALL not include `@GeneratedValue`
