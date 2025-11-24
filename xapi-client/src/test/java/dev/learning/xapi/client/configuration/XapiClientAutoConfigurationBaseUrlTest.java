/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;

import dev.learning.xapi.client.XapiClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * XapiClientAutoConfigurationBaseUrl Test.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("XapiClientAutoConfigurationBaseUrl Test")
@SpringBootTest(
    classes = {
      XapiClientAutoConfiguration.class,
      WebClientAutoConfiguration.class,
      JacksonAutoConfiguration.class
    },
    properties = {"xapi.client.baseUrl = http://127.0.0.1:55123/"})
class XapiClientAutoConfigurationBaseUrlTest {

  @Autowired private XapiClient client;

  private static MockWebServer mockWebServer;

  @BeforeAll
  static void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start(55123);
  }

  @AfterAll
  static void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenConfiguringXapiClientThenBaseUrlIsSet() throws InterruptedException {

    // When Configuring XapiClient
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK"));
    client.getStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6")).block();
    final var recordedRequest = mockWebServer.takeRequest();

    // Then BaseUrl Is Set (Request was sent to the proper url)
    assertThat(
        recordedRequest.getRequestUrl().toString(),
        anyOf(
            is(
                "http://127.0.0.1:55123/statements?statementId=4df42866-40e7-45b6-bf7c-8d5fccbdccd6"),
            is(
                "http://localhost:55123/statements?statementId=4df42866-40e7-45b6-bf7c-8d5fccbdccd6")));
  }
}
