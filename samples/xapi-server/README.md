# xAPI server

This sample demonstrates how the xAPI model can be used in applications that receive xAPI statements.

The server can be run with the following command:

```bash
mvn spring-boot:run
```

## Features

### Timestamp Converter

The server includes a custom timestamp converter (`InstantConverter`) that handles ISO 8601 formatted timestamps in HTTP request parameters with strict xAPI validation. The converter:

- Accepts timestamps with UTC timezone (e.g., `2017-03-01T12:30:00.000Z`)
- Accepts timestamps with positive timezone offsets (e.g., `2017-03-01T12:30:00.000+00`, `2017-03-01T12:30:00.000+0000`, `2017-03-01T12:30:00.000+00:00`)
- Accepts timestamps with non-zero offsets (e.g., `2017-03-01T12:30:00.000+05:00`, `2017-03-01T12:30:00.000-05:00`)
- Assumes UTC when no timezone is specified (e.g., `2017-03-01T12:30:00.000`)
- Rejects negative zero offsets (e.g., `-00`, `-0000`, `-00:00`) per xAPI specification
- Supports nanosecond precision

The converter is automatically registered with Spring MVC through the `WebConfig` configuration class and is used for all HTTP parameters of type `java.time.Instant`.

## Examples

### Post Statement

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

### Query Statements with Timestamp

Query statements created since a specific timestamp:

```bash
curl --location 'http://localhost:8080/xapi/statements?since=2017-03-01T12:30:00.000Z'
```

Note: The timestamp parameter will be properly parsed and converted, though the endpoint currently returns `501 Not Implemented` as it's a demonstration server.