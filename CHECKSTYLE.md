# Checkstyle Configuration

This project uses [Checkstyle](https://checkstyle.org/) to enforce coding standards based on the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

## Configuration

The Checkstyle configuration is defined in `checkstyle.xml`, which extends Google Checks with additional rules:

- **Copyright Header Check**: All Java source files must include a copyright header in the format:
  ```java
  /*
   * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
   */
  ```

## Running Checkstyle

Checkstyle runs automatically during the Maven `validate` phase. You can also run it manually:

```bash
# Check for violations
./mvnw checkstyle:check

# Generate a report
./mvnw checkstyle:checkstyle
```

## Updating Copyright Year

When updating the copyright year in the future:

1. Update all Java source files using find/replace:
   ```bash
   find . -name "*.java" -type f -exec sed -i 's/Copyright 2016-YYYY/Copyright 2016-NEW_YEAR/g' {} \;
   ```

2. Update the `checkstyle.xml` RegexpHeader pattern to match the new year

3. Update the LICENSE file copyright year

4. Run `./mvnw checkstyle:check` to verify all files are updated correctly
