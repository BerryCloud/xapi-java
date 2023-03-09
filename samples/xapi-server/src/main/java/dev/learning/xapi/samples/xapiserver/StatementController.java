/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.learning.xapi.model.Statement;

/**
 * <p>
 * StatementsController.
 * Basic implementation of xAPI statements PUT and POST endpoint.
 * </p>   
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI resources</a>
 * @author István Rátkai (Selindek)
 */
@Validated
@RestController
@RequestMapping(value = "/xapi/statements" )
public class StatementController {

  Logger log = LoggerFactory.getLogger(StatementController.class);
  
  @Autowired
  private StatementService statementService;

  /**
   * <p>
   * Put Statement.
   * </p>
   *
   * @param statementId the {@link UUID} of the statement.
   * @param statement   the body of the statement.
   * @return a {@link org.springframework.http.ResponseEntity} object.
   * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#211-put-statements">xAPI put statements endpoint</a>
   */
  @PutMapping(params = { "statementId" }, consumes = { "application/json" })
  public ResponseEntity<Void> putStatement(@RequestParam(required = true) UUID statementId,
      @Valid @RequestBody Statement statement)  {

    log.debug("PUT statement: {} Statement ID: {}", statement, statementId);

    statementService.processStatement(statementId, statement);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * <p>
   * Post Statements.
   * </p>
   *
   * @param statements an array of {@link io.launchlearning.xapi.model.Statement} objects.
   * @return a {@link org.springframework.http.ResponseEntity} object.
   * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#212-post-statements">xAPI post statements endpoint</a>
   */
  @PostMapping(consumes = { "application/json" })
  public ResponseEntity<Collection<UUID>> postStatements(@Valid @RequestBody List<Statement> statements) {

    log.debug("POST statements");

    statements.forEach(statement -> statementService.processStatement(statement.getId(), statement));

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
