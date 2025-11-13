# Contributing to xAPI Java

Thank you for your interest in contributing to xAPI Java! This document provides guidelines and workflows for different types of contributions.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Types of Contributions](#types-of-contributions)
- [Development Workflow](#development-workflow)
- [Chore and Maintenance Workflow](#chore-and-maintenance-workflow)
- [Pull Request Process](#pull-request-process)
- [Code Style and Quality](#code-style-and-quality)

## Code of Conduct

This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to conduct@berrycloud.co.uk.

## Getting Started

### Prerequisites

- Java 17 or newer
- Maven 3.6 or newer

### Building the Project

```bash
# Clone the repository
git clone https://github.com/BerryCloud/xapi-java.git
cd xapi-java

# Build the project
./mvnw clean install

# Run tests
./mvnw test
```

## Types of Contributions

We welcome various types of contributions:

### Bug Reports

If you find a bug, please create an issue using the [Bug Report template](.github/ISSUE_TEMPLATE/bug-report.yml). Include:
- A clear description of the bug
- Steps to reproduce
- Expected vs actual behavior
- Version information

### Enhancement Requests

For new features or improvements, use the [Enhancement Request template](.github/ISSUE_TEMPLATE/enhancement-request.yml). Describe:
- What the enhancement does
- Why it would be useful
- Any relevant context or examples

### Chores and Maintenance

For routine maintenance tasks, use the [Chore template](.github/ISSUE_TEMPLATE/chore.yml). This includes:
- **Dependency Updates**: Keeping dependencies current and secure
- **Code Cleanup**: Removing unused code, refactoring, improving readability
- **Documentation Updates**: Fixing typos, improving clarity, adding examples
- **Build/CI Configuration**: Improving build processes and CI/CD workflows
- **Testing Infrastructure**: Enhancing test coverage and test utilities
- **Repository Hygiene**: Updating .gitignore, cleaning up files, organizing structure

## Development Workflow

1. **Fork the repository** and create a new branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/your-bug-fix
   # or
   git checkout -b chore/your-maintenance-task
   ```

2. **Make your changes** following our [code style guidelines](#code-style-and-quality)

3. **Write or update tests** to cover your changes

4. **Run the test suite** to ensure everything passes:
   ```bash
   ./mvnw clean test
   ```

5. **Run Checkstyle** to ensure code style compliance:
   ```bash
   ./mvnw checkstyle:check
   ```

6. **Commit your changes** with clear, descriptive commit messages:
   ```bash
   git commit -m "Add feature: description of your change"
   ```

7. **Push to your fork** and submit a pull request

## Chore and Maintenance Workflow

Regular maintenance keeps the codebase healthy and sustainable. Here's the workflow for common maintenance tasks:

### Dependency Updates

1. **Check for updates**: Review dependency versions in `pom.xml` files
2. **Update versions**: Change version numbers in appropriate `pom.xml` files
3. **Test thoroughly**: Run full test suite to ensure compatibility
4. **Document changes**: Note any breaking changes or migration steps
5. **Create PR**: Use the chore issue template and reference in your PR

### Code Cleanup and Refactoring

1. **Identify areas**: Look for code smells, unused imports, or deprecated APIs
2. **Make incremental changes**: Keep changes focused and reviewable
3. **Preserve functionality**: Ensure refactoring doesn't change behavior
4. **Update tests**: Modify tests if needed to reflect refactored structure
5. **Document rationale**: Explain why the cleanup improves the codebase

### Documentation Improvements

1. **Fix errors**: Correct typos, broken links, or outdated information
2. **Add examples**: Provide code samples for common use cases
3. **Improve clarity**: Rewrite confusing sections for better understanding
4. **Keep consistency**: Match existing documentation style and format
5. **Review impact**: Ensure changes are accurate and helpful

### Build and CI Configuration

1. **Test locally**: Verify changes work in your local environment
2. **Check CI**: Ensure GitHub Actions workflows still pass
3. **Update documentation**: Reflect any changes to build process
4. **Consider compatibility**: Ensure changes work across supported platforms
5. **Monitor results**: Watch CI runs after merging

## Pull Request Process

1. **Fill out the PR template** completely:
   - Describe your changes
   - Link to related issue(s)
   - Complete the checklist

2. **Ensure all checks pass**:
   - All tests must pass
   - Checkstyle validation must pass
   - SonarCloud analysis should show no new issues
   - CodeQL security scan must pass

3. **Respond to review feedback**:
   - Address reviewer comments promptly
   - Ask questions if feedback is unclear
   - Make requested changes or explain your reasoning

4. **Keep PR focused**:
   - One feature/fix per PR
   - Avoid mixing unrelated changes
   - Keep the scope manageable for review

5. **Update documentation** if your changes affect:
   - Public APIs
   - Configuration options
   - Usage examples
   - Build process

## Code Style and Quality

### Style Guidelines

This project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Checkstyle enforces these rules automatically.

Key points:
- Use 2 spaces for indentation (not tabs)
- Maximum line length: 100 characters
- Proper Javadoc for public methods and classes
- Meaningful variable and method names

### Testing Requirements

- **Public methods must be tested**: Every public API should have unit tests
- **Public methods must be documented**: Include Javadoc with parameter descriptions
- **Tests must pass**: New and existing tests must pass locally and in CI
- **No new warnings**: Don't introduce new compiler or Checkstyle warnings

### Commit Message Guidelines

Write clear, concise commit messages:

```
Short summary (50 characters or less)

More detailed explanation if necessary. Wrap at 72 characters.
Explain what changed and why, not how (the code shows how).

- Bullet points are okay
- Use present tense ("Add feature" not "Added feature")
- Reference issues: "Fixes #123" or "Relates to #456"
```

## Quality Checks

Before submitting your PR, ensure:

- [ ] Code compiles without errors
- [ ] All tests pass locally
- [ ] Checkstyle validation passes
- [ ] No new compiler warnings
- [ ] Public methods are documented with Javadoc
- [ ] Public methods have unit tests
- [ ] Changes are described in the PR description
- [ ] Related issue is referenced

## Getting Help

- **Questions about contributing?** Open a discussion or ask in your PR
- **Found a security issue?** Please report responsibly via email to security@berrycloud.co.uk (if available) or create a private security advisory
- **Need clarification on requirements?** Comment on the related issue

## Recognition

Contributors are recognized in the project's commit history and release notes. Significant contributions may be highlighted in release announcements.

Thank you for contributing to xAPI Java! 🎉
