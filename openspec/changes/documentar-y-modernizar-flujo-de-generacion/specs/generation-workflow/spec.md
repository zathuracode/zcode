## ADDED Requirements

### Requirement: Document Supported Generation Workflow

Zathuracode SHALL document the supported generation workflow from generator startup to generated project validation.

#### Scenario: Team members review the supported flow

- WHEN a developer consults the generation workflow documentation
- THEN the documentation SHALL describe the stages of configuration, reverse engineering, metadata loading, templating, formatting, and generated project validation
- AND the documentation SHALL identify `ZcodeMain` as the supported entrypoint for end-to-end generation

### Requirement: Define Operational Prerequisites

Zathuracode SHALL document the minimum operational prerequisites needed to run the generator against a known database.

#### Scenario: A developer prepares a local execution environment

- WHEN a developer prepares to execute the generator
- THEN the documentation SHALL list the required Java and Maven runtime expectations
- AND the documentation SHALL mention the need for local generation configuration and reachable target database connectivity

### Requirement: Require End-to-End Validation for Generation Flow Changes

Zathuracode SHALL require end-to-end validation when a change affects the generation flow, templates, formatter, or runtime dependencies.

#### Scenario: A contributor proposes a modernization to the generation flow

- WHEN a change modifies reverse engineering, metadata processing, templating, formatting, or generation dependencies
- THEN the change SHALL include validation of generator compile and package steps
- AND the change SHALL include direct execution of `org.zcode.generator.ZcodeMain`
- AND the change SHALL include compilation of the generated project as part of acceptance criteria

### Requirement: Preserve Incremental Modernization Boundaries

Zathuracode SHALL treat large framework migrations as separate changes from incremental modernization of the current flow.

#### Scenario: A contributor evaluates a major framework migration

- WHEN a proposed modernization requires migration to Hibernate 6 or a broad move from `javax.*` to `jakarta.*`
- THEN the work SHALL be proposed as a separate OpenSpec change
- AND the current change SHALL remain focused on documenting and safeguarding the supported workflow
