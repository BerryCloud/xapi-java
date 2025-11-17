# Release Workflow Authentication Guide

This document provides instructions for configuring the automated release workflow to commit to protected branches (like `main`) despite branch protection rules.

## Problem Statement

The automated release workflow needs to push commits back to the `main` branch as part of the Maven release process. However, GitHub branch protection rules prevent these automated commits from being pushed, causing the release workflow to fail.

Currently, the workflow uses `secrets.GITHUB_TOKEN`, which:
- Is automatically provided by GitHub Actions
- Has limited permissions and cannot bypass branch protection rules
- Cannot be used to trigger other workflows

## Solution Options

There are three primary approaches to solve this issue:

### Option 1: GitHub App (RECOMMENDED)

**Pros:**
- ✅ Modern, secure approach recommended by GitHub
- ✅ No need to create bot users in the organization
- ✅ Fine-grained permissions with audit trail
- ✅ Can bypass branch protection when configured
- ✅ Can trigger other workflows
- ✅ Tokens are short-lived and automatically rotated
- ✅ Organization-level app can be used across multiple repositories

**Cons:**
- ⚠️ Requires initial setup (creating and installing GitHub App)
- ⚠️ Slightly more complex than PAT

**Implementation Steps:**

1. **Create a GitHub App:**
   - Go to Organization Settings → Developer settings → GitHub Apps → New GitHub App
   - Name: `xapi-java-release-bot` (or similar)
   - Uncheck "Webhook Active" (not needed for this use case)
   - Set **Repository permissions:**
     - Contents: Read and write
     - Metadata: Read-only (automatically required)
   - Set **"Where can this GitHub App be installed?"** to "Only on this account"
   - Click "Create GitHub App"

2. **Generate Private Key:**
   - After creating the app, scroll to "Private keys" section
   - Click "Generate a private key"
   - Save the downloaded `.pem` file securely
   - Copy the entire content of the private key (including `-----BEGIN RSA PRIVATE KEY-----` and `-----END RSA PRIVATE KEY-----`)

3. **Install the App:**
   - In the app settings, click "Install App" in the left sidebar
   - Select your organization
   - Choose "Only select repositories" and select `xapi-java`
   - Click "Install"

4. **Configure Repository Secrets:**
   - Go to repository Settings → Secrets and variables → Actions
   - Add the following secrets:
     - `APP_ID`: The App ID from the GitHub App settings page
     - `APP_PRIVATE_KEY`: The entire private key content from the `.pem` file

5. **Update Release Workflow:**

   Add a new step at the beginning of the workflow to generate a token:

   ```yaml
   - name: Generate GitHub App Token
     id: generate_token
     uses: actions/create-github-app-token@v1
     with:
       app-id: ${{ secrets.APP_ID }}
       private-key: ${{ secrets.APP_PRIVATE_KEY }}
   ```

   Update the checkout step to use the generated token:

   ```yaml
   - name: Checkout repository
     uses: actions/checkout@v3
     with:
       ref: ${{ steps.target_branch.outputs.target_branch }}
       fetch-depth: 0
       token: ${{ steps.generate_token.outputs.token }}
   ```

6. **Configure Branch Protection or Rulesets:**

   **Using Classic Branch Protection:**
   - Go to repository Settings → Branches → Branch protection rules
   - Edit the rule for `main` branch
   - Under "Require status checks to pass before merging":
     - Check "Include administrators" if desired
   - Under "Restrict who can push to matching branches":
     - Add the GitHub App to the allowed list
   - Alternatively, check "Allow specified actors to bypass required pull requests" and add the app

   **Using GitHub Rulesets (Recommended - see Option 3):**
   - Configure bypass permissions for the GitHub App

### Option 2: Personal Access Token (PAT) or Fine-Grained PAT

**Pros:**
- ✅ Simple to set up
- ✅ Can bypass branch protection
- ✅ Can trigger other workflows

**Cons:**
- ❌ Tied to a personal account (not ideal for organizational automation)
- ❌ Requires creating a bot user account if you don't want to use a real user's PAT
- ❌ Tokens are long-lived (security concern)
- ❌ If the user leaves the organization, the workflow breaks
- ⚠️ Fine-grained PATs have limited repository access configuration

