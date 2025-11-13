/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getagentprofiles;

import dev.learning.xapi.client.XapiClient;
import java.util.List;
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
public class GetAgentProfilesApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetAgentProfilesApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Get Profiles
    ResponseEntity<List<String>> response = client
        .getAgentProfiles(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com")))

        .block();

    // Print the each returned profile id to the console
    response.getBody().stream().forEach(System.out::println);

  }

}
