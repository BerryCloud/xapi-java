/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.poststatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample using xAPI client to post multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PostStatementsApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(PostStatementsApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    Statement attemptedStatement = Statement.builder()
        .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
        .verb(Verb.ATTEMPTED)
        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
        .build();

    Statement passedStatement = attemptedStatement.toBuilder().verb(Verb.PASSED).build();

    // Post multiple statements
    client.postStatements(r -> r.statements(attemptedStatement, passedStatement)).block();
  }

}
