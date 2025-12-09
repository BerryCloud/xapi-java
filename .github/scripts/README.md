# Scripts

This directory contains automation scripts used by GitHub Actions workflows.

## update-example-versions.sh

Updates version numbers in documentation examples during the release process.

**Purpose**: Ensures that version numbers in README.md dependency examples always match the current release version.

**Usage**:
```bash
# Automatically called by .github/workflows/manual-release.yml
bash .github/scripts/update-example-versions.sh
```

**What it does**:
1. Extracts the current version from the root `pom.xml` (skipping parent version)
2. Strips `-SNAPSHOT` suffix to get the release version
3. Updates all `<version>` tags within `<dependency>` blocks in README.md
4. Reports changes made via git diff

## test-update-example-versions.sh

Test script for `update-example-versions.sh`. This is not part of the Java test suite.

**Usage**:
```bash
# Run tests
bash .github/scripts/test-update-example-versions.sh
```

**Tests included**:
1. Version extraction from pom.xml
2. SNAPSHOT suffix stripping
3. Full script execution
4. Version pattern matching (sed patterns)
5. Selective replacement (only updates dependency blocks)
6. Error handling (missing files)

**Exit codes**:
- `0`: All tests passed
- `1`: One or more tests failed
