/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getstatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.Verb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetStatementsApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  /** Main method to start the application. */
  public static void main(String[] args) {
    SpringApplication.run(GetStatementsApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Get Statements
    ResponseEntity<StatementResult> response = client.getStatements().block();

    // Print the returned statements to the console
    response.getBody().getStatements().forEach(System.out::println);

    // Get Statements with Verb filter
    ResponseEntity<StatementResult> filteredResponse =
        client.getStatements(r -> r.verb(Verb.ATTEMPTED.getId())).block();

    // Print the returned statements to the console
    filteredResponse.getBody().getStatements().forEach(System.out::println);
  }
}
