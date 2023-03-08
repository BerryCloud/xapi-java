/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.putagentprofile;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to put an agent profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PutAgentProfileApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public PutAgentProfileApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();


    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(PutAgentProfileApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Put Profile
    client.putAgentProfile(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .profileId("bookmark")

        .profile(new ExampleState("Hello World!", Instant.now())))

        .block();
  }

}
