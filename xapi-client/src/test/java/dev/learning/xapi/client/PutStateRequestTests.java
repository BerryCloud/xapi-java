/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import dev.learning.xapi.client.PutStateRequest.Builder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * PutStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("PutStateRequest Tests")
class PutStateRequestTests {

  @Test
  void whenBuildingPutStateRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building PutStateRequest With All Parameters
    Builder<?, ?> builder = PutStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .state("Hello World!");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void givenPutStateRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given PutStateRequest With All Parameters
    PutStateRequest request = PutStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .state("Hello World!")

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark")));

  }

  @Test
  void givenPutStateRequestWithoutRegistrationWhenGettingURLThenResultIsExpected() {

    // Given PutStateRequest Without Registration
    PutStateRequest request = PutStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .stateId("bookmark")

        .state("Hello World!")

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(result, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22mailto%3Aanother%40example.com%22%7D&stateId=bookmark")));

  }

}
