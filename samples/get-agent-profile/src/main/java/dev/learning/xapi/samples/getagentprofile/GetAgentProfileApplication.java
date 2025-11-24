/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getagentprofile;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get a agent profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetAgentProfileApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetAgentProfileApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Post Example profile for later retrieval
    postAgentProfile();

    // Get Profile
    ResponseEntity<ExampleState> response = client
        .getAgentProfile(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .profileId("bookmark"), ExampleState.class)

        .block();

    // Print the returned profile to the console
    System.out.println(response.getBody());

  }

  private void postAgentProfile() {

    // Post Profile
    client
        .postAgentProfile(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .profileId("bookmark")

            .profile(new ExampleState("Hello World!", Instant.now())))

        .block();

  }

}
