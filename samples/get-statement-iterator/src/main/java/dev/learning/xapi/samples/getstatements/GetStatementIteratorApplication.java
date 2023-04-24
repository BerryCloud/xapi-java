/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getstatements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Verb;
 
/**
 * Sample using xAPI client to get multiple statements as StatementIterator.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@SpringBootApplication
public class GetStatementIteratorApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetStatementIteratorApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Get Statements as StatementIterator
    var iterator = client.getStatementIterator().block();

    // Print the returned statements to the console
    iterator.toStream().forEach(s -> System.out.println(s));

    // Get Statements with Verb filter as StatementIterator
    var filteredStatements =
        client.getStatementIterator(r -> r.verb(Verb.ATTEMPTED.getId())).block();

    // Print the returned statements to the console
    filteredStatements.toStream().forEach(s -> System.out.println(s));

  }

}
