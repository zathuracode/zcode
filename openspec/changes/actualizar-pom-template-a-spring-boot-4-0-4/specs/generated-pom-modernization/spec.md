## ADDED Requirements

### Requirement: Generate Projects with Spring Boot 4.0.4

Zathuracode SHALL generate projects whose parent pom uses Spring Boot `4.0.4`.

#### Scenario: Generate a project from the SkyJet template

- WHEN the generator renders `pom.xml` from `templates/skyJet/pom.xml.vm`
- THEN the generated project SHALL use `org.springframework.boot:spring-boot-starter-parent:4.0.4`

### Requirement: Prefer Spring Boot BOM Version Management

Zathuracode SHALL prefer dependency version management from the Spring Boot BOM whenever the dependency is already managed there.

#### Scenario: Render managed dependencies

- WHEN the template renders a dependency that Spring Boot 4.0.4 already manages
- THEN the generated dependency SHALL omit an explicit `<version>` unless the build needs one outside the BOM contract

### Requirement: Keep External Libraries Explicit and Compatible

Zathuracode SHALL keep explicit versions only for external libraries that are not fully managed by Spring Boot and that are required by the generated project build.

#### Scenario: Render external libraries in the generated pom

- WHEN the template renders external libraries such as `springdoc`, `mapstruct`, `jjwt`, `pitest`, or `clover`
- THEN the generated pom SHALL use explicit versions only where needed
- AND those explicit versions SHALL remain compatible with Spring Boot `4.0.4`

### Requirement: Generated Project Must Compile After Template Update

Zathuracode SHALL keep the generated project buildable after updating the pom template.

#### Scenario: Validate a generated project after the template update

- WHEN `ZcodeMain` generates a project using the updated template
- THEN `mvn -DskipTests compile` SHALL succeed
- AND `mvn -DskipTests test-compile` SHALL succeed
