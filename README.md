# xAPI Java [![CodeQL](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BerryCloud_xapi-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java)

xAPI Java helps you to create applications that send or receive xAPI [Statements](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statements) or [Documents](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#10-documents).

There are two projects in this [Monorepo](https://en.wikipedia.org/wiki/Monorepo), xAPI Client and xAPI Model.

Both the xAPI Client and xAPI Model use a [fluent interface](https://en.wikipedia.org/wiki/Fluent_interface). Objects are [immutable](https://en.wikipedia.org/wiki/Immutable_object).

## Requirements

xAPI Java requires **Java 25 or newer**. See [CONTRIBUTING.md](CONTRIBUTING.md#prerequisites) for detailed installation instructions.

### Version Compatibility

- **Version 2.x** - Requires Java 25 or newer
- **Version 1.x** - Requires Java 17 or newer

If you need to use Java 17, please use version 1.x of xAPI Java.

## xAPI Java Client

The xAPI Java Client can be used by learning record providers (LRP) to communicate with learning record stores (LRS) or a system which follows the LRS requirements of one or more of the xAPI resources.

### Getting started

To use the xAPI Java Client include the appropriate XML in the `dependencies` section of your `pom.xml`, as shown in the following example:

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>getting-started</artifactId>
    <!-- ... -->
    <dependencies>
        <!-- ... -->
        <dependency>
            <groupId>dev.learning.xapi</groupId>
            <artifactId>xapi-client</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>
</project>
```

xAPI Java Client is available in the [Maven Central Repository](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-client/).

### Configuration

The xAPI Java Client has a Spring AutoConfiguration bean which picks up the following properties:

| Property                  | Description                                                                     |
| ------------------------- | ------------------------------------------------------------------------------- |
| xapi.client.baseUrl       | The base url of the LRS endpoint                                                |
| xapi.client.username      | Username for basic authorization header                                         |
| xapi.client.password      | Password for basic authorization header                                         |
| xapi.client.authorization | Authorization header (has precedence over the username and password properties) |

Properties can be set using any [external configuration](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#features.external-config.files) method supported by Spring Boot.

If you need more specific customization (eg. your LRS needs specific headers, or you want to set the authorization header dynamically) you can create a custom configurer by implementing the `XapiClientConfigurer` interface.

### Advanced Configuration

The xAPI Java Client uses the Spring WebClient. Spring WebClient has default memory limit of 256KB for buffering data. If this limit is exceeded then a DataBufferLimitException will be thrown.

The default memory limit of 256KB for buffering data could be exceeded if the LRS returns a large number of Statements or if the Statements contain attachments.

It is possible to set the memory limit for buffering data with the `spring.codec.max-in-memory-size` property.

Example:

```
spring.codec.max-in-memory-size=1MB
```

### Statement Resource

The xAPI Java Client allows applications to store and fetch xAPI [Statements](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statements).

### Getting a Statement

Example:

```java
var response = client.getStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6")).block();

Statement statement = response.getBody();
```

### Getting a Statement with attachments

Example:

```java
var response = client.getStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6").attachments(true).block();

Statement statement = response.getBody();
```

### Getting Statements

Example:

```java
var response = client.getStatements().block();

StatementResult statementResult = response.getBody();

Statement[] statements = statementResult.getStatements();
```

### Getting the next page of Statements

Example:

```java
var response = client.getStatements().block();

var moreResponse = client.getMoreStatements(r -> r.more(response.getBody().getMore())).block();

StatementResult moreStatementResult = moreResponse.getBody();

Statement[] statements = moreStatementResult.getStatements();
```

### Getting Statements as Iterator (and processing them as a Stream)

In most cases it is preferable to use `getStatementIterator()` instead of `getStatments()` and `getMoreStatements()`.

Example:

```java
var statements = client.getStatementIterator().block();

// process the first 100 Statements
statements.toStream().limit(100).forEach(s -> {
    // add logic here...
  });
```

### Posting a Statement

Example:

```java
client.postStatement(
    r -> r.statement(s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
    .block();
```

### Posting a Statement with an attachment

Example:

```java
client.postStatement(
    r -> r.statement(s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
            .usageType(URI.create("https://example.com/attachments/simplestatement"))
            .addDisplay(Locale.ENGLISH, "text attachment"))

    )).block();
```

### Posting a Signed Statement

Example:

```java
client.postStatement(
    r -> r.signedStatement(s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement"))),

        keyPair.getPrivate()))
    .block();
```

### Posting Statements

Example:

```java
Statement attemptedStatement = Statement.builder()
    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

Statement passedStatement = attemptedStatement.toBuilder().verb(Verb.PASSED).build();

client.postStatements(r -> r.statements(attemptedStatement, passedStatement)).block();
```

### Getting a voided Statement

Example:

```java
var response = client.getVoidedStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6")).block();

Statement voidedStatement = response.getBody();
```

### State Resource

The xAPI Java Client allows applications to store, change, fetch, or delete [state documents](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#23-state-resource).

#### Getting a state

Example:

```java
var response = client.getState(r -> r.activityId("https://example.com/activity/1")

    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    .stateId("bookmark"), String.class)

    .block();

String state = response.getBody();
```

#### Posting a state

Example:

```java
client.postState(r -> r.activityId("https://example.com/activity/1")

    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    .stateId("bookmark")

    .state("Hello World!"))

    .block();
```

#### Putting a state

Example:

```java
client.putState(r -> r.activityId("https://example.com/activity/1")

    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    .stateId("bookmark")

    .state("Hello World!"))

    .block();
```

#### Deleting a state

Example:

```java
client.deleteState(r -> r.activityId("https://example.com/activity/1")

    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    .stateId("bookmark"))

    .block();
```

### Samples

The samples folder in this repository contains [sample applications](samples) that use the xAPI client.

## xAPI Model Spring Boot Starter

The xAPI specification has strict rules for API requests/responses formatting. The xAPI Model has inbuilt validation for all of these rules. However, if you plan to use the xAPI Model, you should keep in mind that some activity providers do not fully conform to these rules.

In some cases it may be desirable to turn off some or all of the rules in order to be compatible with a wider range of xAPI activity providers. However, it should be noted that doing this is in violation of the xAPI specification.

The xAPI Model Spring Boot Starter package provides an easy way to turn on/off these validation rules.

### Getting started

To use the xAPI Model Spring Boot Starter include the appropriate XML in the `dependencies` section of your `pom.xml`, as shown in the following example:

```xml
<dependency>
  <groupId>dev.learning.xapi</groupId>
  <artifactId>xapi-model-spring-boot-starter</artifactId>
  <version>2.0.0</version>
</dependency>
```

xAPI Model Spring Boot Starter is available in the [Maven Central Repository](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model-spring-boot-starter/).

### Configuration

The xAPI Model Spring Boot Starter has a Spring AutoConfiguration bean which picks up the following properties:

| Property                                 | Description                                                                |
| ---------------------------------------- | -------------------------------------------------------------------------- |
| xapi.model.validateJson                  | Fail on trailing JSON tokens                                               |
| xapi.model.validateProperties            | Fail on unknown JSON properties                                            |
| xapi.model.validateNullValues            | Fail on null JSON properties                                               |
| xapi.model.validateLiterals              | Fail on number and boolean JSON properties defined as string               |
| xapi.model.validateObjectType            | Fail on invalid JSON objectType property                                   |
| xapi.model.validateLocale                | Fail on invalid Locale strings                                             |
| xapi.model.validateTimestamp             | Fail on negative zero timezone offsets                                     |
| xapi.model.validateActivityDefinition    | Fail on invalid xAPI ActivityDefinition (missing properties)               |
| xapi.model.validateActor                 | Fail on invalid xAPI Actor (missing or multiple identifiers)               |
| xapi.model.validateAuthority             | Fail on invalid xAPI Authority object                                      |
| xapi.model.validateUriScheme             | Fail on invalid xAPI URI property (missing scheme)                         |
| xapi.model.validateMbox                  | Fail on invalid xAPI mbox property (invalid email or missing prefix)       |
| xapi.model.validateLocaleNotUndetermined | Fail on invalid xAPI locale property (locale is undetermined)              |
| xapi.model.validateScaledScore           | Fail on invalid xAPI scaledScore property (out of -1 - 1 range)            |
| xapi.model.validateScore                 | Fail on invalid xAPI Score (raw score is out of min/max range)             |
| xapi.model.validateStatementPlatform     | Fail on invalid xAPI context.platform (if present object must be Activity) |
| xapi.model.validateStatementRevision     | Fail on invalid xAPI context.revision (if present object must be Activity) |
| xapi.model.validateStatementListIds      | Fail on invalid xAPI statement List (conflicting statement ids)            |
| xapi.model.validateStatementVerb         | Fail on invalid xAPI voided statement (object must be StatemnetReference)  |
| xapi.model.validateUuidVariant           | Fail on invalid xAPI UUID property (must be UUID variant 4)                |

The default value is **TRUE** for all of the above properties.

## xAPI Java Model

The xAPI model can be used by clients that send xAPI data or by servers that receive xAPI data.

**The xAPI Model JAR is approximately 120 KB.** This makes it suitable for simple projects that only require basic serialisation and deserialisation.

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
            <version>2.0.0</version>
        </dependency>
    </dependencies>
</project>
```

xAPI Model is available in the [Maven Central Repository](https://central.sonatype.com/artifact/dev.learning.xapi/xapi-model/).

### Creating a statement

Example:

```java
Statement statement = Statement.builder()

    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .verb(Verb.ATTEMPTED)

    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

    .build();
```

### Deserializing Statements

The Jackson ObjectMapper can be used to deserialize statements into Java objects.

Example:

```java
String json = """
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

Statement statement = objectMapper.readValue(json, Statement.class);
```

### Serializing Statements

The Jackson ObjectMapper can be used to serialize Statement objects into JSON.

Example:

```java

Statement statement = Statement.builder()
    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

String json = objectMapper.writeValueAsString(statement);

```

### Creating a new statement using an existing statement as template

Example:

```java

Statement passed = Statement.builder()
    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.PASSED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

Statement completed = passed.toBuilder().verb(Verb.COMPLETED).build();

```

### Validating Statements

Statements can be validated programmatically.

Example:

```java
Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

Statement statement = Statement.builder()

    .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

    .activityObject(o -> o.id("https://example.com/xapi/activity/simplestatement"))

    .build();

Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

System.out.println(constraintViolations)

// Prints [ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=verb, rootBeanClass=class dev.learning.xapi.model.Statement, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}]

```

Statements can also be validated when they are received by a method in a REST controller. The following example requires Spring MVC and the Hibernate Validator.

```java
@PostMapping
public ResponseEntity<Collection<UUID>> postStatements(
    @RequestBody List<@Valid Statement> statements) {

  // Process the statements

  return new ResponseEntity<>(HttpStatus.OK);
}

```

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Development environment setup
- Code style guidelines
- Building and testing procedures
- Pull request process

For information about creating releases, see [RELEASING.md](RELEASING.md) (maintainers only).
