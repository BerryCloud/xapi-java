/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * XapiClient Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("XapiClient Tests")
@SpringBootTest
class XapiClientMultipartTests {

  @Autowired
  private WebClient.Builder webClientBuilder;

  private MockWebServer mockWebServer;
  private XapiClient client;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    webClientBuilder.baseUrl(mockWebServer.url("").toString());

    client = new XapiClient(webClientBuilder);

  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenPostingStatementWithAttachmentThenContentTypeHeaderIsMultipartMixed()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Attachment
    client.postStatement(r -> r
        .statement(s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Content Type Header Is Multipart Mixed
    assertThat(recordedRequest.getHeader("content-type"), startsWith("multipart/mixed"));
  }

  @Test
  void whenPostingStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Text Attachment
    client.postStatement(r -> r
        .statement(s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 486\r\n\r\n{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementWithBinaryAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Binary Attachment
    client
        .postStatement(
            r -> r
                .statement(
                    s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

                        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255})
                            .length(6).contentType("application/octet-stream")
                            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                            .addDisplay(Locale.ENGLISH, "binary attachment"))

                        .verb(Verb.ATTEMPTED)

                        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 501\r\n\r\n{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"}]}\r\n"
        + boundary
        + "\r\nContent-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: 0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\r\n\r\n@ABCD�\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementWithoutAttachmentDataThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement Without Attachment Data
    client
        .postStatement(
            r -> r
                .statement(
                    s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

                        .addAttachment(a -> a.length(6).contentType("application/octet-stream")
                            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                            .fileUrl(URI.create("example.com/attachment"))
                            .addDisplay(Locale.ENGLISH, "binary attachment"))

                        .verb(Verb.ATTEMPTED)

                        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"fileUrl\":\"example.com/attachment\"}]}"));
  }

  @Test
  void whenPostingSubStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting SubStatement With Text Attachment
    client.postStatement(r -> r.statement(s -> s
        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ABANDONED)

        .object(SubStatement.builder()

            .actor(Agent.builder().name("A N Other").mbox("mailto:another@example.com").build())

            .verb(Verb.ATTENDED)

            .object(Activity.builder().id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")).build())

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .build())

    )).block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 676\r\n\r\n{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"https://w3id.org/xapi/adl/verbs/abandoned\",\"display\":{\"und\":\"abandoned\"}},\"object\":{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attended\",\"display\":{\"und\":\"attended\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}],\"objectType\":\"SubStatement\"}}\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementsWithAttachmentsThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statements With Attachments
    final var statement1 = Statement.builder()

        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    final var statement2 = Statement.builder()

        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
            .addDisplay(Locale.ENGLISH, "text attachment"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    // When posting Statements
    client.postStatements(r -> r.statements(statement1, statement2)).block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 1211\r\n\r\n[{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"}]},{\"actor\":{\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"},{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}]\r\n"
        + boundary
        + "\r\nContent-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: 0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\r\n\r\n@ABCD�\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));


  }

  @Test
  void whenPostingStatementsWithTimestampAndAttachmentThenNoExceptionIsThrown()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    final var statement = Statement.builder()

        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, 69}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://example.com/attachment"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .timestamp(Instant.now())

        .build();

    // When Posting Statements With Timestamp And Attachment

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> client.postStatements(r -> r.statements(statement)).block());

  }

  @SuppressWarnings("null")
  @Test
  void whenGettingStatementWithAttachmentThenResponseIsExpected() {

    // single statement with two attachments
    final var body =
        """
            ---------314159265358979323846
            Content-Type:application/json

            {"id":"183aabbe-ef9e-49c9-82a3-16ce5135b25b","actor":{"name":"A N Other","mbox":"mailto:another@example.com","objectType":"Agent"},"verb":{"id":"http://adlnet.gov/expapi/verbs/attempted","display":{"und":"attempted"}},"object":{"objectType":"Activity","id":"https://example.com/activity/simplestatement","definition":{"name":{"en":"Simple Statement"}}},"timestamp":"2023-03-29T12:42:27.923571Z","stored":"2023-03-29T12:42:27.923571Z","authority":{"account":{"homePage":"http://localhost","name":"admin"},"objectType":"Agent"},"attachments":[{"usageType":"http://adlnet.gov/expapi/attachments/code","display":{"en":"binary attachment"},"contentType":"application/octet-stream","length":6,"sha2":"0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef"},{"usageType":"http://adlnet.gov/expapi/attachments/text","display":{"en":"text attachment"},"contentType":"text/plain","length":17,"sha2":"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5"}]}
            ---------314159265358979323846
            Content-Type:application/octet-stream
            Content-Transfer-Encoding:binary
            X-Experience-API-Hash:0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef

            @ABCDE
            ---------314159265358979323846
            Content-Type:text/plain
            Content-Transfer-Encoding:binary
            X-Experience-API-Hash:b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5

            Simple attachment
            ---------314159265358979323846--"""
            .replace("\n", "\r\n");

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")

        .setBody(body)

        .addHeader("Content-Type", "multipart/mixed; boundary=-------314159265358979323846"));

    // When Getting Statement With Attachment
    final var response = client
        .getStatement(r -> r.id("183aabbe-ef9e-49c9-82a3-16ce5135b25b").attachments(true)).block();

    // Then Response Is Expected
    assertThat(response.getBody(), instanceOf(Statement.class));
    assertThat(response.getBody().toString(), is(
        "Statement(id=183aabbe-ef9e-49c9-82a3-16ce5135b25b, actor=Agent(super=Actor(name=A N Other, mbox=mailto:another@example.com, mboxSha1sum=null, openid=null, account=null), objectType=AGENT), verb=Verb(id=http://adlnet.gov/expapi/verbs/attempted, display={und=attempted}), object=Activity(objectType=ACTIVITY, id=https://example.com/activity/simplestatement, definition=ActivityDefinition(name={en=Simple Statement}, description=null, type=null, moreInfo=null, interactionType=null, correctResponsesPattern=null, choices=null, scale=null, source=null, target=null, steps=null, extensions=null)), result=null, context=null, timestamp=2023-03-29T12:42:27.923571Z, stored=2023-03-29T12:42:27.923571Z, authority=Agent(super=Actor(name=null, mbox=null, mboxSha1sum=null, openid=null, account=Account(homePage=http://localhost, name=admin)), objectType=AGENT), version=null, attachments=[Attachment(usageType=http://adlnet.gov/expapi/attachments/code, display={en=binary attachment}, description=null, contentType=application/octet-stream, length=6, sha2=0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef, fileUrl=null, content=[64, 65, 66, 67, 68, 69]), Attachment(usageType=http://adlnet.gov/expapi/attachments/text, display={en=text attachment}, description=null, contentType=text/plain, length=17, sha2=b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5, fileUrl=null, content=[83, 105, 109, 112, 108, 101, 32, 97, 116, 116, 97, 99, 104, 109, 101, 110, 116])])"));
  }

