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

The release process is fully automated via GitHub Actions. To create a new release:

### Step 1: Create a GitHub Release

1. Navigate to the [Releases page](https://github.com/BerryCloud/xapi-java/releases)
2. Click **"Draft a new release"**
3. Enter the tag version in the format: `vX.Y.Z` (e.g., `v1.2.0`)
   - The tag **must** start with `v` followed by semantic version numbers
   - Example: `v1.1.16`, `v2.0.0`, `v1.2.3`
4. Enter a release title (e.g., "Release 1.2.0")
5. Add release notes describing the changes
6. Click **"Publish release"**

### Step 2: Automated Workflow Execution

Once you publish the release, the "Automated Release" workflow will:

1. ✅ Validate the tag format (must be `vX.Y.Z`)
2. ✅ Extract the version number from the tag
3. ✅ Create a `release-X.Y.Z` branch from the release tag
4. ✅ Update all `pom.xml` files to the release version
5. ✅ Commit the version change
6. ✅ **Run full test suite** to verify the release
7. ✅ Build and deploy artifacts to Maven Central with:
   - Compiled JARs
   - Source JARs
   - Javadoc JARs
   - GPG signatures
8. ✅ Update the release tag to point to the release commit
9. ✅ Update `pom.xml` files to the next SNAPSHOT version
10. ✅ Push all commits to the release branch

**Workflow Diagram:**
```
User Action: Create Release (tag: v1.2.0)
    ↓
GitHub: Creates tag v1.2.0 → commit A (from main)
    ↓
Workflow: Checkout tag v1.2.0
    ↓
Workflow: Create branch release-1.2.0
    ↓
Workflow: Update pom.xml to 1.2.0 → commit B
    ↓
Workflow: Run tests (mvn verify)
    ↓
Workflow: Build & deploy to Maven Central
    ↓
Workflow: Move tag v1.2.0 to commit B
    ↓
Workflow: Update pom.xml to 1.2.1-SNAPSHOT → commit C
    ↓
Workflow: Push branch release-1.2.0
    ↓
Result:
  - Tag v1.2.0 → commit B (release version)
  - Branch release-1.2.0 → commit C (next SNAPSHOT)
  - Artifacts deployed to Maven Central
```

### Step 3: Verify Release

1. Check the [Actions tab](https://github.com/BerryCloud/xapi-java/actions) to ensure the workflow completed successfully
2. Verify the release branch was created: `release-X.Y.Z`
3. Verify artifacts are available on [Maven Central](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model)

## Release Branch Strategy

- **Main branch (`main`)**: Contains development code with `-SNAPSHOT` versions
- **Release branches (`release-X.Y.Z`)**: Created automatically for each release
  - Contains two commits:
    1. Version update to release version (X.Y.Z)
    2. Version update to next development version (X.Y.Z+1-SNAPSHOT)
- **Release tags (`vX.Y.Z`)**: Points to the release version commit

## Version Numbering

This project follows [Semantic Versioning](https://semver.org/):

- **MAJOR** version (X): Incompatible API changes
- **MINOR** version (Y): Backward-compatible functionality additions
- **PATCH** version (Z): Backward-compatible bug fixes

The automated workflow increments the **PATCH** version for the next development iteration.

## Manual Override (Not Recommended)

For special cases, you can still manually trigger the deprecated "Maven Release (Manual)" workflow:

1. Go to [Actions](https://github.com/BerryCloud/xapi-java/actions)
2. Select "Maven Release (Manual - Deprecated)"
3. Click "Run workflow"

**Note**: This method is deprecated and should only be used if the automated process fails.

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
   | Branch already exists | Delete existing branch (see below) before creating new release |
   | Permission denied | Ensure workflow has `contents: write` permission |

4. **After fixing issues:**
   - Delete the failed release and tag in GitHub UI
   - Delete the release branch if it was created: `git push origin --delete release-X.Y.Z`
   - Create a new release with the same tag

### Release Branch Already Exists

If you need to re-release the same version:

1. Delete the existing release branch:
   ```bash
   git push origin --delete release-X.Y.Z
   ```
2. Delete the existing release in GitHub UI:
   - Go to Releases → Click on the release → Delete release
3. Delete the tag:
   - Go to Tags → Find the tag → Delete tag
4. Create a new release with the same version tag

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

## Required Secrets

The following GitHub secrets must be configured for releases to work:

- `OSSRH_USERNAME`: Sonatype OSSRH username
- `OSSRH_TOKEN`: Sonatype OSSRH token
- `MAVEN_GPG_PRIVATE_KEY`: GPG private key for signing artifacts
- `MAVEN_GPG_PASSPHRASE`: Passphrase for the GPG private key

## Snapshot Releases

Snapshot versions are automatically published to Maven Central's snapshot repository when changes are pushed to `release-*` branches. See the "Publish snapshot to the Sonatype OSSRH" workflow.
