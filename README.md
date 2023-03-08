# xAPI Java [![CodeQL](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/BerryCloud/xapi-java/actions/workflows/codeql.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BerryCloud_xapi-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BerryCloud_xapi-java)

xAPI Java helps you to create applications that send or receive xAPI [Statements](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statements) or [Documents](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#10-documents).

There are two projects in this [Monorepo](https://en.wikipedia.org/wiki/Monorepo), xAPI Client and xAPI Model. 

Both the xAPI Client and xAPI Model use a [fluent interface](https://en.wikipedia.org/wiki/Fluent_interface). Objects are [immutable](https://en.wikipedia.org/wiki/Immutable_object). 

[CheckStyle](https://checkstyle.sourceforge.io) is used to enforce the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Sonar performs automatic pull request reviews. [CodeQL](https://codeql.github.com) scans for vulnerabilities. The number of bugs, code smells and vulnerabilities in the codebase can be viewed in SonarCloud. The code coverage and code duplication percentages can also be viewed in SonarCloud. Over three-hundred unit tests ensure conformance with the xAPI specification.

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
            <version>1.0.7</version>
        </dependency>
    </dependencies>
</project>
```

### Statement Resource

The xAPI Java Client allows applications to store and fetch xAPI [Statements](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statements).

### Getting a Statement

Example:

```java
var response = client.getStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6")).block();
    
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


### Posting a Statement

Example:

```java
client.postStatement(
    r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
    .block();
```

### Posting Statements

Example:

```java
Statement attemptedStatement = Statement.builder()
    .actor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
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

## xAPI Java Model

The xAPI model can be used by clients that send xAPI data or by servers that receive xAPI data.

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
            <version>1.0.7</version>
        </dependency>
    </dependencies>
</project>
```

### Creating a statement

Example:

```java
Statement statement = Statement.builder()

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
    .actor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

String json = objectMapper.writeValueAsString(statement);

```

### Creating a new statement using an existing statement as template

Example:

```java

Statement passed = Statement.builder()
    .actor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.PASSED)
    .activityObject(o -> o.id("https://example.com/activity/simplestatement")
        .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
    .build();

Statement completed = passed.toBuilder().verb(Verb.COMPLETED).build();

```

## Requirements

xAPI Java requires Java 17 or newer.
