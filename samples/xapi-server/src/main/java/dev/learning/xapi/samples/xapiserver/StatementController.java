/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.validation.constraints.Statements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Basic implementation of xAPI statements GET, PUT and POST resources.
 *
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI
 *     resources</a>
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Validated
@RestController
@RequestMapping(value = "/xapi/statements")
public class StatementController {

  private static final Set<String> GET_PARAMETERS =
      Set.of(
          "statementId",
          "voidedStatementId",
          "agent",
          "verb",
          "activity",
          "registration",
          "related_activities",
          "related_agents",
          "since",
          "until",
          "limit",
          "format",
          "attachments",
          "ascending");

  private static final Set<String> SINGLE_STATEMENT_PARAMETERS = Set.of("format", "attachments");

  Logger log = LoggerFactory.getLogger(StatementController.class);

  private final StatementService statementService;

  /**
   * Constructor for StatementController.
   *
   * @param statementService the statement service
   */
  public StatementController(StatementService statementService) {

    this.statementService = statementService;
  }

  /**
   * Validates parameter names and combinations before a statement request reaches the service.
   *
   * @param request the statement request
   */
  @ModelAttribute
  public void validateParameters(HttpServletRequest request) {
    final var parameters = request.getParameterMap().keySet();
    final var allowed =
        switch (request.getMethod()) {
          case "GET", "HEAD" -> GET_PARAMETERS;
          case "PUT" -> Set.of("statementId");
          default -> Set.<String>of();
        };
    if (!allowed.containsAll(parameters)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unrecognised statement parameter");
    }
    if (request.getMethod().equals("GET") || request.getMethod().equals("HEAD")) {
      for (final var id : List.of("statementId", "voidedStatementId")) {
        if (parameters.contains(id)
            && parameters.stream()
                .anyMatch(
                    name -> !name.equals(id) && !SINGLE_STATEMENT_PARAMETERS.contains(name))) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, id + " may only be combined with format and attachments");
        }
      }
    }
  }

  /**
   * Handles voided statement retrieval until voiding support is implemented.
   *
   * @param voidedStatementId the id of the voided statement
   * @return a not implemented response
   */
  @GetMapping(params = {"voidedStatementId", "!statementId"})
  public ResponseEntity<Void> getVoidedStatement(@RequestParam UUID voidedStatementId) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * Get a single Statement.
   *
   * @param statementId the id of the statement to get.
   * @return the ResponseEntity
   * @see <a href=
   *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
   *     Statements</a>
   */
  @GetMapping(params = "statementId")
  public ResponseEntity<Statement> getStatement(@RequestParam(required = true) UUID statementId) {

    log.debug("GET statement");

    final var statement = statementService.getStatement(statementId);

    return statement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Get Statements.
   *
   * @param since the instant since when to get statements
   * @return the ResponseEntity
   * @see <a href=
   *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
   *     Statements</a>
   */
  @GetMapping(params = {"since", "!statementId", "!voidedStatementId"})
  public ResponseEntity<Void> getStatementsSince(@RequestParam Instant since) {

    log.debug("GET statements since");

    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * Get Statements.
   *
   * @return the ResponseEntity
   * @see <a href=
   *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
   *     Statements</a>
   */
  @GetMapping(params = {"!statementId", "!voidedStatementId", "!since"})
  public ResponseEntity<StatementResult> getStatements() {

    log.debug("GET statements");

    return ResponseEntity.ok(statementService.getStatements());
  }

  /**
   * Put Statement.
   *
   * @param statementId the statementId of the statement
   * @param statement The statement to process
   * @return the ResponseEntity
   * @see <a href=
   *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#211-put-statements">PUT
   *     statements</a>
   */
  @PutMapping(
      params = {"statementId"},
      consumes = {"application/json"})
  public ResponseEntity<Void> putStatement(
      @RequestParam(required = true) UUID statementId, @Valid @RequestBody Statement statement) {

    log.debug("PUT statement");

    statementService.processStatement(statementId, statement);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Post Statements.
   *
   * @param statements The statements to process.
   * @return the ResponseEntity
   * @see <a href=
   *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#212-post-statements">POST
   *     statements</a>
   */
  @PostMapping(consumes = {"application/json"})
  public ResponseEntity<Collection<UUID>> postStatements(
      @RequestBody @Statements List<@Valid Statement> statements) {

    log.debug("POST statements");

    return new ResponseEntity<>(statementService.processStatements(statements), HttpStatus.OK);
  }
}
