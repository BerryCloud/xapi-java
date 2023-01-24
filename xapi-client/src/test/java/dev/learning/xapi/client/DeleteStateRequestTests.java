/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * DeleteStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("DeleteStateRequest Tests")
class DeleteStateRequestTests {

  @Test
  void WhenBuildingDeleteStateRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building DeleteStateRequest With All Parameters
    assertDoesNotThrow(() -> {
      DeleteStateRequest.builder()

          .activityId("https://example.com/activity/1")

          .agent(a -> a.name("A N Other").mbox("another@example.com"))

          .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

          .stateId("bookmark")

          // Headers

          .build();

    });

    // Then No Exception Is Thrown

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutRegistrationThenNoExceptionIsThrown() {

    // When Building DeleteStateRequest Without Registration
    assertDoesNotThrow(() -> {
      DeleteStateRequest.builder()

          .activityId("https://example.com/activity/1")

          .agent(a -> a.name("A N Other").mbox("another@example.com"))

          .stateId("bookmark")

          .build();

    });

    // Then No Exception Is Thrown

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutActivityIdThenExceptionIsThrown() {


    // When Building DeleteStateRequest Without ActivityId
    assertThrows(NullPointerException.class, () -> {
      DeleteStateRequest.builder()

          .agent(a -> a.name("A N Other").mbox("another@example.com"))

          .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

          .stateId("bookmark")

          // Headers

          .build();

    });

    // Then NullPointerException Is Thrown

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutAgentThenExceptionIsThrown() {


    // When Building DeleteStateRequest Without Agent
    assertThrows(NullPointerException.class, () -> {
      DeleteStateRequest.builder()

          .activityId("https://example.com/activity/1")

          .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

          .stateId("bookmark")

          // Headers

          .build();

    });

    // Then NullPointerException Is Thrown

  }

  @Test
  void whenBuildingDeleteStateRequestWithoutStateIdThenExceptionIsThrown() {

    // When Building DeleteStateRequest Without StateId
    assertThrows(NullPointerException.class, () -> {
      DeleteStateRequest.builder()

          .activityId("https://example.com/activity/1")

          .agent(a -> a.name("A N Other").mbox("another@example.com"))

          .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

          // Headers

          .build();

    });

    // Then NullPointerException Is Thrown

  }

}


