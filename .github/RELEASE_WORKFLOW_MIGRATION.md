# Release Workflow Migration Guide

## Summary of Changes

This project has migrated from an **automated release workflow** to a **manual release workflow** for better control and predictability.

## What Changed

### Before (Automated Release)
- **Trigger**: Creating a GitHub Release automatically triggered the workflow
- **Process**: Release was created in GitHub UI → workflow ran automatically
- **Control**: Limited control over when releases happened

### After (Manual Release)
- **Trigger**: Workflow must be manually triggered via GitHub Actions UI
- **Process**: Trigger workflow → enter version → workflow runs
- **Control**: Full control over release timing and options

## Migration Benefits

1. **Explicit Control**: Releases only happen when you explicitly trigger them
2. **No Surprises**: No accidental releases from automation
3. **Testing Options**: Dry-run capability to test releases without deployment
4. **Flexible**: Can skip GitHub Release creation or Maven Central deployment independently
5. **Review Process**: Better integration with code review and approval processes

## How to Use the New Workflow

See [RELEASING.md](../RELEASING.md) for complete instructions.

**Quick Start:**
1. Go to [Actions tab](https://github.com/BerryCloud/xapi-java/actions)
2. Select "Manual Release" workflow
3. Click "Run workflow"
4. Enter version (e.g., `1.2.0` - no `v` prefix)
5. Click "Run workflow" button

## Workflow Options

| Option | Description | Default |
|--------|-------------|---------|
| **Release version** | Version number (X.Y.Z format, no 'v') | Required |
| **Skip Maven Central** | Skip deployment (dry-run) | false |
| **Create GitHub Release** | Create release after deployment | true |

## Old Workflow

The old automated workflow (`release.yml`) has been:
- Renamed to `release-automated-deprecated.yml`
- Trigger disabled to prevent automatic execution
- Kept for reference only

**Do not use the old workflow.** It will not run automatically anymore.

## Questions?

- See [RELEASING.md](../RELEASING.md) for detailed documentation
- Check [manual-release.yml](workflows/manual-release.yml) for workflow implementation
- Open an issue if you encounter problems
