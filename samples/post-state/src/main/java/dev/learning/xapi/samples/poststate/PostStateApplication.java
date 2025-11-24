/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.poststate;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample using xAPI client to post a state.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PostStateApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  /** Main method to start the application. */
  public static void main(String[] args) {
    SpringApplication.run(PostStateApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

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
