/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * XapiClient Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("XapiClient Tests")
@SpringBootTest
class XapiClientTests {

  @Autowired
  private WebClient.Builder webClientBuilder;

  private MockWebServer mockWebServer;
  private XapiClient client;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

    webClientBuilder.baseUrl(baseUrl);
    client = new XapiClient(webClientBuilder);

  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenGettingASingleStateThenMethodIsGet() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 No Content"));

    // When Getting A Single State
    client.getState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark"), String.class).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Method Is Get
    assertThat(recordedRequest.getMethod(), is("GET"));
  }

  @Test
  void whenGettingASingleStateThenPathIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 No Content"));

    // When Getting A Single State
    client.getState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark"), String.class).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Path Is Expected
    assertThat(recordedRequest.getPath(), is(
        "/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22objectType%22%3A%22Agent%22%2C%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark"));
  }



  @Test
  void test() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 No Content")
        .setBody("Hello World!").addHeader("Content-Type", "text/plain; charset=utf-8"));

    // When Getting A Single State
    ResponseEntity<String> response = client
        .getState(r -> r.activityId("https://example.com/activity/1")

            .agent(a -> a.name("A N Other").mbox("another@example.com"))

            .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

            .stateId("bookmark"), String.class)

        .block();

    // Then Path Is Expected
    assertThat(response.getBody(), instanceOf(String.class));
  }

  @Test
  void whenDeletingASingleStateThenMethodIsDelete() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting A Single State
    client.deleteState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Method Is Delete
    assertThat(recordedRequest.getMethod(), is("DELETE"));
  }

  @Test
  void whenDeletingASingleStateThenPathIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting A Single State
    client.deleteState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Path Is Expected
    assertThat(recordedRequest.getPath(), is(
        "/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22objectType%22%3A%22Agent%22%2C%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark"));
  }

  @Test
  void whenDeletingASingleStateWithoutRegistrationThenMethodIsDelete() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting A Single State Without Registration
    client.deleteState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .stateId("bookmark")).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Method Is Delete
    assertThat(recordedRequest.getMethod(), is("DELETE"));
  }

  @Test
  void whenDeletingASingleStateWithoutRegistrationThenPathIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting A Single State Without Registration
    client.deleteState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Path Is Expected
    assertThat(recordedRequest.getPath(), is(
        "/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22objectType%22%3A%22Agent%22%2C%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark"));
  }

  @Test
  void whenDeletingMultipleStatesThenMethodIsDelete() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting Multiple States
    client.deleteStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    ).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Method Is Delete
    assertThat(recordedRequest.getMethod(), is("DELETE"));
  }

  @Test
  void whenDeletingMultipleStatesThenPathIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting Multiple States
    client.deleteStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    ).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Path Is Expected
    assertThat(recordedRequest.getPath(), is(
        "/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22objectType%22%3A%22Agent%22%2C%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6"));
  }

  @Test
  void whenDeletingMultipleStatesWithoutRegistrationThenMethodIsDelete()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting Multiple States Without Registration
    client.deleteStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

    ).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Method Is Delete
    assertThat(recordedRequest.getMethod(), is("DELETE"));
  }

  @Test
  void whenDeletingMultipleStatesWithoutRegistrationThenPathIsExpected()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));

    // When Deleting Multiple States Without Registration
    client.deleteStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

    ).block();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Path Is Expected
    assertThat(recordedRequest.getPath(), is(
        "/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22objectType%22%3A%22Agent%22%2C%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D"));
  }


  // Test utility
  private MultiValueMap<String, String> getQueryParams(RecordedRequest recordedRequest) {

    UriComponents uriComponents =
        UriComponentsBuilder.fromHttpUrl(recordedRequest.getRequestUrl().toString()).build();

    return uriComponents.getQueryParams();

  }

}


