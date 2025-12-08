# Release Workflow Migration Guide

## Summary of Changes

This project has migrated from an **automated release workflow** to a **manual draft release workflow** for better control and predictability.

## What Changed

### Before (Automated Release)
- **Trigger**: Creating a GitHub Release automatically triggered the workflow
- **Process**: Release was created in GitHub UI → workflow ran automatically
- **Control**: Limited control over when releases happened

### After (Manual Draft Release)
- **Trigger**: Create draft release first, then manually trigger workflow via GitHub Actions UI
- **Process**: Create draft release → trigger workflow → enter draft title → workflow runs
- **Control**: Full control over release timing, review draft before publishing

## Migration Benefits

1. **Explicit Control**: Releases only happen when you explicitly trigger them
2. **No Surprises**: No accidental releases from automation
3. **Draft Review**: Create and review draft releases before deployment
4. **Release Notes**: Prepare comprehensive release notes in advance
5. **Artifact Attachment**: JAR files automatically uploaded to GitHub Release
6. **Review Process**: Better integration with code review and approval processes

## How to Use the New Workflow

See [RELEASING.md](../RELEASING.md) for complete instructions.

**Quick Start:**
1. Go to [Releases page](https://github.com/BerryCloud/xapi-java/releases)
2. Click "Draft a new release"
3. Fill in tag (e.g., `v1.2.0`), title (same as tag), and release notes
4. Click "Save draft"
5. Go to [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
6. Select "Manual Draft Release" workflow
7. Click "Run workflow"
8. Enter draft release title (e.g., `v1.2.0`)
9. Click "Run workflow" button

## Workflow Input

| Option | Description | Format |
|--------|-------------|--------|
| **Draft release title** | Title of the draft release to process | vX.Y.Z (e.g., v1.2.0) - MUST include 'v' prefix |

## Old Workflow

The old automated workflow (`release.yml`) has been:
- Completely removed from the repository
- Replaced with `manual-release.yml` (Manual Draft Release workflow)

**The automated release workflow is no longer available.** All releases must now use the manual draft release process.

## Questions?

- See [RELEASING.md](../RELEASING.md) for detailed documentation
- Check [manual-release.yml](workflows/manual-release.yml) for workflow implementation
- Open an issue if you encounter problems
