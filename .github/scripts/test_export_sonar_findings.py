"""Offline regression tests for the Sonar exporter, with no external requests."""

# Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.

import base64
import contextlib
import importlib.util
import io
import json
import os
from pathlib import Path
import tempfile
import unittest
import urllib.error
from unittest.mock import patch

SPEC = importlib.util.spec_from_file_location(
    "sonar_export", Path(__file__).with_name("export-sonar-findings.py")
)
exporter = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(exporter)


class ExportTests(unittest.TestCase):
    def test_collects_all_pages_with_original_filters(self):
        pages = [
            {"issues": [{"key": "first"}], "paging": {"total": 2}},
            {"issues": [{"key": "second"}], "paging": {"total": 2}},
        ]
        with patch.object(exporter, "request", side_effect=pages) as request:
            issues = exporter.collect("issues/search", {"branch": "main"}, "issues")
        self.assertEqual(["first", "second"], [i["key"] for i in issues])
        self.assertEqual([1, 2], [call.args[1]["p"] for call in request.call_args_list])
        for call in request.call_args_list:
            self.assertEqual("main", call.args[1]["branch"])
            self.assertEqual(500, call.args[1]["ps"])

    def test_rejects_empty_page_in_incomplete_report(self):
        response = {"issues": [], "paging": {"total": 1}}
        with patch.object(exporter, "request", return_value=response):
            with self.assertRaisesRegex(RuntimeError, "could not export all"):
                exporter.collect("issues/search", {}, "issues")

    def test_rejects_result_count_changing_during_export(self):
        response = {"issues": [1, 2], "paging": {"total": 1}}
        with patch.object(exporter, "request", return_value=response):
            with self.assertRaisesRegex(RuntimeError, "changed during pagination"):
                exporter.collect("issues/search", {}, "issues")

    def test_rejects_sonars_ten_thousand_result_limit(self):
        response = {"issues": [None] * 500, "paging": {"total": 10001}}
        with patch.object(exporter, "request", return_value=response):
            with self.assertRaisesRegex(RuntimeError, "could not export all 10001"):
                exporter.collect("issues/search", {}, "issues")

    def test_requests_encode_parameters_and_send_credentials_only_in_header(self):
        with patch.dict(os.environ, {"SONAR_TOKEN": "test-token"}):
            with patch.object(exporter.urllib.request, "urlopen", return_value=io.StringIO('{"ok":true}')) as urlopen:
                result = exporter.request("rules/show", {"key": "java:S1234"})
        self.assertEqual({"ok": True}, result)
        request = urlopen.call_args.args[0]
        self.assertIn("key=java%3AS1234", request.full_url)
        self.assertNotIn("test-token", request.full_url)
        credentials = base64.b64encode(b"test-token:").decode()
        self.assertEqual(f"Basic {credentials}", request.get_header("Authorization"))

    def test_public_requests_need_no_token(self):
        with patch.dict(os.environ, {}, clear=True):
            with patch.object(exporter.urllib.request, "urlopen", return_value=io.StringIO('{}')) as urlopen:
                exporter.request("issues/search", {})
        self.assertIsNone(urlopen.call_args.args[0].get_header("Authorization"))

    def test_http_errors_do_not_expose_response_or_credentials(self):
        error = urllib.error.HTTPError("https://example.com", 403, "secret response", {}, None)
        with patch.object(exporter.urllib.request, "urlopen", side_effect=error):
            with self.assertRaisesRegex(RuntimeError, "^Sonar issues/search returned HTTP 403$"):
                exporter.request("issues/search", {})

    def test_exports_complete_findings_rules_and_summary(self):
        issue = {
            "key": "issue-1", "rule": "java:S1234", "component": "project:File.java",
            "line": 12, "status": "OPEN", "message": "Example finding",
        }
        hotspots = [{"key": "hotspot-1"}]
        with tempfile.TemporaryDirectory() as directory:
            summary = Path(directory) / "sonar-summary.md"
            with contextlib.chdir(directory):
                with patch.object(exporter, "collect", side_effect=[[issue, issue], hotspots]) as collect:
                    with patch.object(exporter, "request", return_value={"rule": {"key": issue["rule"]}}) as request:
                        with contextlib.redirect_stdout(io.StringIO()) as log:
                            exporter.main()
                report = json.loads(Path("sonar-findings.json").read_text())
            self.assertEqual([issue, issue], report["analyses"]["main"]["issues"])
            self.assertEqual(hotspots, report["analyses"]["main"]["hotspots"])
            self.assertEqual("main", report["analyses"]["main"]["selector"]["branch"])
            self.assertEqual("false", collect.call_args_list[0].args[1]["resolved"])
            self.assertNotIn("sinceLeakPeriod", collect.call_args_list[0].args[1])
            request.assert_called_once_with("rules/show", {"key": issue["rule"], "organization": "berrycloud"})
            self.assertIn("2 unresolved issues, 1 security hotspots", summary.read_text())
            self.assertIn('"line": 12', log.getvalue())

    def test_exports_empty_backlog(self):
        with tempfile.TemporaryDirectory() as directory:
            summary = Path(directory) / "sonar-summary.md"
            with contextlib.chdir(directory):
                with patch.object(exporter, "collect", return_value=[]), patch.object(exporter, "request") as request:
                    with contextlib.redirect_stdout(io.StringIO()):
                        exporter.main()
                report = json.loads(Path("sonar-findings.json").read_text())
            request.assert_not_called()
            self.assertEqual({}, report["rules"])
            self.assertIn("0 unresolved issues", summary.read_text())


if __name__ == "__main__":
    unittest.main()
