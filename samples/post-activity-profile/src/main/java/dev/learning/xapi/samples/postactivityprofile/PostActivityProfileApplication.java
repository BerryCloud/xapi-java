/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.postactivityprofile;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample using xAPI client to post an activity profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PostActivityProfileApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(PostActivityProfileApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post activity profile
    client.postActivityProfile(r -> r

        .activityId("https://example.com/activity/1")

        .profileId("bookmark")

        .activityProfile(new ExampleState("Hello World!", Instant.now())))

        .block();

  }

}
