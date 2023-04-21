/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
 * XapiEnhancedClient Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("XapiEnhancedClient Tests")
@SpringBootTest
class XapiEnhancedClientTests {

  @Autowired
  private WebClient.Builder webClientBuilder;

  private MockWebServer mockWebServer;
  private XapiEnhancedClient client;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    webClientBuilder.baseUrl(mockWebServer.url("").toString());

    client = new XapiEnhancedClient(webClientBuilder);

  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenGettingStatementIteratorViaMultipeResponsesThenResultIsExpected()
      throws InterruptedException {
    final var body1 = """
        {
          "statements" : [
            {
              "id" : "c0aaea0b-252b-4d9d-b7ad-46c541572570"
            }
          ],
          "more" : "/statements/more/1"
        }
        """;
    final var body2 = """
        {
          "statements" : [
            {
              "id" : "4ed0209a-f50f-4f57-8602-ba5f981d211a"
            }
          ],
          "more" : ""
        }
        """;

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body1)
        .addHeader("Content-Type", "application/json; charset=utf-8"));
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body2)
        .addHeader("Content-Type", "application/json; charset=utf-8"));

    // When Getting StatementIterator Via Multipe Responses
    final var iterator = client.getStatementIterator().block();

    // Then Result Is Expected
    assertThat(iterator.next().getId(),
        is(UUID.fromString("c0aaea0b-252b-4d9d-b7ad-46c541572570")));
    assertThat(iterator.next().getId(),
        is(UUID.fromString("4ed0209a-f50f-4f57-8602-ba5f981d211a")));
    assertThat(iterator.hasNext(), is(false));

  }

  @Test
  void whenGettingStatementIteratorViaMultipeResponsesThenRequestsAreExpected()
      throws InterruptedException {
    final var body1 = """
        {
          "statements" : [
            {
              "id" : "c0aaea0b-252b-4d9d-b7ad-46c541572570"
            }
          ],
          "more" : "/statements/more/1"
        }
        """;
    final var body2 = """
        {
          "statements" : [
            {
              "id" : "4ed0209a-f50f-4f57-8602-ba5f981d211a"
            }
          ],
          "more" : ""
        }
        """;

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body1)
        .addHeader("Content-Type", "application/json; charset=utf-8"));
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body2)
        .addHeader("Content-Type", "application/json; charset=utf-8"));

    // When Getting StatementIterator Via Multipe Responses
    final var iterator = client.getStatementIterator().block();
    iterator.next();
    iterator.next();

    // Then Requests Are Expected
    assertThat(mockWebServer.takeRequest().getPath(), is("/statements"));
    assertThat(mockWebServer.takeRequest().getPath(), is("/statements/more/1"));

  }

  @Test
  void whenGettingStatementIteratorThenRequestsAreExpected() throws InterruptedException {
    final var body1 = """
        {
          "statements" : [
            {
              "id" : "c0aaea0b-252b-4d9d-b7ad-46c541572570"
            }
          ],
          "more" : "/statements/more/1"
        }
        """;
    final var body2 = """
        {
          "statements" : [
            {
              "id" : "4ed0209a-f50f-4f57-8602-ba5f981d211a"
            }
          ],
          "more" : ""
        }
        """;

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body1)
        .addHeader("Content-Type", "application/json; charset=utf-8"));
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body2)
        .addHeader("Content-Type", "application/json; charset=utf-8"));

    // When Getting StatementIterator
    client.getStatementIterator().block();

    // Then Requests Are Expected
    assertThat(mockWebServer.takeRequest().getPath(), is("/statements"));
    assertThat(mockWebServer.takeRequest(1, TimeUnit.SECONDS), is(nullValue()));

  }

  @Test
  void givenEmptyResponseWhenGettingStatementIteratorThenHasNextIsFalse()
      throws InterruptedException {

    // Given Empty Response
    final var body = """
        {
          "statements" : [
          ],
          "more" : ""
        }
        """;

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK").setBody(body)
        .addHeader("Content-Type", "application/json; charset=utf-8"));

    // When Getting StatementIterator
    final var iterator = client.getStatementIterator().block();

    // Then HasNext Is False
    assertThat(iterator.hasNext(), is(false));

  }

  @Test
  void whenVoidingStatementThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"2eb84e56-441a-492c-9d7b-f8e9ddd3e15d\"]")
        .addHeader("Content-Type", "application/json"));

    final var attemptedStatement = Statement.builder()

        .id(UUID.fromString("175c9264-692f-4108-9b7d-0ba64bd59ac3"))

        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    // When Voiding Statement
    client.voidStatement(attemptedStatement).block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/voided\",\"display\":{\"und\":\"voided\"}},\"object\":{\"objectType\":\"StatementRef\",\"id\":\"175c9264-692f-4108-9b7d-0ba64bd59ac3\"}}"));
  }
}
