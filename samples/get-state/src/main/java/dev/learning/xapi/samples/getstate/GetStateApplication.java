/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getstate;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get a state.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetStateApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetStateApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();


    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(GetStateApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post Example state for later retrieval
    postState();

    // Get State
    ResponseEntity<ExampleState> response = client
        .getState(r -> r.activityId("https://example.com/activity/1")

            .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

            .stateId("bookmark"), ExampleState.class)

        .block();

    // Print the returned state to the console
    System.out.println(response.getBody());

  }

  private void postState() {

    // Post State
    client.postState(r -> r.activityId("https://example.com/activity/1")

        .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

        .stateId("bookmark")

        .state(new ExampleState("Hello World!", Instant.now())))

        .block();

  }

}
