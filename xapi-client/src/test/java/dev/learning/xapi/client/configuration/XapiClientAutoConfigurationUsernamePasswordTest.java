/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.client.configuration.XapiClientAutoConfigurationUsernamePasswordTest.XapiTestClientConfiguration;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient.Builder;

/**
 * XapiClientAutoConfigurationUsernamePassword Test.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("XapiClientAutoConfigurationUsernamePassword Test")
@SpringBootTest(
    classes = { XapiClientAutoConfiguration.class, WebClientAutoConfiguration.class,
        XapiTestClientConfiguration.class, JacksonAutoConfiguration.class },
    properties = { "xapi.client.username = username", "xapi.client.password = password" })
class XapiClientAutoConfigurationUsernamePasswordTest {

  @Autowired
  private XapiClient client;

  private static MockWebServer mockWebServer;

  static class XapiTestClientConfiguration implements XapiClientConfigurer {

    @Override
    public void accept(Builder builder) {
      mockWebServer = new MockWebServer();
      try {
        mockWebServer.start();
      } catch (final IOException e) {
      }
      builder.baseUrl(mockWebServer.url("").toString());

    }

  }

  @AfterAll
  static void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenConfiguringXapiClientThenAuthenticationIsSet() throws InterruptedException {

    // When Configuring XapiClient
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK"));
    client.getStatement(r -> r.id("4df42866-40e7-45b6-bf7c-8d5fccbdccd6")).block();
    final RecordedRequest recordedRequest = mockWebServer.takeRequest();

    // Then Authorization Is Set
    assertThat(recordedRequest.getHeaders().get(HttpHeaders.AUTHORIZATION),
        is("Basic dXNlcm5hbWU6cGFzc3dvcmQ="));
  }

}
