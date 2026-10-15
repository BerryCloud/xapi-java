# xAPI server

This sample demonstrates how the xAPI model can be used in applications that receive xAPI statements and store activity state documents. It is a teaching example implementing a subset of xAPI, rather than a production LRS.

## Lombok Usage

This sample uses [Project Lombok](https://projectlombok.org/) to reduce boilerplate code in the `StatementEntity` class. Lombok annotations such as `@Data`, `@NoArgsConstructor`, and `@AllArgsConstructor` automatically generate getters, setters, constructors, and other common methods at compile time. This demonstrates best practices for using Lombok in xAPI-based applications and makes it easier for contributors to get started.

If you're using an IDE, you may need to install the Lombok plugin and enable annotation processing for proper IDE support.

## Running the Server

The server can be run with the following command:

```bash
mvn spring-boot:run
```

You can test the server with the following command:

```bash
curl --location 'http://localhost:8080/xapi/statements' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "da415905-dcd8-4421-b4ba-bc885dfbf28c",
    "actor": {
        "objectType": "Agent",
        "name": "A N Other",
        "mbox": "mailto:another@example.com"
    },
    "object": {
        "objectType": "Activity",
        "id": "https://example.com/activity/simplestatement",
        "definition": {
            "name": {
                "en": "Simple Statement"
            }
        }
    }
}'
```

## JSON persistence

The sample uses Spring Boot's auto-configured Jackson 3 mapper for HTTP and the
xAPI validation modules. Statement documents are stored as Jackson 3 `JsonNode`
values through Hypersistence Utils `hypersistence-utils-hibernate-73:3.16.0`,
matching Boot's Hibernate 7.4 line. No separate Jackson 2 mapper is required.

## Conformance progress

The **Example LRS conformance** GitHub Actions workflow runs the
[ADL LRS conformance test suite](https://github.com/adlnet/lrs-conformance-test-suite)
against this sample using xAPI **1.0.3**, matching the project's client. It runs
for relevant pull requests and pushes to `main`, and can also be started manually
from the Actions tab once the workflow is on `main`.

Each run starts a new server with an in-memory H2 database and strict validation.
The workflow summary shows **Passed: X · Failed: Y**. Conformance failures are
informational while this example works towards compliance. Build, startup,
timeout and runner/report errors still fail the workflow. Tests that are pending
or blocked by failed setup hooks are reported separately as **Not run**, so they
are not mistaken for passes. No tests are filtered out and bail is disabled.

Download the `lrs-conformance-results` artefact for the detailed ADL JSON result
log, runner output and server log. Results are retained for 14 days. The workflow
pins the upstream suite to a commit; update that reference deliberately when
changing the baseline. Upstream does not provide a dependency lockfile, so its npm
dependency resolution can still change between runs.

## Activity state

The State Resource is available at `/xapi/activities/state`:

| Method | With `stateId` | Without `stateId` |
| --- | --- | --- |
| PUT | Create or replace a document | Not supported |
| POST | Create or merge a JSON object | Not supported |
| GET | Retrieve a document | List state ids, optionally filtered by `since` |
| DELETE | Delete a document | Delete all documents in the context |

Every request requires `activityId` (an absolute IRI) and `agent` (a JSON Agent with one identifier). `registration` is an optional UUID. Agent names, JSON property order and the optional `objectType` do not affect identity.

Single-document operations address the activity, agent, optional registration and state id. An omitted or empty `registration` defaults to the nil UUID (`00000000-0000-0000-0000-000000000000`), which is reserved for the unregistered context. Explicit nil and omission address the same context for every operation, including listing and bulk deletion; registered documents are unaffected. This follows the interpretation discussed in [xAPI-Spec issue #1086](https://github.com/adlnet/xAPI-Spec/issues/1086#issuecomment-471997823). The `since` filter returns ids modified strictly after the supplied timestamp.

PUT accepts arbitrary bytes and preserves the Content-Type, defaulting to `application/octet-stream` if it is omitted. POST requires JSON objects with Content-Type `application/json` and merges top-level properties, replacing nested objects as whole values. Invalid merges return 400 without changing the existing document.

For example, store a document and retrieve it:

```bash
curl --request PUT 'http://localhost:8080/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity&agent=%7B%22mbox%22%3A%22mailto%3Alearner%40example.com%22%7D&stateId=progress' \
  --header 'Content-Type: application/json' \
  --data-raw '{"page":1}'

curl --get 'http://localhost:8080/xapi/activities/state' \
  --data-urlencode 'activityId=https://example.com/activity' \
  --data-urlencode 'agent={"mbox":"mailto:learner@example.com"}' \
  --data-urlencode 'stateId=progress'
```

GET responses include a quoted SHA-1 ETag and Last-Modified metadata when documents exist. PUT, POST and single-document DELETE accept optional `If-Match` and `If-None-Match` headers, returning 412 when the precondition fails. State writes do not require these headers. JPA optimistic locking prevents concurrent updates from silently overwriting one another; clients should retrieve the current document and retry after a conflict.

Documents use the same H2/JPA storage as the statement sample. The default embedded H2 database is transient, so documents are lost when the application stops. Authentication, cross-instance creation conflicts and full xAPI conformance remain outside the scope of this example.

State documents use a composite primary key of activity/agent scope, registration UUID and state id. All key columns are non-null. State ids use H2's unbounded `VARCHAR` rather than a LOB so they can participate in the key without imposing a 255-character limit. Other database engines may require a different mapping for long indexed identifiers.