  @SuppressWarnings("null")
  @Test
  void whenGettingStatementsWithAttachmentsThenResponseIsExpected() {

    // two statements with overlapping attachments
    final var body =
        """
            ---------314159265358979323846
            Content-Type:application/json

            {"statements":[{"id":"183aabbe-ef9e-49c9-82a3-16ce5135b25b","actor":{"name":"A N Other","mbox":"mailto:another@example.com","objectType":"Agent"},"verb":{"id":"http://adlnet.gov/expapi/verbs/attempted","display":{"und":"attempted"}},"object":{"objectType":"Activity","id":"https://example.com/activity/simplestatement","definition":{"name":{"en":"Simple Statement"}}},"timestamp":"2023-03-29T12:42:27.923571Z","stored":"2023-03-29T12:42:27.923571Z","authority":{"account":{"homePage":"http://localhost","name":"admin"},"objectType":"Agent"},"attachments":[{"usageType":"http://adlnet.gov/expapi/attachments/code","display":{"en":"binary attachment"},"contentType":"application/octet-stream","length":6,"sha2":"0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef"},{"usageType":"http://adlnet.gov/expapi/attachments/text","display":{"en":"text attachment"},"contentType":"text/plain","length":17,"sha2":"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5"}]},{"id":"bbd3babf-61bf-4038-81fe-8342a4cea9bf","actor":{"name":"A N Other","mbox":"mailto:another@example.com","objectType":"Agent"},"verb":{"id":"http://adlnet.gov/expapi/verbs/attempted","display":{"und":"attempted"}},"object":{"objectType":"Activity","id":"https://example.com/activity/simplestatement","definition":{"name":{"en":"Simple Statement"}}},"timestamp":"2023-03-29T12:42:27.923571Z","stored":"2023-03-29T12:42:27.923571Z","authority":{"account":{"homePage":"http://localhost","name":"admin"},"objectType":"Agent"},"attachments":[{"usageType":"http://adlnet.gov/expapi/attachments/code","display":{"en":"binary attachment"},"contentType":"application/octet-stream","length":6,"sha2":"0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef"}]}]}
            ---------314159265358979323846
            Content-Type:application/octet-stream
            Content-Transfer-Encoding:binary
            X-Experience-API-Hash:0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef

            @ABCDE
            ---------314159265358979323846
            Content-Type:text/plain
            Content-Transfer-Encoding:binary
            X-Experience-API-Hash:b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5

            Simple attachment
            ---------314159265358979323846--"""
            .replace("\n", "\r\n");
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")

        .setBody(body)

        .addHeader("Content-Type", "multipart/mixed; boundary=-------314159265358979323846"));

