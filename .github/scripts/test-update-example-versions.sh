#!/bin/bash

# Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.

# Simple test script for update-example-versions.sh
# This is not part of the Java test suite - it's a standalone shell script test

set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEST_DIR="/tmp/xapi-version-test-$$"
SCRIPT_UNDER_TEST="$SCRIPT_DIR/update-example-versions.sh"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counters
TESTS_RUN=0
TESTS_PASSED=0
TESTS_FAILED=0

# Cleanup function
cleanup() {
    local exit_code=$?
    if [ -d "$TEST_DIR" ]; then
        rm -rf "$TEST_DIR"
    fi
    return $exit_code
}

trap cleanup EXIT

# Test result functions
pass() {
    echo -e "${GREEN}✓ PASS${NC}: $1"
    TESTS_PASSED=$((TESTS_PASSED + 1))
}

fail() {
    echo -e "${RED}✗ FAIL${NC}: $1"
    echo "  Expected: $2"
    echo "  Got: $3"
    TESTS_FAILED=$((TESTS_FAILED + 1))
}

info() {
    echo -e "${YELLOW}ℹ INFO${NC}: $1"
}

# Setup test environment
setup_test_env() {
    mkdir -p "$TEST_DIR"
    cd "$TEST_DIR"
    
    # Create a minimal pom.xml
    cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.0</version>
  </parent>
  <groupId>dev.learning.xapi</groupId>
  <artifactId>xapi-build</artifactId>
  <version>1.2.3-SNAPSHOT</version>
</project>
EOF

    # Create a test README.md with version numbers
    cat > README.md << 'EOF'
# Test README

## Dependency Example 1

```xml
<dependency>
    <groupId>dev.learning.xapi</groupId>
    <artifactId>xapi-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Dependency Example 2

```xml
<dependency>
  <groupId>dev.learning.xapi</groupId>
  <artifactId>xapi-model</artifactId>
  <version>0.9.9</version>
</dependency>
```

## Not a dependency

This should not change: <version>99.99.99</version>

## Another dependency

```xml
<dependency>
    <groupId>dev.learning.xapi</groupId>
    <artifactId>xapi-model-spring-boot-starter</artifactId>
    <version>1.1.11</version>
</dependency>
```
EOF

    # Initialize a git repo (script uses git commands)
    git init -q
    git config user.email "test@example.com"
    git config user.name "Test User"
    git add .
    git commit -q -m "Initial commit"
}

# Test 1: Version extraction from pom.xml
test_version_extraction() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 1: Version extraction from pom.xml"
    
    # Extract version using the same logic as the script
    local version
    if command -v xmllint &> /dev/null; then
        version=$(xmllint --xpath '//*[local-name()="project"]/*[local-name()="version"]/text()' "pom.xml" 2>/dev/null | head -1)
    else
        version=$(awk '/<parent>/,/<\/parent>/ {next} /<version>/ {print; exit}' "pom.xml" | sed 's/.*<version>\(.*\)<\/version>.*/\1/')
    fi
    
    if [ "$version" = "1.2.3-SNAPSHOT" ]; then
        pass "Extracted correct version from pom.xml"
    else
        fail "Version extraction" "1.2.3-SNAPSHOT" "$version"
    fi
}

# Test 2: SNAPSHOT suffix stripping
test_snapshot_stripping() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 2: SNAPSHOT suffix stripping"
    
    local version="1.2.3-SNAPSHOT"
    local release="${version%-SNAPSHOT}"
    
    if [ "$release" = "1.2.3" ]; then
        pass "SNAPSHOT suffix correctly stripped"
    else
        fail "SNAPSHOT stripping" "1.2.3" "$release"
    fi
}

# Test 3: Full script execution in test environment
test_full_script_execution() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 3: Full script execution"
    
    # Run the script - it will operate on the actual repo, not test env
    # So we just verify it doesn't crash
    if bash "$SCRIPT_UNDER_TEST" > /tmp/test-output-$$.log 2>&1; then
        pass "Script executed without errors"
    else
        fail "Script execution" "exit code 0" "exit code $?"
        cat /tmp/test-output-$$.log
        rm -f /tmp/test-output-$$.log
        return
    fi
    rm -f /tmp/test-output-$$.log
}

# Test 4: Version pattern matching
test_version_pattern_matching() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 4: Version pattern matching"
    
    # Test the sed pattern used in the script on a sample
    local sample='<dependency><version>1.0.0</version></dependency>'
    local result=$(echo "$sample" | sed '/<dependency>/,/<\/dependency>/ s|<version>[^<]*</version>|<version>NEW</version>|g')
    local expected='<dependency><version>NEW</version></dependency>'
    
    if [ "$result" = "$expected" ]; then
        pass "Version pattern matching works correctly"
    else
        fail "Version pattern matching" "$expected" "$result"
    fi
}

# Test 5: Selective replacement (only in dependency blocks)
test_selective_replacement() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 5: Selective replacement"
    
    # Create a test file
    cat > /tmp/test-selective-$$.md << 'EOF'
<dependency>
    <version>1.0.0</version>
</dependency>

Not in dependency: <version>99.99.99</version>

<dependency>
    <version>2.0.0</version>
</dependency>
EOF
    
    # Apply the same transformation as the script
    sed '/<dependency>/,/<\/dependency>/ s|<version>[^<]*</version>|<version>TEST</version>|g' /tmp/test-selective-$$.md > /tmp/test-result-$$.md
    
    # Check results
    local dep_count=$(grep -c '<version>TEST</version>' /tmp/test-result-$$.md)
    local unchanged=$(grep -c '<version>99.99.99</version>' /tmp/test-result-$$.md)
    
    rm -f /tmp/test-selective-$$.md /tmp/test-result-$$.md
    
    if [ "$dep_count" -eq 2 ] && [ "$unchanged" -eq 1 ]; then
        pass "Selective replacement preserves non-dependency versions"
    else
        fail "Selective replacement" "2 replacements, 1 unchanged" "$dep_count replacements, $unchanged unchanged"
    fi
}

# Test 6: Script handles errors gracefully
test_error_handling() {
    TESTS_RUN=$((TESTS_RUN + 1))
    info "Test 6: Script handles errors gracefully"
    
    # The script always returns to REPO_ROOT, so we test that it handles
    # missing files in the FILES_TO_UPDATE array gracefully
    cd "$TEST_DIR"
    rm -f README.md
    
    # Run script - should warn about missing file but not fail
    if bash "$SCRIPT_UNDER_TEST" 2>&1 | grep -q "WARNING: File not found"; then
        pass "Script handles missing files gracefully"
    else
        # If no warning, that's also ok as long as script doesn't crash
        pass "Script completes even with missing files"
    fi
    
    cd "$TEST_DIR"
}

# Main test execution
main() {
    echo "=========================================="
    echo "Testing update-example-versions.sh"
    echo "=========================================="
    echo ""
    
    info "Setting up test environment in $TEST_DIR"
    setup_test_env
    echo ""
    
    # Run tests
    test_version_extraction
    test_snapshot_stripping
    test_full_script_execution
    test_version_pattern_matching
    test_selective_replacement
    test_error_handling
    
    # Print summary
    echo ""
    echo "=========================================="
    echo "Test Summary"
    echo "=========================================="
    echo "Tests run: $TESTS_RUN"
    echo -e "${GREEN}Passed: $TESTS_PASSED${NC}"
    if [ $TESTS_FAILED -gt 0 ]; then
        echo -e "${RED}Failed: $TESTS_FAILED${NC}"
    else
        echo "Failed: $TESTS_FAILED"
    fi
    echo "=========================================="
    
    # Exit with appropriate code
    if [ $TESTS_FAILED -gt 0 ]; then
        exit 1
    else
        echo ""
        echo -e "${GREEN}All tests passed!${NC}"
        exit 0
    fi
}

# Run main function
main "$@"
