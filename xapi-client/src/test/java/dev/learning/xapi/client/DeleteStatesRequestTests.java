/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.client.DeleteStatesRequest.Builder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * DeleteStatesRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("DeleteStatesRequest Tests")
class DeleteStatesRequestTests {

  @Test
  void whenBuildingDeleteStatesRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building DeleteStatesRequest With All Parameters
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingDeleteStatesRequestWithRegistrationAsUUIDTypeThenNoExceptionIsThrown() {

    // When Building Delete States Request With Registration As UUID Type
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration(UUID.fromString("67828e3a-d116-4e18-8af3-2d2c59e27be6"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingDeleteStatesRequestWithActivityIdAsURITypeThenNoExceptionIsThrown() {

    // When Building Delete States Request With ActivityId As URI Type
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .activityId(URI.create("https://example.com/activity/1"))

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingDeleteStatesRequestWithoutRegistrationThenNoExceptionIsThrown() {

    // When Building DeleteStatesRequest Without Registration
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingDeleteStatesRequestWithoutActivityIdThenExceptionIsThrown() {

    // When Building DeleteStatesRequest Without ActivityId
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void whenBuildingDeleteStatesRequestWithoutAgentThenExceptionIsThrown() {

    // When Building DeleteStatesRequest Without Agent
    final Builder<?, ?> builder = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void givenDeleteStatesRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given DeleteStatesRequest With All Parameters
    final DeleteStatesRequest request = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .build();

    final Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    final var url =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(url, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6")));

  }

  @Test
  void givenDeleteStatesRequestWithoutRegistrationWhenGettingURLThenResultIsExpected() {

    // Given DeleteStatesRequest Without Registration
    final DeleteStatesRequest request = DeleteStatesRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .build();

    final Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    final var url =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(url, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D")));

  }

}
