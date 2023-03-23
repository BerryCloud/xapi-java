/*
 * Copyright 2016rue-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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

  @Autowired
  private ObjectMapper objectMapper;

  private MockWebServer mockWebServer;
  private XapiClient client;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    webClientBuilder.baseUrl(mockWebServer.url("").toString());

    client = new XapiClient(webClientBuilder, objectMapper);

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
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Content Type Header Is Multipart Mixed
    assertThat(recordedRequest.getHeader("content-type"), startsWith("multipart/mixed"));
  }

  @Test
  void whenPostingStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Text Attachment
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "--xapi-learning-dev-boundary\r\nContent-Type:application/json\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}\r\n--xapi-learning-dev-boundary\r\nContent-Type:text/plain\r\nContent-Transfer-Encoding:binary\r\nX-Experience-API-Hash:b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n--xapi-learning-dev-boundary--"));
  }

  @Test
  void whenPostingStatementWithBinaryAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Binary Attachment
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content(new byte[] { 64, 65, 66, 67, 68, (byte) 255 }).length(6)
                .contentType("application/octet-stream")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                .addDisplay(Locale.ENGLISH, "binary attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "--xapi-learning-dev-boundary\r\nContent-Type:application/json\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"}]}\r\n--xapi-learning-dev-boundary\r\nContent-Type:application/octet-stream\r\nContent-Transfer-Encoding:binary\r\nX-Experience-API-Hash:0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\r\n\r\n@ABCD�\r\n--xapi-learning-dev-boundary--"));
  }

  @Test
  void whenPostingStatementWithoutAttachmentDataThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement Without Attachment Data
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.length(6).contentType("application/octet-stream")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                .fileUrl(URI.create("example.com/attachment"))
                .addDisplay(Locale.ENGLISH, "binary attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"fileUrl\":\"example.com/attachment\"}]}"));
  }

  @Test
  void whenPostingSubStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting SubStatement With Text Attachment
    client.postStatement(r -> r.statement(s -> s
        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

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

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "--xapi-learning-dev-boundary\r\nContent-Type:application/json\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"https://w3id.org/xapi/adl/verbs/abandoned\",\"display\":{\"und\":\"abandoned\"}},\"object\":{\"objectType\":\"SubStatement\",\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attended\",\"display\":{\"und\":\"attended\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}}\r\n--xapi-learning-dev-boundary\r\nContent-Type:text/plain\r\nContent-Transfer-Encoding:binary\r\nX-Experience-API-Hash:b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n--xapi-learning-dev-boundary--"));
  }

  @Test
  void whenPostingStatementsWithAttachmentsThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statements With Attachments
    final Statement statement1 = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] { 64, 65, 66, 67, 68, 69 }).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    final Statement statement2 = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] { 64, 65, 66, 67, 68, 69 }).length(6)
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

    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "--xapi-learning-dev-boundary\r\nContent-Type:application/json\r\n\r\n[{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef\"}]},{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef\"},{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}]\r\n--xapi-learning-dev-boundary\r\nContent-Type:text/plain\r\nContent-Transfer-Encoding:binary\r\nX-Experience-API-Hash:b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n--xapi-learning-dev-boundary\r\nContent-Type:application/octet-stream\r\nContent-Transfer-Encoding:binary\r\nX-Experience-API-Hash:0ff3c6749b3eeaae17254fdf0e2de1f32b21c592f474bf39b62b398e8a787eef\r\n\r\n@ABCDE\r\n--xapi-learning-dev-boundary--"));
  }

}
