/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dev.learning.xapi.client.GetStateRequest.Builder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * GetStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("GetStateRequest Tests")
class GetStateRequestTests {

  @Test
  void whenBuildingGetStateRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building GetStateRequest With All Parameters
    final Builder<?, ?> builder = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetStateRequestWithoutRegistrationThenNoExceptionIsThrown() {

    // When Building GetStateRequest Without Registration
    final Builder<?, ?> builder = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .stateId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetStateRequestWithoutActivityIdThenNullPointerExceptionIsThrown() {

    // When Building GetStateRequest Without activityId
    final Builder<?, ?> builder = GetStateRequest.builder()

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .stateId("bookmark");

    // Then Null Pointer Exception Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void whenBuildingGetStateRequestWithoutAgentThenNullPointerExceptionIsThrown() {

    // When Building GetStateRequest Without Agent
    final Builder<?, ?> builder = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .stateId("bookmark");

    // Then Null Pointer Exception Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void whenBuildingGetStateRequestWithoutStateIdThenNullPointerExceptionIsThrown() {

    // When Building GetStateRequest Without StateId
    final Builder<?, ?> builder = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"));

    // Then Null Pointer Exception Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void givenGetStateRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given GetStateRequest With All Parameters
    final GetStateRequest request = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .build();

    final Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    final var result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark")));

  }

  @Test
  void givenGetStateRequestWithoutRegistrationWhenGettingURLThenResultIsExpected() {

    // Given GetStateRequest Without Registration
    final GetStateRequest request = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .stateId("bookmark")

        .build();

    final Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    final var result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D&stateId=bookmark")));

  }

}