**Implementation Steps:**

1. **Create a Bot User (Optional but Recommended):**
   - Create a new GitHub user account (e.g., `xapi-java-bot`)
   - Add this user to the BerryCloud organization
   - Give the user write access to the repository

2. **Generate a Personal Access Token:**
   
   **Classic PAT:**
   - Go to User Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Click "Generate new token (classic)"
   - Name: `xapi-java-release-workflow`
   - Expiration: Choose appropriate duration (90 days, 1 year, or no expiration)
   - Scopes: Select `repo` (Full control of private repositories)
   - Click "Generate token"
   - Copy the token immediately (you won't be able to see it again)

   **Fine-Grained PAT (Recommended over Classic):**
   - Go to User Settings → Developer settings → Personal access tokens → Fine-grained tokens
   - Click "Generate new token"
   - Token name: `xapi-java-release-workflow`
   - Expiration: Choose appropriate duration
   - Repository access: Select "Only select repositories" → `xapi-java`
   - Permissions:
     - Contents: Read and write
     - Metadata: Read-only
   - Click "Generate token"
   - Copy the token immediately

3. **Configure Repository Secret:**
   - Go to repository Settings → Secrets and variables → Actions
   - Add a new secret:
     - Name: `RELEASE_PAT`
     - Value: The generated PAT

4. **Update Release Workflow:**

   Update the checkout step to use the PAT:

   ```yaml
   - name: Checkout repository
     uses: actions/checkout@v3
     with:
       ref: ${{ steps.target_branch.outputs.target_branch }}
       fetch-depth: 0
       token: ${{ secrets.RELEASE_PAT }}
   ```

5. **Configure Branch Protection:**
   - Go to repository Settings → Branches → Branch protection rules
   - Edit the rule for `main` branch
   - Under "Restrict who can push to matching branches":
     - Add the bot user (or the user whose PAT you're using) to the allowed list
   - Alternatively, the PAT with sufficient permissions can bypass certain restrictions

### Option 3: GitHub Rulesets (RECOMMENDED - Most Modern Approach)

GitHub Rulesets are the modern replacement for classic branch protection rules, offering:
- More granular control
- Better bypass mechanisms
- Easier management across multiple branches
- Integration with GitHub Apps

**Pros:**
- ✅ Modern approach that GitHub is actively developing
- ✅ More flexible than classic branch protection
- ✅ Better integration with GitHub Apps
- ✅ Can define bypass actors explicitly
- ✅ Can be applied to multiple branches with patterns
- ✅ Easier to manage in large organizations

**Cons:**
- ⚠️ Relatively new feature (may require familiarization)
- ⚠️ Requires migration from classic branch protection rules if already in use

**Implementation Steps:**

1. **Create or Update GitHub App (as in Option 1):**
   - Follow steps 1-4 from Option 1 to set up the GitHub App

2. **Migrate from Classic Branch Protection to Rulesets:**

   a. **Document Current Branch Protection Settings:**
      - Go to repository Settings → Branches
      - Note all current protection rules for `main` branch
      - Take screenshots or notes of all enabled options

   b. **Create a New Ruleset:**
      - Go to repository Settings → Rules → Rulesets
      - Click "New ruleset" → "New branch ruleset"
      - Name: `Protect main branch`
      - Enforcement status: "Active"
      - Bypass list:
        - Click "Add bypass"
        - Select "Repository roles" → Choose administrators if needed
        - Add the GitHub App created in Option 1
      - Target branches:
        - Select "Add target" → "Include by pattern"
        - Pattern: `main`
      - Rules (configure based on your current protection rules):
        - ✅ Require a pull request before merging
          - Required approvals: 1 (or as configured)
          - Dismiss stale pull request approvals: as configured
          - Require review from Code Owners: as configured
        - ✅ Require status checks to pass
          - Add required status checks (e.g., `build`, `test`)
          - Require branches to be up to date: as configured
        - ✅ Block force pushes
        - ✅ Require linear history (if desired)
        - ✅ Require signed commits (if desired)
      - Click "Create"

   c. **Test the Ruleset:**
      - Try making a direct push to `main` (should be blocked)
      - Verify PR requirements work as expected

   d. **Remove Classic Branch Protection (after testing):**
      - Go to repository Settings → Branches
      - Delete the old branch protection rule for `main`

3. **Update Release Workflow (as in Option 1):**

   ```yaml
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

4. **Verify Bypass Works:**
   - The GitHub App token should now be able to bypass the ruleset
   - The workflow will be able to push commits to `main`

## Recommended Approach

**For this repository, we recommend: Option 3 (GitHub Rulesets with GitHub App)**

This combination provides:
1. Modern, GitHub-recommended approach
2. No bot user accounts needed
3. Fine-grained security with audit trail
4. Easy to manage and maintain
5. Better organizational scalability

## Implementation Workflow Code Changes

Here's the specific change needed in `.github/workflows/release.yml`:

**Add the token generation step after validation steps and before checkout:**

```yaml
- name: Generate GitHub App Token
  id: generate_token
  uses: actions/create-github-app-token@v1
  with:
    app-id: ${{ secrets.APP_ID }}
    private-key: ${{ secrets.APP_PRIVATE_KEY }}
```

**Update the checkout step (line 58-63):**

```yaml
- name: Checkout repository
  uses: actions/checkout@v3
  with:
    ref: ${{ steps.target_branch.outputs.target_branch }}
    fetch-depth: 0
    token: ${{ steps.generate_token.outputs.token }}  # Changed from secrets.GITHUB_TOKEN
```

## Security Considerations

1. **GitHub App Permissions:**
   - Only grant minimum required permissions (Contents: Read and write)
   - Regularly audit app usage in organization settings

2. **PAT Security (if using Option 2):**
   - Use fine-grained PATs over classic PATs
   - Set appropriate expiration dates
   - Rotate tokens regularly
   - Use a dedicated bot account, not personal accounts

3. **Bypass Permissions:**
   - Only grant bypass permissions to the specific actors that need them
   - Regularly review bypass list in Rulesets or branch protection settings

4. **Audit Trail:**
   - All commits pushed by GitHub App will show as authored by the app
   - Review Actions logs regularly for any unauthorized access attempts

## Troubleshooting

### Common Issues

**Issue: Token doesn't bypass branch protection**
- **Solution:** Ensure the GitHub App or user is explicitly added to the bypass list in branch protection or rulesets

**Issue: "Resource not accessible by integration" error**
- **Solution:** Check that the GitHub App has "Contents: Read and write" permission

**Issue: App token expires during workflow**
- **Solution:** GitHub App tokens are automatically valid for 1 hour, which is sufficient for most workflows

**Issue: Cannot see GitHub App in bypass list**
- **Solution:** Ensure the app is installed on the repository first

## Testing the Solution

1. **Test without releasing:**
   - Create a test branch
   - Modify the workflow to push to the test branch instead of `main`
   - Verify the workflow can push successfully

2. **Test with a pre-release:**
   - Create a pre-release tag (e.g., `v1.0.0-rc1`)
   - Verify the workflow completes successfully
   - Check that commits are pushed to the target branch

3. **Monitor first production release:**
   - Watch the workflow execution closely
   - Verify commits are pushed to `main`
   - Verify the tag is created correctly

## Additional Resources

- [GitHub Apps Documentation](https://docs.github.com/en/apps)
- [Creating a GitHub App](https://docs.github.com/en/apps/creating-github-apps/about-creating-github-apps/about-creating-github-apps)
- [GitHub Rulesets Documentation](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/about-rulesets)
- [actions/create-github-app-token](https://github.com/actions/create-github-app-token)
- [Managing branch protection rules](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/managing-a-branch-protection-rule)

## Next Steps for Repository Maintainers

1. **Decision:** Choose between GitHub App + Rulesets (recommended) or PAT approach
2. **Setup:** Follow the implementation steps for your chosen option
3. **Configure:** Set up repository secrets (`APP_ID` and `APP_PRIVATE_KEY` or `RELEASE_PAT`)
4. **Update:** Modify `.github/workflows/release.yml` with the token generation step
5. **Configure Branch Protection:** Set up Rulesets or update classic branch protection with bypass permissions
6. **Test:** Create a test release to verify the workflow works
7. **Monitor:** Watch the first production release carefully
8. **Document:** Update `RELEASING.md` if any manual steps are needed during releases
