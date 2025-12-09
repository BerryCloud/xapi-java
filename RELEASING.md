# Release Process

This document describes the manual draft release process for xAPI Java.

## Overview

The release process is **manually controlled** via draft releases and GitHub Actions workflow dispatch. Releases are initiated on-demand with explicit version control:
1. Create a draft release in GitHub UI with a semver tag
2. Go to Actions tab and trigger the "Manual Draft Release" workflow
3. Enter the draft release title
4. Wait for workflow to complete ✅

All version management, building, testing, and deployment happens in a controlled, manual process.

## Manual Draft Release Process

### Step 1: Create a Draft Release

1. Navigate to the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
2. Click **"Draft a new release"**
3. Fill in the release details:
   - **Tag**: Enter a semver tag in format `vX.Y.Z` (e.g., `v1.2.0`)
     - **Important**: Tag MUST include the `v` prefix followed by semantic version numbers
     - Example: `v1.1.20`, `v2.0.0`, `v1.2.3`
   - **Release title**: Use the same tag name (e.g., `v1.2.0`)
   - **Target**: Select the branch to release from (typically `main`)
   - **Description**: Add release notes describing the changes
4. Click **"Save draft"** - do NOT publish yet

### Step 2: Trigger the Manual Draft Release Workflow

1. Navigate to the [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. Click on **"Manual Draft Release"** workflow in the left sidebar
3. Click the **"Run workflow"** button (top right)
4. Fill in the workflow input:
   - **Draft release title**: Enter the exact title of your draft release (e.g., `v1.2.0`)
     - Must match the title from Step 1 exactly
     - Format: `vX.Y.Z` with the `v` prefix
5. Click **"Run workflow"** to start the release process

### Step 3: Workflow Execution

Once you trigger the workflow, the "Manual Draft Release" workflow will:

1. ✅ Validate the draft release title format (must be `vX.Y.Z`)
2. ✅ Find and validate the draft release in GitHub
3. ✅ Extract version information from the draft release
4. ✅ **Run Maven release:prepare** to:
   - Update all `pom.xml` files to the release version (e.g., 1.2.0)
   - Commit the version change
   - Create the release tag (e.g., v1.2.0) pointing to the release commit
   - Update `pom.xml` files to the next SNAPSHOT version (e.g., 1.2.1-SNAPSHOT)
   - Commit the next development iteration
5. ✅ **Update example version numbers in documentation**:
   - Automatically update version numbers in README.md dependency examples
   - Create a third commit with the documentation updates
   - Ensures documentation examples always match the release version
6. ✅ **Run Maven release:perform** to:
   - Check out the release tag
   - Build and test the release version
   - Deploy artifacts to Maven Central with GPG signatures
7. ✅ Push commits and tag back to the target branch
8. ✅ Upload release artifacts (JAR files) to the draft release
9. ✅ Publish the GitHub Release (remove draft status)

**Workflow Diagram:**
```
User Action: Create draft release in GitHub UI
             - Title: v1.2.0
             - Tag: v1.2.0
             - Target: main
    ↓
User Action: Trigger "Manual Draft Release" workflow
             - Draft release title: v1.2.0
    ↓
Workflow: Validates title format (v1.2.0)
    ↓
Workflow: Finds draft release with title "v1.2.0"
    ↓
Workflow: Extracts version (1.2.0) and target branch (main)
    ↓
Workflow: Runs release:prepare on main branch
    ↓
  - Commit A: pom.xml → 1.2.0 (release version)
  - Creates tag v1.2.0 → commit A
  - Commit B: pom.xml → 1.2.1-SNAPSHOT (next dev version)
    ↓
Workflow: Updates example versions in documentation
    ↓
  - Updates README.md with version 1.2.0
  - Commit C: Documentation updates
    ↓
Workflow: Runs release:perform
    ↓
  - Checks out tag v1.2.0 (commit A)
  - Builds and tests
  - Deploys to Maven Central
    ↓
Workflow: Pushes commits A, B & C to main
    ↓
Workflow: Pushes tag v1.2.0 → commit A
    ↓
Workflow: Uploads JAR artifacts to draft release
    ↓
Workflow: Publishes the GitHub Release
    ↓
Result:
  - Tag v1.2.0 → commit A (release version: 1.2.0)
  - Main branch → commit C (next SNAPSHOT: 1.2.1-SNAPSHOT, with updated docs)
  - Artifacts deployed to Maven Central
  - GitHub Release published with JAR files
```

### Step 4: Verify Release

1. Check the [Actions tab](https://github.com/BerryCloud/xapi-java/actions) to ensure the workflow completed successfully
   - The workflow will show a summary of the release including version, tag, and status
2. Verify the target branch (e.g., `main`) has three new commits:
   - Release commit: `[maven-release-plugin] prepare release vX.Y.Z`
   - Development commit: `[maven-release-plugin] prepare for next development iteration`
   - Documentation commit: `[release] Update documentation examples to version X.Y.Z`
3. Verify the GitHub Release was published at the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
   - The release should no longer be in draft state
   - JAR artifacts should be attached to the release
4. Verify documentation examples were updated:
   - Check that README.md contains the correct version in dependency examples
   - The version should match the release version (e.g., 1.2.0)
5. Verify artifacts are available on [Maven Central](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model)
   - Note: It may take up to 2 hours for artifacts to sync to Maven Central

## Manual Draft Release Benefits

The manual draft release approach provides several advantages:

- **Full Control**: Releases happen only when explicitly triggered, not automatically
- **Predictability**: No unexpected releases due to automation triggers
- **Review First**: Create and review draft releases before triggering deployment
- **Release Notes**: Prepare release notes in advance as part of the draft
- **Artifact Attachment**: Release artifacts (JARs) are automatically uploaded to the GitHub Release
- **Coordination**: Coordinate releases with code reviews and team schedules
- **Automated Documentation**: Version numbers in documentation examples are automatically updated to match the release version

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

## Automated Documentation Version Updates

The release workflow automatically updates version numbers in documentation examples to match the release version. This ensures users always see correct, up-to-date version numbers when copying examples from the documentation.

### What Gets Updated

The automation updates version numbers in:
- **README.md**: All Maven dependency examples for xapi-client, xapi-model, and xapi-model-spring-boot-starter

### How It Works

1. After Maven release:prepare completes, a script (`.github/scripts/update-example-versions.sh`) runs automatically
2. The script extracts the release version from the root `pom.xml`
3. It updates all `<version>X.Y.Z</version>` tags within dependency blocks in README.md
4. The changes are committed as a separate third commit after the release commit (not amending the release commit)
5. The release tag continues to point to the original release commit (not the documentation update commit)

### Benefits

- **No manual updates needed**: Version numbers are updated automatically during release
- **Always accurate**: Documentation examples always reference the current release version
- **Transparent**: Changes appear in the release commit and are visible in pull requests
- **Single source of truth**: Uses Maven's version from pom.xml as the authoritative source

### Maintenance

The script uses simple pattern matching to find and replace version numbers. If new files need to be updated:

1. Edit `.github/scripts/update-example-versions.sh`
2. Add the file path to the `FILES_TO_UPDATE` array
3. Commit the change

The script will automatically update all listed files in future releases.

## Advanced Options

### Editing Draft Releases

You can edit a draft release before triggering the workflow:

1. Navigate to the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
2. Find your draft release
3. Click **"Edit"**
4. Update release notes, title, or target branch as needed
5. Click **"Save draft"**
6. Then trigger the workflow with the updated title

### Release Notes

The workflow preserves the release notes you write in the draft release:

1. When creating the draft release, write detailed release notes
2. The workflow will keep these notes when publishing the release
3. After publication, the release will show your notes plus attached JAR artifacts

## Troubleshooting

### Release Workflow Failed

If the manual draft release workflow fails:

1. **Check the workflow logs** in the [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. **Identify the failed step** and review the error message
3. **Common issues and solutions:**

   | Issue | Solution |
   |-------|----------|
   | Invalid title format | Use format `vX.Y.Z` (e.g., `v1.2.0`) - MUST include `v` prefix |
   | Draft release not found | Ensure you created a draft release with the exact title you entered |
   | Release is not a draft | The release must be in draft state - edit it and set to draft |
   | No tag name set | Ensure the draft release has a tag name configured |
   | Missing secrets | Ensure GPG keys and Maven credentials are configured in repository secrets |
   | Build failures | Fix build issues on target branch first, then retry release |
   | Test failures | Fix failing tests on target branch first, then retry release |
   | Branch divergence | Someone pushed to the branch during release. Check the error and retry |

4. **After fixing issues:**
   - **If commits were NOT pushed**: Simply re-run the workflow with the same title
   - **If commits were pushed**: 
     - Delete the tag in GitHub UI (if it was created)
     - Delete the published release (if it was published)
     - Reset your target branch if needed:
       ```bash
       # If release:prepare pushed commits before failure
       git fetch origin
       git checkout main  # or your target branch
       git reset --hard origin/main~2  # Remove the 2 release commits
       git push -f origin main
       ```
     - Create a new draft release
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
