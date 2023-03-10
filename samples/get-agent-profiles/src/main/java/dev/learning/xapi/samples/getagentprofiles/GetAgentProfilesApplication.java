/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getagentprofiles;

import dev.learning.xapi.client.XapiClient;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get a agent profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetAgentProfilesApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetAgentProfilesApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();

    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(GetAgentProfilesApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Get Profiles
    ResponseEntity<List<String>> response = client
        .getAgentProfiles(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com")))

        .block();

    // Print the each returned profile id to the console
    response.getBody().stream().forEach(id -> System.out.println(id));

  }

}
