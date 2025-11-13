/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
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

  private final Logger log = LoggerFactory.getLogger(StatementService.class);

  private final StatementRepository repository;
  private final ObjectMapper mapper;

  /**
   * StatementService Constructor.
   */
  public StatementService(StatementRepository repository, ObjectMapper mapper) {

    this.repository = repository;
    this.mapper = mapper;

  }

  /**
   * Get a single Statement.
   *
   * @param statementId the id of the Statement to get
   *
   * @return the statement with the given id or Optional#empty() no statement was found.
   */
  public Optional<Statement> getStatement(UUID statementId) {

    log.info("get statement: {}", statementId);

    // add custom logic here...

    return repository.findById(statementId).map(e -> convertToStatement(e));

  }

  /**
   * Get multiple Statements.
   *
   * @return populated StatementResults
   */
  public StatementResult getStatements() {

    log.info("get statements");

    // add custom logic here...

    final var statements = StreamSupport.stream(repository.findAll().spliterator(), false).limit(10)
        .map(e -> convertToStatement(e)).toList();

    return StatementResult.builder().statements(statements).more(URI.create("")).build();

  }

  /**
   * Processes a single Statement.
   *
   * @param statementId the id of the Statement
   * @param statement the Statement to process
   */
  public void processStatement(UUID statementId, Statement statement) {

    log.info("processing statement: {}", statement);

    // add custom logic here...

    repository.save(new StatementEntity(statementId,
        mapper.valueToTree(statement.withId(statementId).withStored(Instant.now()))));

  }

  /**
   * Processes multiple Statements.
   *
   * @param statements the Statements to process
   *
   * @return the statement id's that were processed
   */
  public Collection<UUID> processStatements(List<Statement> statements) {

    final List<Statement> processedStatements = new ArrayList<>();

    for (final Statement statement : statements) {
      log.info("processing statement: {}", statement);

      if (statement.getId() == null) {
        processedStatements.add(statement.withId(UUID.randomUUID()).withStored(Instant.now()));
      } else {
        processedStatements.add(statement.withStored(Instant.now()));
      }
    }

    // add custom logic here...

    repository.saveAll(processedStatements.stream()
        .map(s -> new StatementEntity(s.getId(), mapper.valueToTree(s))).toList());

    return processedStatements.stream().map(s -> s.getId()).toList();
  }

  private Statement convertToStatement(StatementEntity statementEntity) {

    try {
      final var statement = mapper.treeToValue(statementEntity.getStatement(), Statement.class);

      return statement;
    } catch (final JsonProcessingException e) {
      log.error("Error processing stored Statement", e);

      return null;
    }

  }

}
