# Scripts

This directory contains automation scripts used by GitHub Actions workflows.

## update-example-versions.sh

Updates version numbers in documentation examples during the release process.

**Purpose**: Ensures that version numbers in README.md dependency examples always match the current release version.

**Usage**:
```bash
# With explicit version (recommended during release)
bash .github/scripts/update-example-versions.sh "1.2.0"

# Without version argument (extracts from pom.xml)
bash .github/scripts/update-example-versions.sh
```

**What it does**:
1. Accepts an optional version argument (recommended to avoid confusion after release:prepare)
2. If no argument, extracts the current version from the root `pom.xml` (skipping parent version) and strips `-SNAPSHOT` suffix
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
3. Script accepts version argument
4. Full script execution
5. Version pattern matching (sed patterns)
6. Selective replacement (only updates dependency blocks)
7. Error handling (missing files)

**Exit codes**:
- `0`: All tests passed
- `1`: One or more tests failed

## export-sonar-findings.py

Run the **Export Sonar findings** workflow manually to download the complete unresolved
issue backlog on `main`, including accepted issues, security hotspots and rule descriptions.
The workflow also runs when the exporter changes in a PR from this repository. It uses
the existing `SONAR_TOKEN` secret and uploads a `sonar-findings` JSON artifact for seven
days. Issue locations and messages are also printed in the job log.

The exporter retrieves every page without a new-code filter and fails if the API
returns an incomplete result. The report describes `main` at export time; fixes on an
unmerged PR will only disappear from that backlog after merge and a new main analysis.

Offline exporter regression tests run with:

```bash
python3 -m unittest discover -s .github/scripts -p 'test_export_sonar_findings.py'
```

Java CI runs these tests with coverage.py and sends the coverage report to Sonar.
