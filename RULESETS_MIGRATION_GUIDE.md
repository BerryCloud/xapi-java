# GitHub Rulesets Migration Guide

This guide provides step-by-step instructions for migrating from classic branch protection rules to GitHub Rulesets for the xAPI Java repository.

## Why Migrate to Rulesets?

GitHub Rulesets are the modern replacement for classic branch protection rules, offering:

1. **Better bypass controls:** More granular control over who can bypass rules
2. **Pattern matching:** Apply rules to multiple branches using patterns
3. **GitHub App integration:** Better support for GitHub Apps authentication
4. **Future-proof:** GitHub is investing in Rulesets as the long-term solution
5. **Audit improvements:** Better tracking of who bypassed rules and when
6. **Consistency:** Easier to maintain consistent rules across repositories

## Prerequisites

Before starting the migration:

1. ✅ Document all existing branch protection settings for `main`
2. ✅ Have administrative access to the repository
3. ✅ (Optional but recommended) Create a GitHub App for the release workflow
4. ✅ Ensure all stakeholders are aware of the change

## Migration Steps

### Phase 1: Document Current Settings

1. **Navigate to Branch Protection Rules:**
   - Go to: https://github.com/BerryCloud/xapi-java/settings/branches
   - Click on the edit button for the `main` branch rule

2. **Document Current Settings:**
   Take note of the following (example settings to check):
   ```
   ☐ Require a pull request before merging
     ☐ Require approvals: ___ (number)
     ☐ Dismiss stale pull request approvals when new commits are pushed
     ☐ Require review from Code Owners
     ☐ Restrict who can dismiss pull request reviews
     ☐ Allow specified actors to bypass required pull requests
     ☐ Require approval of the most recent reviewable push
   
   ☐ Require status checks to pass before merging
     ☐ Require branches to be up to date before merging
     ☐ Status checks that are required: (list them)
   
   ☐ Require conversation resolution before merging
   ☐ Require signed commits
   ☐ Require linear history
   ☐ Require deployments to succeed before merging
   
   ☐ Lock branch
   ☐ Do not allow bypassing the above settings
   ☐ Restrict who can push to matching branches
     - Allowed actors: (list them)
   
   ☐ Allow force pushes
     ☐ Everyone
     ☐ Specify who can force push
   
   ☐ Allow deletions
   ```

3. **Take Screenshots:**
   - Capture screenshots of all settings pages
   - Save them for reference during and after migration

### Phase 2: Create GitHub App (Recommended)

Follow the detailed instructions in `RELEASE_WORKFLOW_AUTHENTICATION.md`, Option 1, steps 1-4.

**Quick summary:**
1. Create GitHub App with `Contents: Read and write` permission
2. Generate and save private key
3. Install app on the repository
4. Add `APP_ID` and `APP_PRIVATE_KEY` to repository secrets

### Phase 3: Create Ruleset

1. **Navigate to Rulesets:**
   - Go to: https://github.com/BerryCloud/xapi-java/settings/rules
   - Click "New ruleset" → "New branch ruleset"

2. **Basic Configuration:**
   ```
   Ruleset Name: Protect main branch
   Enforcement status: Active (or Evaluate first for testing)
   ```

3. **Configure Bypass List:**
   
   **Important:** The bypass list determines who can bypass ALL rules in this ruleset.
   
   Click "Add bypass" and add:
   - **Repository administrators** (if they should be able to bypass)
   - **The GitHub App** you created (search by name, e.g., `xapi-java-release-bot`)
   
   **Note:** Users/apps in the bypass list can push directly to `main` without creating PRs or passing status checks. Only add trusted actors.

4. **Target Branches:**
   - Click "Add target" → "Include by pattern"
   - Pattern: `main`
   - Click "Add inclusion pattern"

