/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getmorestatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.StatementResult;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get more multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetMoreStatementsApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  /** Main method to start the application. */
  public static void main(String[] args) {
    SpringApplication.run(GetMoreStatementsApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Get Statements
    ResponseEntity<StatementResult> response = client.getStatements(r -> r.limit(1)).block();

    StatementResult result = response.getBody();

    // Print the returned statements to the console
    Arrays.asList(result.getStatements()).forEach(System.out::println);

    if (result.hasMore()) {
      // Get More Statements
      ResponseEntity<StatementResult> more =
          client.getMoreStatements(r -> r.more(result.getMore())).block();

      // Print the returned statements to the console
      Arrays.asList(more.getBody().getStatements()).forEach(System.out::println);
    }
  }
}
