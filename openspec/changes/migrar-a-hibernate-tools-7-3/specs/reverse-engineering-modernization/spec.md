## ADDED Requirements

### Requirement: Run Reverse Engineering with Hibernate Tools 7.3

Zathuracode SHALL execute reverse engineering using Hibernate Tools 7.3.0.Final while preserving the `ZcodeMain` entrypoint and current generator configuration contract.

#### Scenario: Execute generation from ZcodeMain

- WHEN a developer runs `org.zcode.generator.ZcodeMain`
- THEN the reverse engineering stage SHALL use Hibernate Tools 7.3.0.Final
- AND the generator SHALL continue using the same external generation properties contract

### Requirement: Use Jakarta Persistence for Temporary Metadata

Zathuracode SHALL load temporary generated entities using `jakarta.persistence` annotations.

#### Scenario: Load metadata from temporary generated entities

- WHEN reverse engineering produces temporary entity classes
- THEN metadata loading SHALL inspect `jakarta.persistence` annotations
- AND the generator SHALL not require `javax.persistence` to read temporary generated entities

### Requirement: Compile Temporary Entities Without Legacy JPA Jar

Zathuracode SHALL compile temporary generated entities without relying on the legacy local JPA jar or `buildCompile.xml`.

#### Scenario: Compile temporary generated entities after reverse engineering

- WHEN Hibernate Tools generates temporary entity sources
- THEN the generator SHALL compile them in process using the active Java runtime
- AND the generator SHALL not require `lib/hibernate-jpa-2.1-api-1.0.2.Final.jar`
- AND the generator SHALL not require `buildCompile.xml`

### Requirement: Support Offline Reverse Engineering XML

Zathuracode SHALL generate reverse engineering XML that does not require remote DTD resolution.

#### Scenario: Run reverse engineering in a restricted network environment

- WHEN Hibernate Tools parses generated `hibernate.cfg.xml` and `hibernate.reveng.xml`
- THEN the generator SHALL not depend on remote DTD downloads to proceed

### Requirement: Keep Generated Project Buildable After Migration

Zathuracode SHALL keep the generated project buildable after the Hibernate Tools 7.3 migration.

#### Scenario: Generate and compile a project after the migration

- WHEN the generator runs successfully against a supported database
- THEN it SHALL generate entities, repositories, DTOs, mappers, services, controllers and support files
- AND the generated project SHALL compile successfully with Maven
