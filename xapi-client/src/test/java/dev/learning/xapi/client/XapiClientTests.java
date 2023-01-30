/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;


import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DeleteStateRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("DeleteStateRequest Tests")
class XapiClientTests {


  XapiClient client = new XapiClient(null);

  @Test
  void deleteState() {

    client.deleteState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark"));

  }

  @Test
  void deleteStates() {

    client.deleteStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

    );

  }

  @Test
  void putState() {

    client.putState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .state("Hello")

    );

  }

  @Test
  void postState() {

    client.putState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .state("Hello")

    );

  }

  @Test
  void getState() {

    client.getState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark"));

  }

  @Test
  void getStates() {

    client.getStates(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .since(Instant.parse("2023-01-30T22:27:09Z"))

    );

  }

}


