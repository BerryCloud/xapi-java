# Instructions for Repository Maintainers

## What This PR Provides

This PR contains comprehensive documentation to solve the issue: **"Allow release workflow to commit to main branch despite branch protection rules"**

The automated release workflow currently fails when trying to push commits to `main` because it uses `secrets.GITHUB_TOKEN`, which cannot bypass branch protection rules.

## What You Need to Do

This is a **configuration task** - you need to update repository settings and the workflow file, but no code changes are required.

### Quick Implementation (30-40 minutes)

Follow these steps to implement the recommended solution:

#### 1. Read the Documentation (5 minutes)

Start with one of these based on your needs:

- **For quick implementation:** [QUICK_START_GUIDE.md](QUICK_START_GUIDE.md)
- **For complete understanding:** [BRANCH_PROTECTION_README.md](BRANCH_PROTECTION_README.md)
- **For decision making:** [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)

#### 2. Create GitHub App (15 minutes)

Follow the detailed instructions in [RELEASE_WORKFLOW_AUTHENTICATION.md](RELEASE_WORKFLOW_AUTHENTICATION.md), Option 1.

**Quick steps:**
1. Go to: https://github.com/organizations/BerryCloud/settings/apps
2. Click "New GitHub App"
3. Name: `xapi-java-release-bot` (or similar)
4. Uncheck "Webhook Active"
5. Set permissions: Repository → Contents: **Read and write**
6. Create the app
7. Generate and download private key
8. Install app on `xapi-java` repository
9. Note the App ID

#### 3. Add Repository Secrets (2 minutes)

Go to: https://github.com/BerryCloud/xapi-java/settings/secrets/actions

Add two secrets:
- **`APP_ID`**: The numeric ID from the GitHub App
- **`APP_PRIVATE_KEY`**: Complete content of the `.pem` file (including BEGIN/END lines)

#### 4. Update Workflow File (5 minutes)

Edit `.github/workflows/release.yml`:

**After line 56** (after the "Determine target branch" step), add:

```yaml
- name: Generate GitHub App Token
  id: generate_token
  uses: actions/create-github-app-token@v1
  with:
    app-id: ${{ secrets.APP_ID }}
    private-key: ${{ secrets.APP_PRIVATE_KEY }}
```

**Update line 63** (in the "Checkout repository" step):

Change:
```yaml
token: ${{ secrets.GITHUB_TOKEN }}
```

To:
```yaml
token: ${{ steps.generate_token.outputs.token }}
```

See [WORKFLOW_UPDATE_EXAMPLE.md](WORKFLOW_UPDATE_EXAMPLE.md) for the complete code.

#### 5. Configure Branch Protection (5 minutes)

**Option A: Using GitHub Rulesets (RECOMMENDED)**

See [RULESETS_MIGRATION_GUIDE.md](RULESETS_MIGRATION_GUIDE.md) for detailed steps.

Quick steps:
1. Go to: https://github.com/BerryCloud/xapi-java/settings/rules
2. Create new branch ruleset
3. Name: `Protect main branch`
4. Add GitHub App to bypass list
5. Target: Pattern `main`
6. Configure your protection rules
7. Save

**Option B: Using Classic Branch Protection**

1. Go to: https://github.com/BerryCloud/xapi-java/settings/branches
2. Edit the rule for `main`
3. Under bypass settings, add the GitHub App
4. Save

#### 6. Test (10 minutes)

1. Commit the workflow changes
2. Create a test pre-release: Tag `v0.0.0-test`
3. Monitor the workflow in Actions tab
4. Verify:
   - Workflow completes successfully
   - Commits are pushed to `main`
   - Tags are created correctly

## Alternative: Personal Access Token

If you cannot use a GitHub App, see [RELEASE_WORKFLOW_AUTHENTICATION.md](RELEASE_WORKFLOW_AUTHENTICATION.md), Option 2 for PAT-based approach.

⚠️ **Not recommended** - Requires bot user account and has lower security.

## Documentation Index

| Document | Use When |
|----------|----------|
| **BRANCH_PROTECTION_README.md** | You want an overview and navigation |
| **QUICK_START_GUIDE.md** | You need to implement this quickly (15 min) |
| **IMPLEMENTATION_SUMMARY.md** | You're deciding which approach to take |
| **RELEASE_WORKFLOW_AUTHENTICATION.md** | You want detailed steps for all options |
| **RULESETS_MIGRATION_GUIDE.md** | You want to modernize to Rulesets |
| **WORKFLOW_UPDATE_EXAMPLE.md** | You need exact code for workflow changes |

## Expected Outcome

After implementation:

✅ Release workflow can push commits to `main`  
✅ Branch protection still blocks regular pushes  
✅ PRs still require approvals and status checks  
✅ No bot user accounts needed in organization  
✅ Complete audit trail of all operations  
✅ Secure, maintainable solution  

## Support

- **Troubleshooting:** Each document has a troubleshooting section
- **FAQs:** See [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
- **Workflow errors:** Check Actions logs and compare with [WORKFLOW_UPDATE_EXAMPLE.md](WORKFLOW_UPDATE_EXAMPLE.md)

## Security Notes

The recommended GitHub App approach:
- Uses short-lived tokens (1-hour expiration)
- Has minimal permissions (only Contents: Read/Write)
- Is scoped to this repository only
- Provides complete audit trail
- Requires no organizational bot users

## Questions?

Refer to the comprehensive documentation provided. All common scenarios, issues, and questions are covered in the guides.

## Summary

**What:** Configure the release workflow to bypass branch protection  
**How:** Use GitHub App + Rulesets (recommended) or PAT  
**Time:** 30-40 minutes  
**Risk:** Low - workflow will fail safely if misconfigured  
**Security:** High - uses modern GitHub best practices  

---

**Start here:** [BRANCH_PROTECTION_README.md](BRANCH_PROTECTION_README.md) or [QUICK_START_GUIDE.md](QUICK_START_GUIDE.md)
