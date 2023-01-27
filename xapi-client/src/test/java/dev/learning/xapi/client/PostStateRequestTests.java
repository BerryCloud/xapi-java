/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PutStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("PostStateRequest Tests")
class PostStateRequestTests {


  // activityId | Required
  // agent | Required
  // registration | Optional
  // stateId | Required


  @Test
  void whenBuildingPutStateRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building PutStateRequest With All Parameters
    assertDoesNotThrow(() -> {
      PutStateRequest.builder()

          // Parameters

          .activityId("https://example.com/activity/1")

          .agent(a -> a.name("A N Other").mbox("another@example.com"))

          .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

          .stateId("bookmark")

          // Body

          .state("Hello World!")

          // Headers

          .build();

    });

    // Then No Exception Is Thrown

  }

}
