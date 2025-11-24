/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getactivityprofiles;

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
 * Sample using xAPI client to get activity profiles.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetActivityProfilesApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetActivityProfilesApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Post Example Activity profile for later retrieval
    postActivityProfile();

    // Get Activity Profiles
    ResponseEntity<List<String>> response =
        client.getActivityProfiles(r -> r.activityId("https://example.com/activity/1")).block();

    // Print the each returned activity profile id to the console
    response.getBody().stream().forEach(System.out::println);
  }

  private void postActivityProfile() {

    // Post Profile
    client
        .postActivityProfile(
            r ->
                r.activityId("https://example.com/activity/1")
                    .profileId("bookmark")
                    .activityProfile(new ExampleState("Hello World!", Instant.now())))
        .block();
  }
}
