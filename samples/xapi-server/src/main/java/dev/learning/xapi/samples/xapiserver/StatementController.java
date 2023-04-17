/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.validation.constraints.Statements;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic implementation of xAPI statements GET, PUT and POST resources.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI
 *      resources</a>
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Validated
@RestController
@RequestMapping(value = "/xapi/statements")
public class StatementController {

  Logger log = LoggerFactory.getLogger(StatementController.class);

  private final StatementService statementService;

  public StatementController(StatementService statementService) {

    this.statementService = statementService;
  }

  /**
   * Get a single Statement.
   *
   * @param statementId the id of the statement to get.
   *
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
   *      Statements</a>
   */
  @GetMapping(params = {"statementId", "!voidedStatementId", "!agent", "!verb", "!activity",
      "!registration", "!related_activities", "!related_agents", "!since", "!until", "!limit",
      "!ascending"})
  public ResponseEntity<Statement> getStatement(@RequestParam(required = true) UUID statementId) {

    log.debug("GET statement");

    final var statement = statementService.getStatement(statementId);

    return statement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Get Statements.
   *
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
   *      Statements</a>
   */
  @GetMapping(params = {"!statementId, !voidedStatementId"})
  public ResponseEntity<StatementResult> getStatements() {

    log.debug("GET statements");

    return ResponseEntity.ok(statementService.getStatements());
  }


  /**
   * Put Statement.
   *
   * @param statementId the statementId of the statement
   * @param statement The statement to process
   *
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#211-put-statements">PUT
   *      statements</a>
   */
  @PutMapping(params = {"statementId"}, consumes = {"application/json"})
  public ResponseEntity<Void> putStatement(@RequestParam(required = true) UUID statementId,
      @Valid @RequestBody Statement statement) {

    log.debug("PUT statement");

    statementService.processStatement(statementId, statement);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Post Statements.
   *
   * @param statements The statements to process.
   *
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#212-post-statements">POST
   *      statements</a>
   */
  @PostMapping(consumes = {"application/json"})
  public ResponseEntity<Collection<UUID>> postStatements(
      @RequestBody @Statements List<@Valid Statement> statements) {

    log.debug("POST statements");

    return new ResponseEntity<>(statementService.processStatements(statements), HttpStatus.OK);
  }

}
