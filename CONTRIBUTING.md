# Contributing to xAPI Java

Thank you for your interest in contributing to xAPI Java! This document provides guidelines and workflows for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Issue Types](#issue-types)
  - [Bug Reports](#bug-reports)
  - [Enhancement Requests](#enhancement-requests)
  - [Chores and Maintenance](#chores-and-maintenance)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)

## Code of Conduct

This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## Getting Started

### Prerequisites

- Java 17 or newer
- Maven 3.6 or newer (or use the included Maven wrapper `./mvnw`)

### Building the Project

```bash
# Clone the repository
git clone https://github.com/BerryCloud/xapi-java.git
cd xapi-java

# Build the project (skip tests for faster build)
./mvnw clean install -DskipTests

# Build with tests
./mvnw clean install
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests for a specific module
./mvnw test -pl xapi-client
```

## Development Workflow

1. **Fork and Clone**: Fork the repository and clone your fork locally
2. **Create a Branch**: Create a feature branch from `main`
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make Changes**: Implement your changes following the coding standards
4. **Test**: Ensure all tests pass and add new tests for your changes
5. **Commit**: Write clear, concise commit messages
6. **Push**: Push your changes to your fork
7. **Pull Request**: Open a pull request against the `main` branch

## Issue Types

### Bug Reports

Use the **Bug Report** template for reporting defects or unexpected behavior in the library.

**When to use:**
- The library doesn't work as documented
- You encounter errors or exceptions
- There's incorrect behavior in the xAPI implementation

**Example:**
> [Bug]: Statement with attachment fails with 400 error

### Enhancement Requests

Use the **Enhancement Request** template for proposing new features or improvements to existing functionality.

**When to use:**
- You want to add a new feature
- You want to improve existing functionality
- You have an idea for better API design

**Example:**
> [Enhancement]: Add support for xAPI 2.0 specification

### Chores and Maintenance

Use the **Chore / Maintenance** template for tracking routine maintenance tasks, code quality improvements, and repository hygiene.

**When to use chore/maintenance issues:**

#### Code Cleanup and Refactoring
- Removing deprecated code
- Simplifying complex methods
- Improving code readability
- Reducing code duplication
- Organizing imports or package structure

#### Documentation Updates
- Fixing typos or grammatical errors
- Updating outdated documentation
- Adding missing documentation
- Improving code examples
- Updating README or guides

#### Dependency Updates
- Updating library versions
- Upgrading Spring Boot or other frameworks
- Applying security patches
- Removing unused dependencies

#### Build System and Tooling
- Maven configuration updates
- Checkstyle rule adjustments
- SonarQube configuration
- CI/CD pipeline improvements

#### Repository Configuration
- GitHub Actions workflow updates
- Dependabot configuration
- Issue template improvements
- Git configuration changes

#### Test Infrastructure
- Improving test organization
- Adding test utilities
- Updating test dependencies
- Enhancing test coverage reporting

**Workflow for maintenance tasks:**

1. **Create an issue** using the Chore/Maintenance template
   - Clearly describe what needs to be done
   - Explain why it's beneficial
   - Note the scope and potential impact
   - Add a task checklist if the work involves multiple steps

2. **Label appropriately**
   - `chore`: General maintenance work
   - `maintenance`: Ongoing upkeep tasks
   - `documentation`: Documentation-related changes
   - `dependencies`: Dependency updates
   - `ci-cd`: CI/CD improvements

3. **Keep changes focused**
   - Each maintenance PR should address one specific area
   - Avoid mixing unrelated changes
   - Keep PRs small and reviewable

4. **Document changes**
   - Update relevant documentation if needed
   - Note any breaking changes (even if unlikely)
   - Explain the rationale in the PR description

**Examples of maintenance tasks:**

- ✅ "Update Spring Boot from 3.5.6 to 3.5.7"
- ✅ "Remove unused imports in xapi-model module"
- ✅ "Fix typos in README.md"
- ✅ "Upgrade maven-checkstyle-plugin to 3.5.0"
- ✅ "Add missing JavaDoc to public methods in XapiClient"
- ✅ "Reorganize test fixtures for better reusability"

## Pull Request Process

### Before Submitting

1. **Ensure tests pass**: Run `./mvnw test` locally
2. **Check code style**: The build will fail if there are Checkstyle violations
3. **Update documentation**: Update relevant docs if your changes affect public APIs
4. **Add tests**: New features should include appropriate test coverage

### PR Checklist

When you open a pull request, ensure you complete the PR template checklist:

- [ ] Public methods are documented
- [ ] Public methods are tested
- [ ] New and existing tests pass when run locally
- [ ] There are no new warnings or errors

### Review Process

- All PRs require review before merging
- Automated checks must pass (build, tests, CodeQL, SonarCloud)
- Address reviewer feedback
- Keep PRs focused and reasonably sized

### Commit Messages

Write clear, descriptive commit messages:

```
Brief summary (50 chars or less)

More detailed explanation if necessary. Wrap at 72 characters.
Explain what and why, not how.

- Bullet points are okay
- Use imperative mood: "Add feature" not "Added feature"

Resolves #123
```

## Coding Standards

### Style Guide

This project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), enforced by Checkstyle.

**Key points:**
- 2 spaces for indentation (no tabs)
- Line length limit of 100 characters
- Use meaningful variable and method names
- Add JavaDoc for all public APIs

### Immutability

Objects in this library are immutable. Use builders for object construction:

```java
Statement statement = Statement.builder()
    .agentActor(a -> a.name("John Doe").mbox("mailto:john@example.com"))
    .verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity"))
    .build();
```

### Fluent Interface

The library uses a fluent interface pattern. Chain method calls for readability:

```java
client.postStatement(r -> r
    .statement(s -> s
        .agentActor(a -> a.name("User").mbox("mailto:user@example.com"))
        .verb(Verb.PASSED)
        .activityObject(o -> o.id("https://example.com/activity"))))
    .block();
```

## Testing Guidelines

### Test Organization

- Unit tests should be in the same module as the code they test
- Use descriptive test method names that explain what is being tested
- Follow the Given-When-Then pattern for test structure

### Test Coverage

- Aim for high test coverage, especially for public APIs
- Test both happy paths and error conditions
- Include edge cases and boundary conditions

### Example Test

```java
@Test
void whenPostingValidStatementThenResponseIsOk() {
    // Given
    Statement statement = Statement.builder()
        .agentActor(a -> a.name("Test User").mbox("mailto:test@example.com"))
        .verb(Verb.ATTEMPTED)
        .activityObject(o -> o.id("https://example.com/activity"))
        .build();

    // When
    var response = client.postStatement(r -> r.statement(statement)).block();

    // Then
    assertThat(response.getStatusCode().value(), is(200));
}
```

## Automated Tools

### Dependabot

This project uses Dependabot to automatically check for dependency updates:
- Maven dependencies are checked weekly
- GitHub Actions are checked weekly
- PRs are automatically created for updates

### CodeQL

CodeQL automatically scans for security vulnerabilities on every push and pull request.

### SonarCloud

SonarCloud performs automatic code quality reviews on pull requests, checking for:
- Code smells
- Bugs
- Security vulnerabilities
- Code coverage
- Code duplication

## Questions?

If you have questions or need help:
- Check existing issues and discussions
- Review the [README](README.md) and documentation
- Open a new issue with the appropriate template

Thank you for contributing to xAPI Java!
