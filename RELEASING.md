# Release Process

This document describes the automated release process for xAPI Java.

## Overview

The release process is **fully automated** via GitHub Actions. Creating a release is as simple as:
1. Click "Create Release" in GitHub UI
2. Enter tag (e.g., `v1.2.0`)
3. Publish
4. Wait for workflow to complete ✅

All version management, building, testing, and deployment happens automatically.

## Automated Release Process



### Step 1: Create a GitHub Release

1. Navigate to the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
2. Click **"Draft a new release"**
3. **Choose a tag**: Enter the tag version in the format: `vX.Y.Z` (e.g., `v1.2.0`)
   - The tag **must** start with `v` followed by semantic version numbers
   - Example: `v1.1.16`, `v2.0.0`, `v1.2.3`
4. **Target**: Select the branch to release from (typically `main`)
   - The workflow will automatically detect and use this branch
5. Enter a release title (e.g., "Release 1.2.0")
6. Add release notes describing the changes
7. Click **"Publish release"**

### Step 2: Automated Workflow Execution

Once you publish the release, the "Automated Release" workflow will:

1. ✅ Validate the tag format (must be `vX.Y.Z`)
2. ✅ Detect the target branch (auto-detected from release)
3. ✅ Delete the user-created tag (will be recreated properly)
4. ✅ **Run Maven release:prepare** to:
   - Update all `pom.xml` files to the release version
   - Commit the version change
   - Create the release tag pointing to the release commit
   - Update `pom.xml` files to the next SNAPSHOT version
   - Commit the next development iteration
5. ✅ **Run Maven release:perform** to:
   - Check out the release tag
   - Build and test the release version
   - Deploy artifacts to Maven Central with GPG signatures
6. ✅ Push commits and tag back to the originating branch

**Workflow Diagram:**
```
User Action: Create Release (tag: v1.2.0, target: main)
    ↓
GitHub: Creates tag v1.2.0 → commit A (from main)
    ↓
Workflow: Detects target branch (main)
    ↓
Workflow: Deletes user-created tag v1.2.0
    ↓
Workflow: Runs release:prepare
    ↓
  - Commit B: pom.xml → 1.2.0 (release version)
  - Creates tag v1.2.0 → commit B
  - Commit C: pom.xml → 1.2.1-SNAPSHOT (next dev version)
    ↓
Workflow: Runs release:perform
    ↓
  - Checks out tag v1.2.0 (commit B)
  - Builds and tests
  - Deploys to Maven Central
    ↓
Workflow: Pushes commits B & C to main
    ↓
Workflow: Pushes tag v1.2.0 → commit B
    ↓
Result:
  - Tag v1.2.0 → commit B (release version)
  - Main branch → commit C (next SNAPSHOT: 1.2.1-SNAPSHOT)
  - Artifacts deployed to Maven Central
```

### Step 3: Verify Release

1. Check the [Actions tab](https://github.com/BerryCloud/xapi-java/actions) to ensure the workflow completed successfully
2. Verify the target branch (e.g., `main`) has two new commits:
   - Release commit: `[maven-release-plugin] prepare release vX.Y.Z`
   - Development commit: `[maven-release-plugin] prepare for next development iteration`
3. Verify artifacts are available on [Maven Central](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model)

## Release Branch Strategy

- **Main branch (`main`)** (or other target branch): Contains development code with `-SNAPSHOT` versions
  - After each release, receives two commits from Maven release plugin:
    1. Release commit: Version update to release version (X.Y.Z)
    2. Development commit: Version update to next development version (X.Y.Z+1-SNAPSHOT)
  - The HEAD always points to the next development version
- **Release tags (`vX.Y.Z`)**: Created by Maven release plugin
  - Points to the release version commit (first commit)
  - Used for reproducible builds and deployments
- **No separate release branches**: The release workflow pushes directly to the originating branch

## Branch Protection and Automated Commits

The release workflow needs to push commits to the `main` branch (or target branch). If branch protection rules prevent this, see [RELEASE_WORKFLOW_AUTHENTICATION.md](RELEASE_WORKFLOW_AUTHENTICATION.md) for detailed instructions on configuring the workflow to bypass branch protection using GitHub Apps or Personal Access Tokens.

**Quick links:**
- [Quick Start Guide](QUICK_START_GUIDE.md) - Fast setup (15 minutes)
- [Implementation Summary](IMPLEMENTATION_SUMMARY.md) - Research and recommendations
- [Rulesets Migration Guide](RULESETS_MIGRATION_GUIDE.md) - Modern branch protection

## Troubleshooting

### Release Workflow Failed

If the automated release workflow fails:

1. **Check the workflow logs** in the [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. **Identify the failed step** and review the error message
3. **Common issues and solutions:**

   | Issue | Solution |
   |-------|----------|
   | Invalid tag format | Use format `vX.Y.Z` (e.g., `v1.2.0`, not `1.2.0` or `v1.2`) |
   | Missing secrets | Ensure GPG keys and Maven credentials are configured in repository secrets |
   | Build failures | Fix build issues on main branch first, then retry release |
   | Test failures | Fix failing tests on main branch first, then retry release |
   | Branch protection blocks push | See [RELEASE_WORKFLOW_AUTHENTICATION.md](RELEASE_WORKFLOW_AUTHENTICATION.md) for setup instructions |

4. **After fixing issues:**
   - Delete the failed release and tag in GitHub UI
   - **Important**: Reset your target branch if commits were already pushed:
     ```bash
     # If release:prepare pushed commits before failure
     git fetch origin
     git checkout main  # or your target branch
     git reset --hard origin/main~2  # Remove the 2 release commits
     git push -f origin main
     ```
   - Create a new release with the same tag

### Workflow Stuck or Taking Too Long

The workflow typically takes 5-10 minutes. If it's taking longer:

1. Check if tests are running (this is the longest step)
2. Check for any hanging processes in the workflow logs
3. You can cancel the workflow run and restart it

### Artifacts Not Appearing on Maven Central

If artifacts don't appear on Maven Central after a successful workflow:

1. Check the Central Portal for deployment status: https://central.sonatype.com/
2. Deployment can take up to 2 hours to sync to Maven Central
3. Check for any deployment errors in the workflow logs
4. Verify GPG signing was successful (check for GPG-related errors)
