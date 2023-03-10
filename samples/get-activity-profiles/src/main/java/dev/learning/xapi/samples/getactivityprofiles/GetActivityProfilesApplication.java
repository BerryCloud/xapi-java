/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getactivityprofiles;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get activity profiles.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetActivityProfilesApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetActivityProfilesApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();

    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(GetActivityProfilesApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post Example Activity profile for later retrieval
    postActivityProfile();

    // Get Activity Profiles
    ResponseEntity<List<String>> response =
        client.getActivityProfiles(r -> r.activityId("https://example.com/activity/1"))

            .block();

    // Print the each returned activity profile id to the console
    response.getBody().stream().forEach(id -> System.out.println(id));
  }

  private void postActivityProfile() {

    // Post Profile
    client.postActivityProfile(r -> r.activityId("https://example.com/activity/1")

        .profileId("bookmark")

        .activityProfile(new ExampleState("Hello World!", Instant.now())))

        .block();

  }

}
