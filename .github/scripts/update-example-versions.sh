#!/bin/bash

# Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.

set -euo pipefail

# Script to update version numbers in documentation examples
# This script is called during the release process to ensure documentation
# examples always reference the current release version

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Function to extract version from pom.xml
get_version_from_pom() {
    local pom_file="$1"
    
    # Extract version from the project section (not parent)
    # Use xmllint if available, otherwise use grep/sed
    if command -v xmllint &> /dev/null; then
        xmllint --xpath '//*[local-name()="project"]/*[local-name()="version"]/text()' "$pom_file" 2>/dev/null | head -1
    else
        # Fallback to grep/sed approach - skip parent block, get project version
        # First skip everything in the parent block, then get the first version tag
        awk '/<parent>/,/<\/parent>/ {next} /<version>/ {print; exit}' "$pom_file" | sed 's/.*<version>\(.*\)<\/version>.*/\1/'
    fi
}

# Main script
main() {
    cd "$REPO_ROOT"
    
    echo "Updating example version numbers in documentation..."
    
    # Get the current version from the root pom.xml
    CURRENT_VERSION=$(get_version_from_pom "pom.xml")
    
    if [ -z "$CURRENT_VERSION" ]; then
        echo "ERROR: Could not extract version from pom.xml"
        exit 1
    fi
    
    echo "Current version: $CURRENT_VERSION"
    
    # Strip -SNAPSHOT suffix if present (we want the release version)
    RELEASE_VERSION="${CURRENT_VERSION%-SNAPSHOT}"
    
    echo "Updating documentation to use version: $RELEASE_VERSION"
    
    # Files to update
    FILES_TO_UPDATE=(
        "README.md"
    )
    
    # Update version numbers in all specified files
    for file in "${FILES_TO_UPDATE[@]}"; do
        if [ ! -f "$file" ]; then
            echo "WARNING: File not found: $file"
            continue
        fi
        
        echo "Updating $file..."
        
        # Create a backup
        cp "$file" "$file.bak"
        
        # Replace version numbers in Maven dependency examples
        # Pattern: <version>X.Y.Z</version> where X.Y.Z is a version number (or more components)
        # Only replace in dependency blocks (between <dependency> and </dependency>)
        # Use a more flexible pattern to match versions with multiple components
        # Use cross-platform sed syntax with explicit temp file
        sed '/<dependency>/,/<\/dependency>/ s|<version>[^<]*</version>|<version>'"$RELEASE_VERSION"'</version>|g' "$file" > "$file.tmp"
        mv "$file.tmp" "$file"
        
        # Check if file was modified
        if ! diff -q "$file" "$file.bak" > /dev/null 2>&1; then
            echo "✓ Updated $file"
            rm "$file.bak"
        else
            echo "- No changes needed in $file"
            mv "$file.bak" "$file"
        fi
    done
    
    echo ""
    echo "Example version update complete!"
    echo "Version updated to: $RELEASE_VERSION"
    echo ""
    echo "Modified files:"
    git --no-pager diff --name-only
    echo ""
    echo "Summary of changes:"
    git --no-pager diff --stat
}

# Run main function
main "$@"
