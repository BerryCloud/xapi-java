# xAPI Java [![CodeQL](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BerryCloud_xapi-java&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BerryCloud_xapi-java&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java)

xAPI Java helps you to create applications that send or receive xAPI [Statements](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statements) or [Documents](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#10-documents).

## xAPI Java Model

The xAPI Model has a [fluent interface](https://en.wikipedia.org/wiki/Fluent_interface). Objects are [immutable](https://en.wikipedia.org/wiki/Immutable_object).

### Getting started

To use the xAPI Model include the appropriate XML in the `dependencies` section of your `pom.xml`, as shown in the following example:

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>getting-started</artifactId>
    <!-- ... -->
    <dependencies>
        <!-- ... -->
        <dependency>
            <groupId>dev.learning.xapi</groupId>
            <artifactId>xapi-model</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</project>
```

### Creating a statement

Example:

```java
final Statement statement = Statement.builder()

    .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .verb(Verb.ATTEMPTED)

    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

    .build();
```

### Deserializing Statements

The Jackson ObjectMapper can be used to deserialize statements into Java objects.

Example:

```java
final String json = """
    {
        "actor":{
            "objectType":"Agent",
            "name":"A N Other",
            "mbox":"mailto:another@example.com"
        },
        "verb":{
            "id":"http://adlnet.gov/expapi/verbs/attempted",
            "display":{
                "und":"attempted"
            }
        },
        "object":{
            "objectType":"Activity",
            "id":"https://example.com/activity/simplestatement",
            "definition":{
                "name":{
                "en":"Simple Statement"
                }
            }
        }
    }""";

final Statement statement = objectMapper.readValue(json, Statement.class);
```

### Serializing Statements

The Jackson ObjectMapper can be used to serialize Statement objects into JSON.

Example:

```java

final Statement statement = Statement.builder()
    .actor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

final String json = objectMapper.writeValueAsString(statement);

```
