/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import dev.learning.xapi.client.DeleteStateRequest.Builder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * DeleteStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("DeleteStateRequest Tests")
class DeleteStateRequestTests {

  @Test
  void whenBuildingDeleteStateRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building DeleteStateRequest With All Parameters
    Builder<?, ?> builder = DeleteStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutRegistrationThenNoExceptionIsThrown() {

    // When Building DeleteStateRequest Without Registration
    Builder<?, ?> builder = DeleteStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .stateId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutActivityIdThenExceptionIsThrown() {

    // When Building DeleteStateRequest Without ActivityId
    Builder<?, ?> builder = DeleteStateRequest.builder()

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, () -> builder.build());

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutAgentThenExceptionIsThrown() {

    // When Building DeleteStateRequest Without Agent
    Builder<?, ?> builder = DeleteStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, () -> builder.build());

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutStateIdThenExceptionIsThrown() {

    // When Building DeleteStateRequest Without StateId
    Builder<?, ?> builder = DeleteStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, () -> builder.build());

  }

  @Test
  void When() {

    UriBuilder builder = UriComponentsBuilder.fromUriString("https://example.com/xapi/");

    // When
    DeleteStateRequest request = DeleteStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    URI url = request.url(builder, queryParams).build(queryParams);

    // Then
    assertThat(url, is(URI.create(
        "https://example.com/xapi/activities/state?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&agent=%7B%22name%22%3A%22A%20N%20Other%22%2C%22mbox%22%3A%22another%40example.com%22%7D&registration=67828e3a-d116-4e18-8af3-2d2c59e27be6&stateId=bookmark")));

  }

}


