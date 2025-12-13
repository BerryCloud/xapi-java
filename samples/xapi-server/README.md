# xAPI server

This sample demonstrates how the xAPI model can be used in applications that receive xAPI statements.

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