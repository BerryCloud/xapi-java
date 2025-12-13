# Contributing to xAPI Java

Thank you for your interest in contributing to xAPI Java! This document provides guidelines and instructions for contributing to this project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Setting Up Your Development Environment](#setting-up-your-development-environment)
- [Development Workflow](#development-workflow)
  - [Building the Project](#building-the-project)
  - [Running Tests](#running-tests)
  - [Code Style and Formatting](#code-style-and-formatting)
- [Making Changes](#making-changes)
  - [Creating a Branch](#creating-a-branch)
  - [Writing Code](#writing-code)
  - [Writing Tests](#writing-tests)
  - [Documentation](#documentation)
- [Submitting Changes](#submitting-changes)
  - [Pull Request Process](#pull-request-process)
  - [Pull Request Checklist](#pull-request-checklist)
  - [Code Review Process](#code-review-process)
- [Project Structure](#project-structure)
- [Additional Resources](#additional-resources)

## Code of Conduct

This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to conduct@berrycloud.co.uk.

## Getting Started

### Prerequisites

xAPI Java requires **Java 17 or newer**.

#### Installing Java 17

We recommend using [SDKMAN!](https://sdkman.io/) to install and manage Java versions:

```bash
# Install SDKMAN (if not already installed)
curl -s "https://get.sdkman.io" | bash

# Install Java 17 (Temurin distribution recommended)
sdk install java 17.0.13-tem

# Verify installation
java -version
```

**Note**: The exact identifier (e.g., `17.0.13-tem`) may vary by platform and availability. Run `sdk list java` to see available Java 17 versions for your system and choose the appropriate one for your platform.

### Setting Up Your Development Environment

1. **Fork the repository** on GitHub

2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/xapi-java.git
   cd xapi-java
   ```

3. **Add the upstream repository** as a remote:
   ```bash
   git remote add upstream https://github.com/BerryCloud/xapi-java.git
   ```

4. **Verify your setup**:
   ```bash
   ./mvnw clean verify
   ```

## Development Workflow

### Building the Project

xAPI Java uses Maven with the Maven Wrapper (`./mvnw`). The following build commands are available:

```bash
# Clean build with tests
./mvnw clean verify

# Build without tests (faster, for quick checks)
./mvnw clean verify -DskipTests

# Run tests only
./mvnw test

# Run integration tests
./mvnw verify
```

**Best Practice**: Always run the full build (`./mvnw clean verify`) before starting work to ensure you understand the current state of the project.

### Running Tests

xAPI Java has over 300 unit tests ensuring conformance with the xAPI specification. Tests use:
- **JUnit 5 (Jupiter)** for test framework
- **Hamcrest matchers** for assertions
- **MockWebServer (OkHttp)** for testing HTTP interactions in xapi-client

To run tests:

```bash
# Run all tests
./mvnw test

# Run tests for a specific module
./mvnw test -pl xapi-model

# Run a specific test class
./mvnw test -Dtest=StatementTest
```

### Code Style and Formatting

xAPI Java strictly follows the **[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)**.

#### Automated Formatting

The project uses the **[Spotify fmt-maven-plugin](https://github.com/spotify/fmt-maven-plugin)** to automatically format Java code according to the Google Java Style Guide.

**Recommended: Install Git Hooks**

To automatically format your code before each commit, install the pre-commit hook:

```bash
./install-git-hooks.sh
```

This ensures your code is always formatted correctly before committing. The hook runs `./mvnw com.spotify.fmt:fmt-maven-plugin:format` automatically.

**Manual Formatting**

If you prefer not to use the git hook, you can manually format your code at any time:

```bash
# Format all Java files in the project
./mvnw com.spotify.fmt:fmt-maven-plugin:format
```

**Important**: Run the formatter before committing your changes to avoid formatting issues in pull requests.

#### Automated Enforcement

- **[CheckStyle](https://checkstyle.sourceforge.io)** is configured to enforce the Google Java Style Guide automatically during the build
- CheckStyle validation runs as part of `./mvnw verify`
- **All CheckStyle violations must be resolved before submitting a pull request**

#### Code Quality Tools

The project uses several automated code quality tools:

- **[SonarCloud](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java)**: Performs automatic pull request reviews and tracks code quality metrics
- **[CodeQL](https://codeql.github.com)**: Scans for security vulnerabilities
- **JaCoCo**: Measures code coverage (viewable in SonarCloud)

These tools run automatically on pull requests via GitHub Actions.

#### Key Style Points

- Use **Lombok** annotations to reduce boilerplate (`@Builder`, `@Value`, `@Getter`, etc.)
- All model classes are **immutable** with **fluent interface** patterns
- Use **Jakarta Bean Validation** annotations for validation (`@NotNull`, custom validators)
- Follow existing code patterns and conventions in the module you're modifying

## Making Changes

### Creating a Branch

Create a feature branch from the latest `main`:

```bash
git checkout main
git pull upstream main
git checkout -b feature/your-feature-name
```

Use descriptive branch names:
- `feature/add-something` for new features
- `fix/issue-number-description` for bug fixes
- `docs/update-readme` for documentation changes
- `chore/update-dependencies` for maintenance tasks

### Writing Code

#### Follow the Fluent Interface Pattern

All xAPI model objects are **immutable** and use a **fluent interface** pattern:

```java
Statement statement = Statement.builder()
    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
    .verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();
```

To create modified versions of immutable objects, use `toBuilder()`:

```java
Statement completedStatement = attemptedStatement.toBuilder()
    .verb(Verb.COMPLETED)
    .build();
```

#### Validation

- Use Jakarta Bean Validation annotations for model validation
- Custom validators are in `dev.learning.xapi.model.validation.constraints`
- Ensure validation conforms to the [xAPI specification](https://github.com/adlnet/xAPI-Spec)

#### Jackson Serialization

Custom Jackson modules ensure strict xAPI compliance:
- `XapiStrictLocaleModule`: Validates locale formats
- `XapiStrictNullValuesModule`: Handles null value validation
- `XapiStrictObjectTypeModule`: Validates objectType fields
- `XapiStrictTimestampModule`: Validates timestamp formats

### Writing Tests

**All new functionality must include tests.**

#### Test Structure

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

#### Test Guidelines

- Use JUnit 5 annotations (`@Test`, `@DisplayName`)
- Use Hamcrest matchers for assertions (`assertThat`, `is`, `notNullValue`)
- Use JSON path matchers for verifying serialization (`hasJsonPath`, `hasNoJsonPath`)
- Ensure tests verify xAPI specification compliance
- Follow existing test patterns in the module

### Documentation

#### JavaDoc Guidelines

All public APIs must be documented with comprehensive JavaDoc comments:

- **Classes and interfaces**: Include a brief description and link to relevant xAPI specification sections
- **Public methods**: Document purpose, parameters (`@param`), return values (`@return`), and exceptions (`@throws`)
- **Validation annotations**: Include `@return` tags for `message()`, `groups()`, and `payload()` methods
- **Constructor parameters**: Document all constructor parameters with `@param` tags

**Important**: Lombok-generated code (builders, constructors) may produce unavoidable javadoc warnings. These are suppressed via Maven configuration and do not need manual documentation.

Example:
```java
/**
 * Gets a Statement from the LRS.
 * <p>
 * The returned ResponseEntity contains the response headers and the Statement.
 * </p>
 *
 * @param request the get statement request
 *
 * @return the ResponseEntity
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">xAPI GET Statements</a>
 */
public Mono<ResponseEntity<Statement>> getStatement(GetStatementRequest request) {
  // implementation
}
```

**Generating JavaDoc**: Run `./mvnw javadoc:javadoc` to verify documentation quality.

- **README**: Update if your changes affect usage examples or getting started instructions
- **Code Comments**: Add comments only when necessary to explain complex logic (match existing style)
- Maintain copyright headers in all source files: `Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.`

## Submitting Changes

### Pull Request Process

1. **Ensure all tests pass locally**:
   ```bash
   ./mvnw clean verify
   ```

2. **Commit your changes** with clear, descriptive commit messages:
   ```bash
   git commit -m "Add feature: brief description of change"
   ```

3. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

4. **Create a pull request** on GitHub from your fork to the `main` branch of `BerryCloud/xapi-java`

5. **Fill out the pull request template** completely, including:
   - Summary of changes
   - Issue number being resolved
   - Checklist confirmation

### Pull Request Checklist

Before submitting your pull request, verify:

- [ ] Code has been formatted using `./mvnw com.spotify.fmt:fmt-maven-plugin:format` (or use the git hook)
- [ ] Public methods are documented with JavaDoc
- [ ] Public methods are tested with unit tests
- [ ] New and existing tests pass when run locally (`./mvnw clean verify`)
- [ ] There are no CheckStyle warnings or errors
- [ ] Code follows the Google Java Style Guide
- [ ] Changes maintain backward compatibility (unless discussed with maintainers)
- [ ] Commit messages are clear and descriptive
- [ ] Pull request description references the related issue

**Note**: Pull requests cannot be merged unless all status checks pass (tests, CheckStyle, SonarCloud, CodeQL).

### Code Review Process

1. **Automated Checks**: GitHub Actions will automatically run:
   - Build and tests
   - CheckStyle validation
   - SonarCloud analysis
   - CodeQL security scanning

2. **Maintainer Review**: A project maintainer will review your code for:
   - Correctness and quality
   - Adherence to project conventions
   - Test coverage
   - Documentation completeness

3. **Feedback and Iteration**: 
   - Address any requested changes
   - Push updates to your branch (the PR will update automatically)
   - Discuss any questions or concerns in the PR comments

4. **Approval and Merge**: Once approved and all checks pass, a maintainer will merge your PR

## Project Structure

xAPI Java is a [monorepo](https://en.wikipedia.org/wiki/Monorepo) containing multiple Maven modules:

- **`xapi-model/`**: Core xAPI data models (Statement, Actor, Verb, Activity, etc.)
  - All classes are immutable with builder pattern
  - Extensive validation annotations
  - Custom validators for xAPI-specific rules

- **`xapi-client/`**: Spring WebClient-based reactive client for LRS communication
  - Implements Statement, State, Agent Profile, and Activity Profile resources
  - Fluent request builders
  - Auto-configuration via Spring Boot

- **`xapi-model-spring-boot-starter/`**: Spring Boot autoconfiguration
  - Configurable validation rules
  - Properties prefix: `xapi.model.`

- **`samples/`**: Example applications demonstrating xAPI client usage
  - Useful as reference for common patterns

## Additional Resources

- **[README.md](README.md)**: Project overview and usage examples
- **[RELEASING.md](RELEASING.md)**: Release process information (for maintainers)
- **[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)**: Community standards and behavior expectations
- **[xAPI Specification](https://github.com/adlnet/xAPI-Spec)**: Official xAPI specification
- **[Issues](https://github.com/BerryCloud/xapi-java/issues)**: Bug reports and feature requests
- **[Pull Requests](https://github.com/BerryCloud/xapi-java/pulls)**: Ongoing contributions
- **[GitHub Actions](https://github.com/BerryCloud/xapi-java/actions)**: CI/CD workflow runs
- **[SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java)**: Code quality metrics
- **[Maven Central](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model)**: Published artifacts

---

**Questions or need help?** Feel free to:
- Open an issue for bugs or feature requests
- Ask questions in pull request comments
- Reach out to the maintainers listed in `pom.xml`

Thank you for contributing to xAPI Java! 🎉