5. **Configure Rules (matching your documented settings):**

   **Rule: Require a pull request before merging**
   - ✅ Enable if currently enabled
   - Required approving review count: Set to match current setting (e.g., 1)
   - Additional settings:
     - ☐ Dismiss stale pull request approvals when new commits are pushed
     - ☐ Require review from Code Owners
     - ☐ Require approval of the most recent reviewable push
     - ☐ Require conversation resolution before merging

   **Rule: Require status checks to pass**
   - ✅ Enable if currently enabled
   - Click "Add checks"
   - Add each status check that's currently required (e.g., `build`, `test`, `CheckStyle`, etc.)
   - ☐ Require branches to be up to date before merging (match current setting)

   **Rule: Block force pushes**
   - ✅ Enable (recommended)

   **Rule: Require linear history**
   - ✅ Enable if currently enabled

   **Rule: Require signed commits**
   - ✅ Enable if currently enabled

   **Metadata Restrictions (optional):**
   - Restrict commit metadata (if needed)
   - Restrict commit author email patterns
   - Restrict commit messages
   - Restrict branch names (not applicable here)

6. **Review and Create:**
   - Review all settings carefully
   - Compare with your documented current settings
   - Click "Create" to create the ruleset

### Phase 4: Test the Ruleset

**Option A: Test in Evaluate Mode First**

If you set "Enforcement status: Evaluate" in step 3.2:
1. The ruleset won't actively block anything yet
2. Make test pushes and PRs
3. Check the ruleset insights to see what would have been blocked
4. Go to Settings → Rules → Rulesets → Click your ruleset → Change to "Active"

**Option B: Test with Active Mode**

If you set the ruleset to "Active" immediately:
1. **Test PR requirements:**
   - Create a test branch
   - Make a small change
   - Try to push directly to `main` → Should be blocked
   - Create a PR → Should require approval
   - Merge the PR → Should work

2. **Test status checks:**
   - Create a PR with failing tests (if applicable)
   - Verify PR cannot be merged until checks pass
   - Fix tests and verify merge works

3. **Test bypass (with GitHub App):**
   - This will be tested when updating the workflow (next phase)

### Phase 5: Update Release Workflow

1. **Add token generation step to workflow:**
   
   Edit `.github/workflows/release.yml` and add the following step after the "Determine target branch" step:

   ```yaml
   - name: Generate GitHub App Token
     id: generate_token
     uses: actions/create-github-app-token@v1
     with:
       app-id: ${{ secrets.APP_ID }}
       private-key: ${{ secrets.APP_PRIVATE_KEY }}
   ```

2. **Update checkout step:**
   
   Change the `token` parameter in the "Checkout repository" step:

   ```yaml
   - name: Checkout repository
     uses: actions/checkout@v3
     with:
       ref: ${{ steps.target_branch.outputs.target_branch }}
       fetch-depth: 0
       token: ${{ steps.generate_token.outputs.token }}
   ```

3. **Commit and test:**
   - Commit the workflow changes
   - Create a test release (or pre-release)
   - Monitor the workflow execution
   - Verify the workflow can push commits to `main`

### Phase 6: Remove Classic Branch Protection (Optional)

**Wait at least 1-2 weeks after migration before removing the old rule.**

Once you're confident the ruleset is working correctly:

1. Go to: https://github.com/BerryCloud/xapi-java/settings/branches
2. Find the branch protection rule for `main`
3. Click "Delete"
4. Confirm deletion

**Note:** You can keep both the ruleset and classic branch protection active simultaneously. The stricter rules will apply.

### Phase 7: Monitor and Iterate

1. **Monitor the first few releases:**
   - Watch workflow logs carefully
   - Verify commits are pushed successfully
   - Check that bypass is working correctly

2. **Monitor PR activity:**
   - Ensure PR requirements are working as expected
   - Verify status checks are running correctly
   - Check that appropriate actors can bypass when needed

3. **Review ruleset insights:**
   - Go to Settings → Rules → Rulesets → Click your ruleset → "Insights"
   - Review who has bypassed rules and when
   - Check for any unexpected behavior

4. **Iterate as needed:**
   - Adjust ruleset settings based on team feedback
   - Add or remove bypass actors as needed
   - Refine status check requirements

## Troubleshooting

### Issue: Ruleset blocks legitimate pushes

**Symptom:** Pushes that should be allowed are blocked

**Solution:**
1. Check the bypass list - ensure the correct actors are added
2. For GitHub App: Verify the app has `Contents: Read and write` permission
3. For GitHub App: Verify the app is installed on the repository
4. Check workflow logs for specific error messages

