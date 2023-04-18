/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getactivity;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get an activity.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetActivityApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetActivityApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post statement for later retrieval of activity
    postStatement();

    // Get Activity
    ResponseEntity<Activity> response = client
        .getActivity(r -> r.activityId(URI.create("https://example.com/activity/simplestatement")))
        .block();

    // Print the returned activity to the console
    System.out.println(response.getBody());
  }

  private UUID postStatement() {

    // Post a statement
    ResponseEntity<
        UUID> response =
            client
                .postStatement(r -> r.statement(
                    s -> s.agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

                        .verb(Verb.ATTEMPTED)

                        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
                .block();

    return response.getBody();
  }

}
