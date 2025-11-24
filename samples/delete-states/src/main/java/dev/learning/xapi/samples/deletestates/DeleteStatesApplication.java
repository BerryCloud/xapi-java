/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.deletestates;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample using xAPI client to delete multiple states.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class DeleteStatesApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(DeleteStatesApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Post state for later deletion
    postState();

    // Delete states
    client
        .deleteStates(
            r ->
                r.activityId("https://example.com/activity/1")
                    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))
                    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6"))
        .block();
  }

  private void postState() {

    // Post State
    client
        .postState(
            r ->
                r.activityId("https://example.com/activity/1")
                    .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))
                    .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")
                    .stateId("bookmark")
                    .state(new ExampleState("Hello World!", Instant.now())))
        .block();
  }
}
