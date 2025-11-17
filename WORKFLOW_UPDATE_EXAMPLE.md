# Release Workflow Update Example

This document shows the exact changes needed to update `.github/workflows/release.yml` to support bypassing branch protection.

## Option 1: Using GitHub App (Recommended)

Add the following step after the "Determine target branch" step and before "Checkout repository":

```yaml
- name: Generate GitHub App Token
  id: generate_token
  uses: actions/create-github-app-token@v1
  with:
    app-id: ${{ secrets.APP_ID }}
    private-key: ${{ secrets.APP_PRIVATE_KEY }}
```

Then update the checkout step to use the generated token:

```yaml
- name: Checkout repository
  uses: actions/checkout@v3
  with:
    ref: ${{ steps.target_branch.outputs.target_branch }}
    fetch-depth: 0
    token: ${{ steps.generate_token.outputs.token }}  # Changed from secrets.GITHUB_TOKEN
```

### Complete Updated Section

Here's the complete updated section of the workflow (lines 43-63):

```yaml
- name: Determine target branch
  id: target_branch
  run: |
    # Use target_commitish to determine the originating branch
    TARGET="${{ github.event.release.target_commitish }}"
    
    # If target_commitish is empty or a SHA, default to main
    if [[ -z "$TARGET" ]] || [[ "$TARGET" =~ ^[0-9a-f]{40}$ ]]; then
      TARGET="main"
    fi
    
    echo "target_branch=${TARGET}" >> $GITHUB_OUTPUT
    echo "Target branch: $TARGET"

- name: Generate GitHub App Token
  id: generate_token
  uses: actions/create-github-app-token@v1
  with:
    app-id: ${{ secrets.APP_ID }}
    private-key: ${{ secrets.APP_PRIVATE_KEY }}

- name: Checkout repository
  uses: actions/checkout@v3
  with:
    ref: ${{ steps.target_branch.outputs.target_branch }}
    fetch-depth: 0
    token: ${{ steps.generate_token.outputs.token }}
```

## Option 2: Using Personal Access Token

If using a PAT instead, only the checkout step needs to be updated:

```yaml
- name: Checkout repository
  uses: actions/checkout@v3
  with:
    ref: ${{ steps.target_branch.outputs.target_branch }}
    fetch-depth: 0
    token: ${{ secrets.RELEASE_PAT }}  # Changed from secrets.GITHUB_TOKEN
```

## Required Repository Secrets

### For GitHub App (Option 1):
- `APP_ID`: The numeric App ID from the GitHub App settings
- `APP_PRIVATE_KEY`: The complete private key content (including BEGIN/END lines)

### For PAT (Option 2):
- `RELEASE_PAT`: The Personal Access Token with `repo` permissions

## Testing the Changes

After implementing the changes and configuring branch protection/rulesets:

1. **Dry run test:**
   - Create a test branch from `main`
   - Temporarily modify the workflow to push to the test branch
   - Create a pre-release with a tag like `v0.0.0-test`
   - Verify the workflow completes and pushes commits

2. **Production test:**
   - Restore the workflow to push to the actual target branch
   - Create a real release (or pre-release if cautious)
   - Monitor the workflow execution
   - Verify commits are pushed to `main`

## Rollback Plan

If the changes don't work:

1. The workflow will fail at the push step (no damage to repository)
2. Delete the failed release and tag from GitHub UI
3. Investigate the error in workflow logs
4. Fix configuration issues (secrets, permissions, branch protection)
5. Retry the release

## Additional Notes

- The `actions/create-github-app-token` action requires the app to be installed on the repository
- The generated token is automatically scoped to the repository where the workflow runs
- The token expires after 1 hour (sufficient for the release workflow)
- Commits made with the app token will show the app as the author
