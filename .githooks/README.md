# Git Hooks for xAPI Java

This directory contains git hooks for automating development workflows.

## Available Hooks

### pre-commit

Automatically formats Java code using the Spotify fmt-maven-plugin before committing changes.

**What it does:**
- Runs `./mvnw com.spotify.fmt:fmt-maven-plugin:format` to format all Java files
- Adds reformatted files to the commit automatically
- Ensures code follows the Google Java Style Guide

## Installation

To enable these hooks, run from the repository root:

```bash
./install-git-hooks.sh
```

This configures git to use hooks from this directory instead of the default `.git/hooks/`.

## Disabling Hooks

To disable the hooks:

```bash
git config --unset core.hooksPath
```

## Manual Formatting

If you prefer not to use the git hook, you can manually format code at any time:

```bash
./mvnw com.spotify.fmt:fmt-maven-plugin:format
```
