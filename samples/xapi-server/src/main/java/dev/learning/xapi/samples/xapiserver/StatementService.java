/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sample Statement Service.
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Service
public class StatementService {

  private Logger log = LoggerFactory.getLogger(StatementController.class);

  /**
   * Processes a single Statement.
   *
   * @param statementId the id of the Statement
   * @param statement the Statement to process
   */
  public void processStatement(UUID statementId, Statement statement) {

    log.info("processing statement: {}", statement);

    // add custom logic here...
  }

  /**
   * Processes multiple Statements.
   *
   * @param statements the Statements to process
   * 
   * @return the statement id's that were processed
   */
  public Collection<UUID> processStatements(List<Statement> statements) {

    List<Statement> processedStatements = new ArrayList<>();

    for (Statement statement : statements) {
      log.info("processing statement: {}", statement);

      if (statement.getId() == null) {
        processedStatements.add(statement.withId(UUID.randomUUID()));
      } else {
        processedStatements.add(statement);
      }
    }

    // add custom logic here...

    return processedStatements.stream().map(s -> s.getId()).toList();
  }

}
