// Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
'use strict';

const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { spawnSync } = require('node:child_process');
const { test } = require('node:test');
const { summarise } = require('./lrs-conformance-summary.cjs');

function fixture(t, overrides = {}) {
  const directory = fs.mkdtempSync(path.join(os.tmpdir(), 'lrs-conformance-'));
  t.after(() => fs.rmSync(directory, { recursive: true, force: true }));
  const report = {
    state: 'finished',
    summary: { total: 300, passed: 40, failed: 260, version: '1.0.3' },
    ...overrides,
  };
  fs.writeFileSync(path.join(directory, 'run.log'), JSON.stringify(report));
  return directory;
}

test('reports failures informationally, including wrapped upstream exit codes', t => {
  assert.equal(summarise(fixture(t), 4, '1.0.3'), '**Passed: 40 · Failed: 260**\n');
});

test('reports an all-passing run', t => {
  const directory = fixture(t, {
    summary: { total: 10, passed: 10, failed: 0, version: '1.0.3' },
  });
  assert.equal(summarise(directory, 0, '1.0.3'), '**Passed: 10 · Failed: 0**\n');
});

test('makes pending or hook-blocked tests visible', t => {
  const directory = fixture(t, {
    summary: { total: 310, passed: 40, failed: 260, version: '1.0.3' },
  });
  assert.match(summarise(directory, 4, '1.0.3'), /Not run: 10 of 310/);
});

test('rejects crashed, cancelled, empty, malformed and inconsistent reports', t => {
  for (const state of ['error', 'started', 'cancelled', 'notStarted']) {
    assert.throws(() => summarise(fixture(t, { state }), 4, '1.0.3'));
  }
  for (const summary of [
    { total: 0, passed: 0, failed: 0, version: '1.0.3' },
    { total: 10, passed: 0, failed: 0, version: '1.0.3' },
    { total: 10, passed: 11, failed: 0, version: '1.0.3' },
    { total: 10, passed: null, failed: 1, version: '1.0.3' },
    { total: 10, passed: -1, failed: 1, version: '1.0.3' },
    { total: 10, passed: 9.5, failed: 0.5, version: '1.0.3' },
    { total: 300, passed: 40, failed: 260, version: '2.0.0' },
    undefined,
  ]) {
    assert.throws(() => summarise(fixture(t, { summary }), 4, '1.0.3'));
  }
  for (const exitCode of [0, 1, 124, 137, 256, NaN]) {
    assert.throws(() => summarise(fixture(t), exitCode, '1.0.3'));
  }
});

test('rejects missing, truncated or multiple result logs', t => {
  const directory = fixture(t);
  fs.writeFileSync(path.join(directory, 'run.log'), '{');
  assert.throws(() => summarise(directory, 4, '1.0.3'));
  fs.unlinkSync(path.join(directory, 'run.log'));
  assert.throws(() => summarise(directory, 4, '1.0.3'));
  fs.writeFileSync(path.join(directory, 'one.log'), '{}');
  fs.writeFileSync(path.join(directory, 'two.log'), '{}');
  assert.throws(() => summarise(directory, 4, '1.0.3'));
  assert.throws(() => summarise(path.join(directory, 'missing'), 4, '1.0.3'));
});

test('CLI writes the Actions summary and fails only for runner/report errors', t => {
  const directory = fixture(t);
  const summaryPath = path.join(directory, 'summary.md');
  const run = exitCode => spawnSync(process.execPath,
    [path.join(__dirname, 'lrs-conformance-summary.cjs'), directory, exitCode, '1.0.3'],
    { encoding: 'utf8', env: { ...process.env, GITHUB_STEP_SUMMARY: summaryPath } });
  assert.equal(run('4').status, 0);
  assert.equal(fs.readFileSync(summaryPath, 'utf8'), '**Passed: 40 · Failed: 260**\n');
  assert.equal(run('1').status, 1);
  assert.match(fs.readFileSync(summaryPath, 'utf8'), /Conformance run incomplete/);
});