### Issue: Status checks not running

**Symptom:** Required status checks are not being enforced

**Solution:**
1. Verify status check names exactly match what appears in GitHub
2. Check that the workflows that create these status checks are running
3. Status check names are case-sensitive

### Issue: Cannot merge PRs even with approvals

**Symptom:** PRs show as blocked even with required approvals

**Solution:**
1. Check "Require branches to be up to date" - if enabled, branch must be rebased
2. Verify all required status checks have passed
3. Check for required conversation resolution

### Issue: Release workflow fails with "Resource not accessible by integration"

**Symptom:** GitHub App token doesn't work

**Solution:**
1. Verify `APP_ID` and `APP_PRIVATE_KEY` secrets are correctly set
2. Check that the GitHub App has `Contents: Read and write` permission
3. Verify the app is in the ruleset bypass list
4. Ensure the app is installed on the repository

### Issue: Want to temporarily bypass ruleset

**Solution:**
1. As an admin, you can temporarily disable the ruleset:
   - Go to Settings → Rules → Rulesets
   - Click your ruleset → Change to "Disabled"
   - Make necessary changes
   - Re-enable the ruleset
2. Alternatively, add yourself to the bypass list temporarily

## Comparison: Classic Branch Protection vs. Rulesets

| Feature | Classic Branch Protection | Rulesets |
|---------|---------------------------|----------|
| Pattern matching | Single branch or wildcard | Multiple patterns, inclusion/exclusion |
| Bypass control | Limited (administrators or specific users) | Granular (roles, apps, users, teams) |
| GitHub App support | Limited | Native support |
| Audit trail | Basic | Enhanced with insights |
| Status checks | List of check names | List of check names |
| Multiple rules on same branch | Only one rule | Multiple rulesets can apply |
| Organization-level rules | Not available | Available |
| Future support | Maintenance mode | Active development |

## FAQ

**Q: Can I have both classic branch protection and rulesets active?**
A: Yes, both can be active simultaneously. The most restrictive rules will apply.

**Q: What happens if I delete the classic rule before testing the ruleset?**
A: Your branch will temporarily have no protection. Test the ruleset thoroughly first.

**Q: Can I rollback to classic branch protection after migration?**
A: Yes, you can recreate the classic branch protection rule at any time.

**Q: Do rulesets support all features of classic branch protection?**
A: Most features are supported. Some advanced features might differ slightly in implementation.

**Q: How do I know if the GitHub App is properly configured?**
A: Test by running the release workflow. Check workflow logs for authentication errors.

**Q: Can I apply rulesets to multiple branches?**
A: Yes, use patterns like `main`, `release-*`, or `feature/*` to apply to multiple branches.

**Q: What if my organization has organization-level rules?**
A: Organization-level rulesets and repository-level rulesets can coexist. The most restrictive applies.

## Additional Resources

- [GitHub Rulesets Documentation](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/about-rulesets)
- [Creating Rulesets](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/creating-rulesets-for-a-repository)
- [Managing Rulesets](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/managing-rulesets-for-a-repository)
- [Available Rules](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/available-rules-for-rulesets)
- [GitHub Apps Documentation](https://docs.github.com/en/apps)

## Summary Checklist

Use this checklist to track your migration progress:

- [ ] Documented all current branch protection settings
- [ ] Took screenshots of current settings
- [ ] Created GitHub App for release workflow
- [ ] Added `APP_ID` and `APP_PRIVATE_KEY` secrets to repository
- [ ] Created new ruleset with matching settings
- [ ] Added GitHub App to ruleset bypass list
- [ ] Tested ruleset in Evaluate mode (optional)
- [ ] Tested PR requirements with ruleset active
- [ ] Updated release workflow with token generation
- [ ] Tested release workflow with test release
- [ ] Monitored first production release
- [ ] Removed classic branch protection rule (after 1-2 weeks)
- [ ] Documented changes for team
- [ ] Updated internal documentation

## Contact and Support

If you encounter issues during migration:
1. Review the troubleshooting section above
2. Check GitHub's documentation and status page
3. Review workflow logs for specific error messages
4. Consider testing in a fork or test repository first
