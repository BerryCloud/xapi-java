/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getvoidedstatement;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get a voided statement.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetVoidedStatementApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetVoidedStatementApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Get Voided Statement
    ResponseEntity<Statement> response =
        client.getVoidedStatement(r -> r.id(postAndVoidStatement())).block();

    // Print the returned statement to the console
    System.out.println(response.getBody());
  }

  private UUID postAndVoidStatement() {

    // Post a statement
    ResponseEntity<UUID> response = client
        .postStatement(r -> r
            .statement(s -> s.agentActor(a -> a.name("A N Other")
                .mbox("mailto:another@example.com"))

                .verb(Verb.ATTEMPTED)

                .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                    .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))

        .block();

    // Void the statement
    client
        .postStatement(r -> r
            .statement(s -> s.agentActor(a -> a.name("A N Other")
                .mbox("mailto:another@example.com"))

                .verb(Verb.VOIDED)

                .statementReferenceObject(sr -> sr.id(response.getBody()))))

        .block();

    return response.getBody();
  }

}
