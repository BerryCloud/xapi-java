// Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
'use strict';

const fs = require('node:fs');
const path = require('node:path');

function summarise(directory, exitCode, version) {
  const files = fs.readdirSync(directory).filter(file => file.endsWith('.log'));
  if (files.length !== 1) {
    throw new Error('Expected exactly one ADL result log from a fresh run.');
  }
  const report = JSON.parse(fs.readFileSync(path.join(directory, files[0]), 'utf8'));
  const { total, passed, failed, version: actualVersion } = report.summary || {};
  if (report.state !== 'finished' || actualVersion !== version
      || ![total, passed, failed].every(value => Number.isSafeInteger(value) && value >= 0)
      || total === 0 || passed + failed === 0 || passed + failed > total) {
    throw new Error('ADL did not produce a completed, non-empty report for the requested xAPI version.');
  }
  // The upstream CLI exits with the failure count; process exit codes wrap at 256.
  if (!Number.isInteger(exitCode) || exitCode < 0 || exitCode > 255
      || exitCode !== failed % 256) {
    throw new Error(`ADL runner exit code ${exitCode} does not match its result report.`);
  }

  let summary = `**Passed: ${passed} · Failed: ${failed}**\n`;
  const notRun = total - passed - failed;
  if (notRun > 0) {
    summary += `\nNot run: ${notRun} of ${total} discovered tests (pending or blocked by failed hooks).\n`;
  }
  return summary;
}

if (require.main === module) {
  try {
    const summary = summarise(process.argv[2], Number(process.argv[3]), process.argv[4]);
    console.log(summary);
    if (process.env.GITHUB_STEP_SUMMARY) {
      fs.appendFileSync(process.env.GITHUB_STEP_SUMMARY, summary);
    }
  } catch (error) {
    console.error(`Conformance run failed: ${error.message}`);
    if (process.env.GITHUB_STEP_SUMMARY) {
      fs.appendFileSync(process.env.GITHUB_STEP_SUMMARY,
        '**Conformance run incomplete.** No reliable pass/fail counts are available. See the detailed results artefact.\n');
    }
    process.exitCode = 1;
  }
}

module.exports = { summarise };
