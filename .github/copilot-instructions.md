# GitHub Copilot Instructions for xAPI Java

## Project Overview

xAPI Java is a library that helps you create applications that send or receive xAPI Statements or Documents. This is a Maven-based monorepo containing multiple projects:

- **xapi-model**: Core data models for xAPI statements, actors, verbs, activities, and related objects
- **xapi-client**: Client library for communicating with Learning Record Stores (LRS)
- **xapi-model-spring-boot-starter**: Spring Boot autoconfiguration for xAPI model validation
- **samples**: Example applications demonstrating xAPI client usage

## Technology Stack

- **Language**: Java 25 or newer (required)
- **Build Tool**: Maven (using Maven Wrapper `./mvnw`)
- **Framework**: Spring Boot 3.5.7, Spring WebClient (reactive)
- **Code Style**: Google Java Style Guide enforced via CheckStyle
- **Testing**: JUnit 5 (Jupiter), Hamcrest matchers
- **Code Generation**: Lombok (with specific configurations)
- **Validation**: Jakarta Bean Validation
- **Serialization**: Jackson (with custom modules for strict xAPI compliance)

## Environment Setup

### Java Installation with SDKMAN

This project requires Java 25. We recommend using [SDKMAN!](https://sdkman.io/) to manage Java versions:

```bash
# Install SDKMAN (if not already installed)
curl -s "https://get.sdkman.io" | bash

# Install Java 25 (Temurin distribution recommended)
sdk install java 25.0.1-tem

# Set as default (optional)
sdk default java 25.0.1-tem

# Verify installation
java -version
```

**Note**: The exact identifier (e.g., `25.0.1-tem`) may vary by platform and availability. Use `sdk list java` to see available Java 25 versions for your system.

## Building and Testing

### Build Commands

```bash
# Clean build with tests
./mvnw clean verify

# Build without tests
./mvnw clean verify -DskipTests

# Run tests only
./mvnw test

# Run integration tests
./mvnw verify
```

### Before Making Changes

Always run the full build before starting work to ensure you understand the current state:

```bash
./mvnw clean verify
```

## Code Style and Quality

### Style Guide

- **Strictly follow the Google Java Style Guide**
- CheckStyle is configured and enforced in the build
- Code is automatically scanned with SonarCloud
- CodeQL performs security vulnerability scanning

### Validation

- CheckStyle validation runs automatically during Maven build
- Over 300 unit tests ensure xAPI specification conformance
- Sonar performs automatic pull request reviews

## Development Guidelines

### Immutability and Fluent Interface

All xAPI model objects are **immutable** and use a **fluent interface** pattern:

```java
Statement statement = Statement.builder()
    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
    .verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();
```

### Creating Modified Versions

Use `toBuilder()` to create modified versions of immutable objects:

```java
Statement completedStatement = attemptedStatement.toBuilder()
    .verb(Verb.COMPLETED)
    .build();
```

### Lombok Usage

- All model classes use Lombok for reducing boilerplate
- Common annotations: `@Builder`, `@Value`, `@Getter`, `@With`
- Lombok config is set in `lombok.config` files
- Constructor properties are added for deserialization compatibility

### Validation

Models use Jakarta Bean Validation annotations:

- `@NotNull` for required fields
- Custom validators in `dev.learning.xapi.model.validation.constraints`
- Validation can be programmatically executed or automatic in Spring controllers with `@Valid`

### Jackson Serialization

Custom Jackson modules provide strict xAPI compliance:

- `XapiStrictLocaleModule`: Validates locale formats
- `XapiStrictNullValuesModule`: Handles null value validation
- `XapiStrictObjectTypeModule`: Validates objectType fields
- `XapiStrictTimestampModule`: Validates timestamp formats

## Testing Conventions

### Test Structure

- Tests use JUnit 5 (`@Test`, `@DisplayName`)
- Hamcrest matchers for assertions (`assertThat`, `is`, `notNullValue`, etc.)
- JSON path matchers for verifying serialization: `hasJsonPath`, `hasNoJsonPath`

### Example Test Pattern

```java
@Test
@DisplayName("When Statement Has All Properties Then Serialization Works")
void testStatementSerialization() throws JsonProcessingException {
    Statement statement = Statement.builder()
        .agentActor(a -> a.name("Test User").mbox("mailto:test@example.com"))
        .verb(Verb.COMPLETED)
        .activityObject(o -> o.id("https://example.com/activity/1"))
        .build();

    String json = objectMapper.writeValueAsString(statement);

    assertThat(json, hasJsonPath("$.actor.name", is("Test User")));
    assertThat(json, hasJsonPath("$.verb.id"));
}
```

### Test Coverage

- Comprehensive unit tests for all model classes
- Integration tests for client functionality
- Tests verify xAPI specification compliance
- Use MockWebServer (OkHttp) for testing HTTP interactions in xapi-client

## Module-Specific Guidelines

### xapi-model

- Contains all xAPI data model classes (Statement, Actor, Verb, Activity, etc.)
- All classes are immutable with builder pattern
- Extensive validation annotations
- Custom validators for xAPI-specific rules
- Located in `xapi-model/src/main/java/dev/learning/xapi/model/`

### xapi-client

- Spring WebClient-based reactive client
- Implements xAPI Statement, State, Agent Profile, and Activity Profile resources
- Auto-configuration via Spring Boot
- Uses fluent request builders
- Located in `xapi-client/src/main/java/dev/learning/xapi/client/`

### xapi-model-spring-boot-starter

- Provides Spring Boot autoconfiguration
- Configurable validation rules via properties (prefix: `xapi.model.`)
- All validation rules default to TRUE (strict mode)
- Located in `xapi-model-spring-boot-starter/src/main/java/`

### samples

- Example applications demonstrating client usage
- Each sample shows a specific xAPI operation
- Use samples as reference for common patterns
- Located in `samples/` directory

## Common Patterns

### Creating Agents

```java
Agent agent = Agent.builder()
    .name("John Doe")
    .mbox("mailto:john.doe@example.com")
    .build();
```

### Creating Groups

```java
Group group = Group.builder()
    .name("Team A")
    .addMember(m -> m.name("Member 1").mbox("mailto:member1@example.com"))
    .addMember(m -> m.name("Member 2").mbox("mailto:member2@example.com"))
    .build();
```

### Using Language Maps

```java
LanguageMap names = LanguageMap.builder()
    .addEntry(Locale.ENGLISH, "English Name")
    .addEntry(Locale.FRENCH, "Nom Français")
    .build();
```

## Important Constraints

### xAPI Specification Compliance

- All data must conform to the xAPI specification
- IRI/URI fields must include schemes (e.g., `https://`, `mailto:`)
- Timestamps follow ISO 8601 format
- UUIDs must be variant 4
- Score values have specific ranges (-1 to 1 for scaled scores)
- Voided statements must reference other statements

### Memory Limits

When working with xapi-client:

- Default Spring WebClient buffer limit: 256KB
- May need to increase for large statement sets or attachments
- Configure via `spring.codec.max-in-memory-size` property

## CI/CD

### GitHub Actions Workflows

- **maven_push.yml**: Runs on push to main (build, test, Sonar scan)
- **maven_pull_request.yml**: Runs on PRs (build, test, conditional Sonar scan)
- **codeql.yml**: Security scanning
- **maven-publish.yml**: Maven Central publishing

### Quality Gates

- All builds must pass CheckStyle validation
- All tests must pass
- SonarCloud quality gate must pass
- CodeQL security checks must pass

## Copyright and Licensing

- All source files include copyright header: `Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.`
- Project is licensed under Apache License 2.0
- Maintain copyright headers when creating new files

## Tips for Effective Contributions

1. **Read the xAPI specification** when working with model classes
2. **Use existing patterns** - the codebase has consistent patterns for builders, validation, and testing
3. **Write tests first** - the project has excellent test coverage, maintain it
4. **Keep immutability** - never add mutable state to model objects
5. **Follow fluent patterns** - builders should support method chaining
6. **Validate strictly** - default to strict xAPI compliance unless explicitly configured otherwise
7. **Document public APIs** - use JavaDoc for public methods and classes
8. **Check samples** - refer to sample applications for usage patterns

## Getting Help

- README.md contains detailed usage examples
- Each module has comprehensive JavaDoc
- Samples folder demonstrates common use cases
- xAPI specification: https://github.com/adlnet/xAPI-Spec
