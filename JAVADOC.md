# JavaDoc Configuration and Guidelines

This document explains the JavaDoc configuration and approach used in the xAPI Java project.

## Overview

The xAPI Java project maintains high-quality JavaDoc documentation for all public APIs. The project uses Maven JavaDoc Plugin with custom configuration to handle Lombok-generated code appropriately.

## Running JavaDoc Generation

To generate JavaDoc for the entire project:

```bash
./mvnw javadoc:javadoc
```

This will generate JavaDoc for all modules and report any warnings.

## Maven Configuration

The project's `pom.xml` includes the following JavaDoc plugin configuration:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-javadoc-plugin</artifactId>
  <configuration>
    <!-- Suppress warnings for Lombok-generated code -->
    <doclint>all,-missing</doclint>
    <quiet>true</quiet>
  </configuration>
  <executions>
    <execution>
      <id>attach-javadocs</id>
      <goals>
        <goal>jar</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

### Configuration Explanation

- **`<doclint>all,-missing</doclint>`**: Enables all JavaDoc linting checks except "missing" warnings
  - This suppresses warnings about missing JavaDoc on Lombok-generated constructors and methods
  - All other JavaDoc quality checks remain active
  
- **`<quiet>true</quiet>`**: Reduces verbose output during JavaDoc generation

## Lombok and JavaDoc

### The Challenge

Lombok auto-generates code (builders, constructors, getters, etc.) that JavaDoc cannot analyze. This causes numerous "use of default constructor, which does not provide a comment" warnings.

### The Solution

Rather than:
- Adding manual JavaDoc to Lombok-generated code (not possible)
- Disabling Lombok (defeats the purpose of using it)
- Ignoring hundreds of warnings

We configured the JavaDoc plugin to suppress these specific warnings while maintaining all other quality checks.

## Documentation Standards

### Required Documentation

All **manually written** public APIs must include JavaDoc:

1. **Public classes and interfaces**
   - Brief description
   - Author tags (`@author`)
   - Links to relevant xAPI specification sections (`@see`)

2. **Public methods**
   - Purpose and behavior description
   - Parameter documentation (`@param`)
   - Return value documentation (`@return`)
   - Exception documentation (`@throws`)
   - Links to xAPI specification when applicable

3. **Validation annotations**
   - All annotation methods (`message()`, `groups()`, `payload()`) must have `@return` tags

4. **Constructors** (when not Lombok-generated)
   - Purpose
   - Parameter documentation (`@param`)

### Not Required

Documentation is **not required** for:
- Lombok-generated builders and their methods
- Lombok-generated constructors
- Private methods (internal implementation details)
- Package-private classes intended for internal use only

### Example: Well-Documented Class

```java
/**
 * This class represents the xAPI Agent object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#agent">xAPI Agent</a>
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Agent extends Actor {
  
  private AgentObjectType objectType;
  
  /**
   * Builder for Agent.
   */
  public abstract static class Builder<C extends Agent, B extends Builder<C, B>>
      extends Actor.Builder<C, B> {
    // Lombok generates the builder methods
  }
}
```

### Example: Well-Documented Method

```java
/**
 * Gets a Statement from the LRS.
 * <p>
 * The returned ResponseEntity contains the response headers and the Statement.
 * </p>
 *
 * @param request the get statement request
 *
 * @return the ResponseEntity containing the statement
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">xAPI GET Statements</a>
 */
public Mono<ResponseEntity<Statement>> getStatement(GetStatementRequest request) {
  // implementation
}
```

### Example: Validation Annotation

```java
/**
 * The annotated element must be a valid score.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#Score">Score</a>
 */
@Documented
@Constraint(validatedBy = {ScoreValidator.class})
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ValidScore {

  /**
   * Error Message.
   *
   * @return the error message
   */
  String message() default "must be a valid score";

  /**
   * Groups.
   *
   * @return the validation groups
   */
  Class<?>[] groups() default {};

  /**
   * Payload.
   *
   * @return the payload
   */
  Class<? extends Payload>[] payload() default {};
}
```

## Best Practices

1. **Link to xAPI specification**: When documenting xAPI-related classes or methods, always include a link to the relevant section of the specification

2. **Use HTML formatting sparingly**: JavaDoc supports HTML, but keep it minimal
   - Use `<p>` for paragraph breaks
   - Use `{@code ...}` for inline code
   - Use `{@link ClassName}` for cross-references

3. **Keep descriptions concise**: The first sentence becomes the summary in the JavaDoc index

4. **Document expected behavior**: Include information about:
   - What the method does
   - What parameters are expected
   - What is returned
   - When exceptions are thrown
   - Any important side effects

5. **Maintain consistency**: Follow the documentation style used in existing code

## Quality Checks

The project enforces JavaDoc quality through:

1. **Maven JavaDoc Plugin**: Generates warnings for missing or malformed documentation
2. **Code Review**: Pull requests are reviewed for adequate documentation
3. **SonarCloud**: Analyzes code quality including documentation coverage

## Troubleshooting

### Problem: "warning: no @return" on bean methods

**Solution**: Add `@return the [description]` to the JavaDoc comment

### Problem: "warning: no @param for X"

**Solution**: Add `@param X [description]` to the JavaDoc comment

### Problem: Hundreds of "use of default constructor" warnings

**Solution**: These should be suppressed by the Maven configuration. If you see them, verify that the `pom.xml` has the correct `<doclint>all,-missing</doclint>` configuration.

### Problem: "warning: @inheritDoc used but does not override any method"

**Solution**: Remove `{@inheritDoc}` from the JavaDoc - it's only valid when overriding a method

## Additional Resources

- [Oracle JavaDoc Guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
- [xAPI Specification](https://github.com/adlnet/xAPI-Spec)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

## Maintenance Notes

### For Maintainers

When adding new public APIs:
1. Always include complete JavaDoc
2. Run `./mvnw javadoc:javadoc` to verify
3. Address any warnings that aren't Lombok-related
4. Update this document if new patterns emerge

### Configuration Changes

If you need to modify the JavaDoc configuration:
1. Test thoroughly with `./mvnw javadoc:javadoc`
2. Ensure no legitimate warnings are hidden
3. Document the change in this file
4. Consider the impact on CI/CD pipelines

---

Last updated: 2025-11-18
