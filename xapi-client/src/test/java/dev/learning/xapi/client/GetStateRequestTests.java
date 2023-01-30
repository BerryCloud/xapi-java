/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import dev.learning.xapi.client.GetStateRequest.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    Builder<?, ?, ?> builder = GetStateRequest.builder()

        .activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());


  }

}
