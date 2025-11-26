# xAPI server

This sample demonstrates how the xAPI model can be used in applications that receive xAPI statements and state documents.

The server can be run with the following command:

```bash
mvn spring-boot:run
```

## Testing the Statement Resource

You can test the statement resource with the following command:

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

## Testing the State Resource

### PUT State (create or replace a state document)

```bash
curl --location --request PUT 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D&stateId=bookmark' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message": "Hello World!",
    "timestamp": "2024-01-01T12:00:00Z"
}'
```

### POST State (merge with existing state document)

```bash
curl --location 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D&stateId=bookmark' \
--header 'Content-Type: application/json' \
--data-raw '{
    "additionalProperty": "New value"
}'
```

### GET State (retrieve a single state document)

```bash
curl --location 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D&stateId=bookmark'
```

### GET States (retrieve all state IDs)

```bash
curl --location 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D'
```

### DELETE State (delete a single state document)

```bash
curl --location --request DELETE 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D&stateId=bookmark'
```

### DELETE States (delete all state documents for an activity and agent)

```bash
curl --location --request DELETE 'http://localhost:8080/xapi/activities/state?activityId=https://example.com/activity/1&agent=%7B%22objectType%22:%22Agent%22,%22mbox%22:%22mailto:another@example.com%22%7D'
```