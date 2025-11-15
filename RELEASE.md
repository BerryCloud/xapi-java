# Release Process

This document describes the process for releasing xAPI Java to Maven Central.

## Prerequisites

The project uses the [Central Publisher Portal](https://central.sonatype.org/publish/publish-portal-maven/) for publishing artifacts to Maven Central. This requires:

1. A Sonatype account with publishing rights to the `dev.learning.xapi` namespace
2. Generate a user token from [Central Portal](https://central.sonatype.com/)
3. Configure the following GitHub secrets:
   - `OSSRH_USERNAME`: Your Central Portal token username
   - `OSSRH_TOKEN`: Your Central Portal token password
   - `MAVEN_GPG_PRIVATE_KEY`: GPG private key for signing artifacts
   - `MAVEN_GPG_PASSPHRASE`: Passphrase for the GPG key

## Publishing a Release

Releases are automated using the `Maven Release` GitHub Actions workflow.

1. Go to the [Actions tab](https://github.com/BerryCloud/xapi-java/actions/workflows/maven-publish.yml)
2. Click "Run workflow"
3. Select the branch to release from (typically `main`)
4. Click "Run workflow"

The workflow will:
- Prepare the release (update version numbers, create tag)
- Build and sign the artifacts
- Deploy to Central Portal
- Automatically publish to Maven Central (typically available within 30 minutes)

## Publishing Snapshots

Snapshot versions are automatically published when pushing to `release-*` branches via the `maven-publish-snapshot.yml` workflow.

Snapshots are deployed to the Central Portal snapshot repository and are available immediately after the workflow completes.

## Manual Release (if needed)

If you need to release manually from your local machine:

```bash
# Configure your Maven settings.xml with Central Portal credentials
# ~/.m2/settings.xml should include:
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_TOKEN_USERNAME</username>
      <password>YOUR_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>

# Prepare and perform the release
./mvnw release:prepare release:perform
```

## Migration from Legacy OSSRH

The project was previously using the legacy OSSRH Nexus staging workflow. As of version 1.1.16, the project now uses the Central Publisher Portal which offers:

- Simplified authentication with user tokens
- Direct publishing API without staging repositories
- Faster publication to Maven Central
- Modern, reliable infrastructure

**No changes are required for consumers** - the artifacts are still published to Maven Central with the same coordinates.

## Additional Resources

- [Central Portal Documentation](https://central.sonatype.org/publish/publish-portal-maven/)
- [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/)
- [Signing Artifacts](https://central.sonatype.org/publish/requirements/gpg/)
