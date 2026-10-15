#!/usr/bin/env python3
"""Export unresolved Sonar issues and security hotspots for main."""

# Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.

import base64
import datetime
import json
import os
import urllib.error
import urllib.parse
import urllib.request

PROJECT = "BerryCloud_xapi-java"
BASE_URL = "https://sonarcloud.io"


def request(endpoint, parameters):
    """Read a Sonar API response without logging credentials or response bodies."""
    url = f"{BASE_URL}/api/{endpoint}?{urllib.parse.urlencode(parameters)}"
    headers = {"Accept": "application/json"}
    token = os.environ.get("SONAR_TOKEN")
    if token:
        credentials = base64.b64encode(f"{token}:".encode()).decode()
        headers["Authorization"] = f"Basic {credentials}"
    try:
        with urllib.request.urlopen(
            urllib.request.Request(url, headers=headers), timeout=60
        ) as response:
            return json.load(response)
    except urllib.error.HTTPError as error:
        raise RuntimeError(f"Sonar {endpoint} returned HTTP {error.code}") from None


def collect(endpoint, parameters, key):
    """Retrieve every page and fail rather than silently exporting a partial report."""
    items = []
    page = 1
    while True:
        response = request(endpoint, {**parameters, "p": page, "ps": 500})
        batch = response[key]
        items.extend(batch)
        total = response["paging"]["total"]
        if len(items) >= total:
            if len(items) != total:
                raise RuntimeError(f"Sonar {endpoint} changed during pagination; retry")
            return items
        if not batch or len(items) >= 10000:
            raise RuntimeError(f"Sonar {endpoint} could not export all {total} results")
        page += 1


def analysis(selector):
    """Export open findings, including accepted issues, without a new-code filter."""
    issues = collect(
        "issues/search",
        {"componentKeys": PROJECT, "resolved": "false", **selector},
        "issues",
    )
    hotspots = collect(
        "hotspots/search", {"projectKey": PROJECT, **selector}, "hotspots"
    )
    return {"selector": selector, "issues": issues, "hotspots": hotspots}


def main():
    analyses = {"main": analysis({"branch": "main"})}
    rules = {}
    for result in analyses.values():
        for issue in result["issues"]:
            key = issue["rule"]
            if key not in rules:
                rules[key] = request("rules/show", {"key": key, "organization": "berrycloud"})["rule"]
    report = {
        "project": PROJECT,
        "generatedAt": datetime.datetime.now(datetime.timezone.utc).isoformat(),
        "analyses": analyses,
        "rules": rules,
    }
    with open("sonar-findings.json", "w", encoding="utf-8") as stream:
        json.dump(report, stream, indent=2)
        stream.write("\n")
    summary = ["## Sonar findings", "", f"Project: `{PROJECT}`", ""]
    for name, result in analyses.items():
        summary.append(
            f"- {name}: {len(result['issues'])} unresolved issues, "
            f"{len(result['hotspots'])} security hotspots."
        )
        print(summary[-1])
        # Keep actionable findings readable through GitHub's job-log API as well.
        for issue in result["issues"]:
            print(json.dumps({
                "analysis": name,
                "key": issue["key"],
                "rule": issue["rule"],
                "component": issue["component"],
                "line": issue.get("line"),
                "status": issue["status"],
                "message": issue["message"],
            }))
    summary.extend(["", "Full findings and rule descriptions are in the `sonar-findings` artifact."])
    with open("sonar-summary.md", "w", encoding="utf-8") as stream:
        stream.write("\n".join(summary) + "\n")


if __name__ == "__main__":
    main()