    // When Getting Statements With Attachment
    final var response = client.getStatements(r -> r.attachments(true)).block();

    // Then Response Is Expected
    assertThat(response.getBody(), instanceOf(StatementResult.class));
    assertThat(response.getBody().toString(), is(
        "StatementResult(statements=[Statement(id=183aabbe-ef9e-49c9-82a3-16ce5135b25b, actor=Agent(super=Actor(name=A N Other, mbox=mailto:another@example.com, mboxSha1sum=null, openid=null, account=null), objectType=AGENT), verb=Verb(id=http://adlnet.gov/expapi/verbs/attempted, display={und=attempted}), object=Activity(objectType=ACTIVITY, id=https://example.com/activity/simplestatement, definition=ActivityDefinition(name={en=Simple Statement}, description=null, type=null, moreInfo=null, interactionType=null, correctResponsesPattern=null, choices=null, scale=null, source=null, target=null, steps=null, extensions=null)), result=null, context=null, timestamp=2023-03-29T12:42:27.923571Z, stored=2023-03-29T12:42:27.923571Z, authority=Agent(super=Actor(name=null, mbox=null, mboxSha1sum=null, openid=null, account=Account(homePage=http://localhost, name=admin)), objectType=AGENT), version=null, attachments=[Attachment(usageType=http://adlnet.gov/expapi/attachments/code, display={en=binary attachment}, description=null, contentType=application/octet-stream, length=6, sha2=0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef, fileUrl=null, content=[64, 65, 66, 67, 68, 69]), Attachment(usageType=http://adlnet.gov/expapi/attachments/text, display={en=text attachment}, description=null, contentType=text/plain, length=17, sha2=b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5, fileUrl=null, content=[83, 105, 109, 112, 108, 101, 32, 97, 116, 116, 97, 99, 104, 109, 101, 110, 116])]), Statement(id=bbd3babf-61bf-4038-81fe-8342a4cea9bf, actor=Agent(super=Actor(name=A N Other, mbox=mailto:another@example.com, mboxSha1sum=null, openid=null, account=null), objectType=AGENT), verb=Verb(id=http://adlnet.gov/expapi/verbs/attempted, display={und=attempted}), object=Activity(objectType=ACTIVITY, id=https://example.com/activity/simplestatement, definition=ActivityDefinition(name={en=Simple Statement}, description=null, type=null, moreInfo=null, interactionType=null, correctResponsesPattern=null, choices=null, scale=null, source=null, target=null, steps=null, extensions=null)), result=null, context=null, timestamp=2023-03-29T12:42:27.923571Z, stored=2023-03-29T12:42:27.923571Z, authority=Agent(super=Actor(name=null, mbox=null, mboxSha1sum=null, openid=null, account=Account(homePage=http://localhost, name=admin)), objectType=AGENT), version=null, attachments=[Attachment(usageType=http://adlnet.gov/expapi/attachments/code, display={en=binary attachment}, description=null, contentType=application/octet-stream, length=6, sha2=0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef, fileUrl=null, content=[64, 65, 66, 67, 68, 69])])], more=null)"));

  }

}
