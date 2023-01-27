/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import dev.learning.xapi.client.GetStatesRequest.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6");

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> builder.build());


  }

}
