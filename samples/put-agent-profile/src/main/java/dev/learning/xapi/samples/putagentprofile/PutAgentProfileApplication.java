/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.putagentprofile;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample using xAPI client to put an agent profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PutAgentProfileApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(PutAgentProfileApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Put Profile
    client.putAgentProfile(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .profileId("bookmark")

        .profile(new ExampleState("Hello World!", Instant.now())))

        .block();
  }

}
