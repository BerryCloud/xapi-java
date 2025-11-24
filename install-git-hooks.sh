#!/bin/bash
# Install script for git hooks
# This script sets up git hooks for automatic code formatting

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Installing git hooks for xapi-java..."

# Check if we're in the repository root
if [ ! -f "$SCRIPT_DIR/mvnw" ]; then
    echo "Error: This script must be run from the repository root."
    exit 1
fi

# Configure git to use the .githooks directory
git config core.hooksPath .githooks

echo "✓ Git hooks installed successfully!"
echo ""
echo "The following hooks are now active:"
echo "  - pre-commit: Automatically formats Java code using fmt-maven-plugin"
echo ""
echo "To disable the hooks, run: git config --unset core.hooksPath"
