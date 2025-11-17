# Quick Start Guide: Enable Release Workflow Branch Commits

This is a condensed guide for experienced GitHub administrators. For detailed explanations, see `RELEASE_WORKFLOW_AUTHENTICATION.md`.

## TL;DR

The release workflow needs to bypass branch protection to push commits to `main`. Use a GitHub App with Rulesets for the best security and maintainability.

## Fastest Path (15 minutes)

### Step 1: Create GitHub App (5 minutes)

1. Go to: https://github.com/organizations/BerryCloud/settings/apps → "New GitHub App"
2. Name: `xapi-java-release-bot`
3. Uncheck "Webhook Active"
4. Permissions:
   - Repository → Contents: **Read and write**
   - Repository → Metadata: **Read-only** (auto-required)
5. Click "Create GitHub App"
6. Generate private key → Save the `.pem` file
7. Note the App ID (shown at top of page)
8. Click "Install App" → Select `xapi-java` repository

### Step 2: Add Secrets (2 minutes)

Go to: https://github.com/BerryCloud/xapi-java/settings/secrets/actions

Add two secrets:
- **`APP_ID`**: The numeric ID from step 1.7
- **`APP_PRIVATE_KEY`**: Entire content of the `.pem` file (including BEGIN/END lines)

### Step 3: Update Workflow (3 minutes)

Edit `.github/workflows/release.yml`:

**Add this step after line 56 (after "Determine target branch"):**

```yaml
- name: Generate GitHub App Token
  id: generate_token
  uses: actions/create-github-app-token@v1
  with:
    app-id: ${{ secrets.APP_ID }}
    private-key: ${{ secrets.APP_PRIVATE_KEY }}
```

**Update line 63 (in "Checkout repository" step):**

```yaml
token: ${{ steps.generate_token.outputs.token }}
```

### Step 4: Configure Branch Protection (5 minutes)

**Option A: Using Rulesets (Recommended)**

1. Go to: https://github.com/BerryCloud/xapi-java/settings/rules
2. Click "New ruleset" → "New branch ruleset"
3. Name: `Protect main branch`
4. Enforcement: **Active**
5. Bypass list: Add **`xapi-java-release-bot`** (the GitHub App)
6. Target: Pattern `main`
7. Rules: Enable your desired protections (PR requirements, status checks, etc.)
8. Click "Create"

**Option B: Using Classic Branch Protection**

1. Go to: https://github.com/BerryCloud/xapi-java/settings/branches
2. Edit the rule for `main`
3. Under "Allow specified actors to bypass required pull requests", add the GitHub App
4. Save changes

### Step 5: Test

Create a test pre-release (tag: `v0.0.0-test`) and verify the workflow completes successfully.

## Alternative: Personal Access Token (Not Recommended)

If you can't use a GitHub App:

1. Create a bot user account or use an existing user
2. Generate a fine-grained PAT with `Contents: Read and write` for this repository
3. Add as repository secret: `RELEASE_PAT`
4. Update workflow line 63: `token: ${{ secrets.RELEASE_PAT }}`
5. Add the user to branch protection bypass list

## Verification

✅ Release workflow can push commits to `main`  
✅ Branch protection still blocks regular pushes  
✅ PRs still require approvals and status checks  
✅ GitHub App appears in commit author for release commits

## Troubleshooting

| Error | Solution |
|-------|----------|
| "Resource not accessible by integration" | Check App has Contents: Read and write permission |
| "Protected branch update failed" | Add App to bypass list in branch protection/rulesets |
| "Bad credentials" | Check `APP_ID` and `APP_PRIVATE_KEY` secrets are correct |
| Token generation fails | Verify App is installed on the repository |

## What Changes

**Before:** Workflow fails when trying to push release commits  
**After:** Workflow successfully pushes release commits to `main`

**Security:** Only the GitHub App can bypass protection, audited in workflow logs

## Next Steps

- Review `RELEASE_WORKFLOW_AUTHENTICATION.md` for detailed explanations
- See `RULESETS_MIGRATION_GUIDE.md` for full Rulesets migration
- Update `RELEASING.md` if any process changes are needed

## Files to Review

- `.github/workflows/release.yml` - The workflow that needs updating
- `RELEASE_WORKFLOW_AUTHENTICATION.md` - Detailed guide with all options
- `RULESETS_MIGRATION_GUIDE.md` - Step-by-step Rulesets migration
- `WORKFLOW_UPDATE_EXAMPLE.md` - Exact code changes needed
