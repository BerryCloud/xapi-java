/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getstates;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get states.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetStatesApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  /** Main method to start the application. */
  public static void main(String[] args) {
    SpringApplication.run(GetStatesApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Post Example state for later retrieval
    postState();

    // Get States
    ResponseEntity<List<String>> response =
        client
            .getStates(
                r ->
                    r.activityId("https://example.com/activity/1")
                        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))
                        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6"))
            .block();

    // Print the each returned state id to the console
    response.getBody().stream().forEach(System.out::println);
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
