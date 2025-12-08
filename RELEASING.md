# Release Process

This document describes the manual release process for xAPI Java.

## Overview

The release process is **manually controlled** via GitHub Actions workflow dispatch. Releases are initiated on-demand with explicit version control:
1. Go to Actions tab in GitHub
2. Select "Manual Release" workflow
3. Click "Run workflow"
4. Enter the release version
5. Wait for workflow to complete ✅

All version management, building, testing, and deployment happens in a controlled, manual process.

## Manual Release Process



### Step 1: Trigger the Manual Release Workflow

1. Navigate to the [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. Click on **"Manual Release"** workflow in the left sidebar
3. Click the **"Run workflow"** button (top right)
4. Fill in the workflow inputs:
   - **Branch**: Select the branch to release from (typically `main`)
   - **Release version**: Enter the version number in format `X.Y.Z` (e.g., `1.2.0`)
     - **Important**: Do NOT include the `v` prefix - just the version numbers
     - Example: `1.1.16`, `2.0.0`, `1.2.3`
   - **Skip Maven Central**: Leave unchecked (or check for dry-run testing)
   - **Create GitHub Release**: Leave checked to automatically create a GitHub Release
5. Click **"Run workflow"** to start the release process

### Step 2: Workflow Execution

Once you trigger the workflow, the "Manual Release" workflow will:

1. ✅ Validate the version format (must be `X.Y.Z`)
2. ✅ Check that the tag doesn't already exist
3. ✅ **Run Maven release:prepare** to:
   - Update all `pom.xml` files to the release version (e.g., 1.2.0)
   - Commit the version change
   - Create the release tag (e.g., v1.2.0) pointing to the release commit
   - Update `pom.xml` files to the next SNAPSHOT version (e.g., 1.2.1-SNAPSHOT)
   - Commit the next development iteration
4. ✅ **Run Maven release:perform** to:
   - Check out the release tag
   - Build and test the release version
   - Deploy artifacts to Maven Central with GPG signatures
5. ✅ Push commits and tag back to the selected branch
6. ✅ Create a GitHub Release (if enabled)

**Workflow Diagram:**
```
User Action: Trigger "Manual Release" workflow
             - Branch: main
             - Version: 1.2.0
    ↓
Workflow: Validates version format (1.2.0)
    ↓
Workflow: Checks tag v1.2.0 doesn't exist
    ↓
Workflow: Runs release:prepare on main branch
    ↓
  - Commit A: pom.xml → 1.2.0 (release version)
  - Creates tag v1.2.0 → commit A
  - Commit B: pom.xml → 1.2.1-SNAPSHOT (next dev version)
    ↓
Workflow: Runs release:perform
    ↓
  - Checks out tag v1.2.0 (commit A)
  - Builds and tests
  - Deploys to Maven Central
    ↓
Workflow: Pushes commits A & B to main
    ↓
Workflow: Pushes tag v1.2.0 → commit A
    ↓
Workflow: Creates GitHub Release for v1.2.0
    ↓
Result:
  - Tag v1.2.0 → commit A (release version: 1.2.0)
  - Main branch → commit B (next SNAPSHOT: 1.2.1-SNAPSHOT)
  - Artifacts deployed to Maven Central
  - GitHub Release created
```

### Step 3: Verify Release

1. Check the [Actions tab](https://github.com/BerryCloud/xapi-java/actions) to ensure the workflow completed successfully
   - The workflow will show a summary of the release including version, tag, and deployment status
2. Verify the target branch (e.g., `main`) has two new commits:
   - Release commit: `[maven-release-plugin] prepare release vX.Y.Z`
   - Development commit: `[maven-release-plugin] prepare for next development iteration`
3. Verify the GitHub Release was created at the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
4. Verify artifacts are available on [Maven Central](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model)
   - Note: It may take up to 2 hours for artifacts to sync to Maven Central

## Manual Release Benefits

The manual release approach provides several advantages:

- **Full Control**: Releases happen only when explicitly triggered, not automatically
- **Predictability**: No unexpected releases due to automation triggers
- **Testing**: Ability to run dry-run releases (skip Maven Central deployment) for testing
- **Flexibility**: Choose when to create GitHub Releases independently
- **Review**: Coordinate releases with code reviews and team schedules

## Release Branch Strategy

- **Main branch (`main`)** (or other target branch): Contains development code with `-SNAPSHOT` versions
  - After each release, receives two commits from Maven release plugin:
    1. Release commit: Version update to release version (X.Y.Z)
    2. Development commit: Version update to next development version (X.Y.Z+1-SNAPSHOT)
  - The HEAD always points to the next development version
- **Release tags (`vX.Y.Z`)**: Created by Maven release plugin during workflow execution
  - Points to the release version commit (first commit)
  - Used for reproducible builds and deployments
- **No separate release branches**: The release workflow pushes directly to the selected branch

## Advanced Options

### Dry-Run Release (Testing)

You can test the release process without deploying to Maven Central:

1. Trigger the "Manual Release" workflow as described above
2. Check the **"Skip Maven Central"** option
3. The workflow will:
   - Run all version updates and create commits/tags
   - Skip the actual deployment to Maven Central
   - Allow you to verify the release process works correctly

This is useful for:
- Testing the release workflow
- Verifying version numbering
- Ensuring all commits and tags are created correctly

### Release Without GitHub Release

If you want to deploy to Maven Central but create the GitHub Release manually later:

1. Trigger the "Manual Release" workflow
2. Uncheck the **"Create GitHub Release"** option
3. After verifying the Maven Central deployment, manually create a GitHub Release

## Troubleshooting

### Release Workflow Failed

If the manual release workflow fails:

1. **Check the workflow logs** in the [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. **Identify the failed step** and review the error message
3. **Common issues and solutions:**

   | Issue | Solution |
   |-------|----------|
   | Invalid version format | Use format `X.Y.Z` (e.g., `1.2.0`) - do NOT include `v` prefix |
   | Tag already exists | The version has already been released. Use a different version number |
   | Missing secrets | Ensure GPG keys and Maven credentials are configured in repository secrets |
   | Build failures | Fix build issues on target branch first, then retry release |
   | Test failures | Fix failing tests on target branch first, then retry release |
   | Branch divergence | Someone pushed to the branch during release. Check the error and retry |

4. **After fixing issues:**
   - **If commits were NOT pushed**: Simply re-run the workflow with the same version
   - **If commits were pushed**: 
     - Delete the tag in GitHub UI (if it was created)
     - Reset your target branch if needed:
       ```bash
       # If release:prepare pushed commits before failure
       git fetch origin
       git checkout main  # or your target branch
       git reset --hard origin/main~2  # Remove the 2 release commits
       git push -f origin main
       ```
     - Re-run the workflow

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
