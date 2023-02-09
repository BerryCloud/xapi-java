/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import dev.learning.xapi.client.GetStatesRequest.Builder;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * GetStatesRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("GetStatesRequest Tests")
class GetStatesRequestTests {

  @Test
  void whenBuildingGetStatesRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building GetStatesRequest With All Parameters
    Builder<?, ?> builder = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .since(Instant.now());

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingGetStatesRequestWithoutSinceParameterThenNoExceptionIsThrown() {

    // When Building GetStatesRequest Without Since Parameter
    Builder<?, ?> builder = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingGetStatesRequestWithoutRegistrationParameterThenNoExceptionIsThrown() {

    // When Building GetStatesRequest Without Registration Parameter
    Builder<?, ?> builder = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingGetStatesRequestWithRequiredParametersThenNoExceptionIsThrown() {

    // When Building GetStatesRequest With Required Parameters
    Builder<?, ?> builder = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingGetStatesRequestWithoutActivityIdThenNullPointerExceptionIsThrown() {

    // When Building GetStatesRequest Without ActivityId
    Builder<?, ?> builder = GetStatesRequest.builder()

        .agent(a -> a.name("A N Other").mbox("another@example.com"));

    // Then Null Pointer Exception Is Thrown
    assertThrows(NullPointerException.class, () -> builder.build());

  }

  @Test
  void whenBuildingGetStatesRequestWithoutAgentThenNullPointerExceptionIsThrown() {

    // When Building GetStatesRequest Without Agent
    Builder<?, ?> builder = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1");

    // Then Null Pointer Exception Is Thrown
    assertThrows(NullPointerException.class, () -> builder.build());

  }

  @Test
  void givenGetStatesRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given GetStatesRequest With All Parameters
    GetStatesRequest request = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .since(Instant.parse("2016-01-01T00:00:00Z"))

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&since=2016-01-01T00:00:00Z")));

  }

  @Test
  void givenGetStatesRequestWithoutRegistrationParameterWhenGettingURLThenResultIsExpected() {

    // Given GetStatesRequest Without Registration Parameter
    GetStatesRequest request = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D")));

  }

  @Test
  void givenGetStatesRequestWithoutSinceParameterWhenGettingURLThenResultIsExpected() {

    // Given GetStatesRequest Without Since Parameter
    GetStatesRequest request = GetStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6")));

  }

}
