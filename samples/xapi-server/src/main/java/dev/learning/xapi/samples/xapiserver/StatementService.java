/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/**
 * Sample Statement Service.
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Service
public class StatementService {

  private static final int PAGE_SIZE = 10;

  private final Logger log = LoggerFactory.getLogger(StatementService.class);

  private final StatementRepository repository;
  private final ObjectMapper mapper;

  /**
   * StatementService Constructor.
   *
   * @param repository the statement repository
   * @param mapper the object mapper
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

    return buildStatementResult(0, null);

  }

  /**
   * Get multiple Statements since a specific time.
   *
   * @param since return statements stored since this instant (inclusive)
   *
   * @return populated StatementResults
   */
  public StatementResult getStatementsSince(Instant since) {

    log.info("get statements since: {}", since);

    return buildStatementResult(0, since);

  }

  /**
   * Get multiple Statements using a more token.
   *
   * @param moreToken the more token indicating where to continue retrieval
   *
   * @return populated StatementResults
   */
  public StatementResult getStatementsMore(String moreToken) {

    log.info("get statements more: {}", moreToken);

    final var more = decodeMoreToken(moreToken);

    return buildStatementResult(more.page(), more.since());

  }

  private StatementResult buildStatementResult(int page, Instant since) {

    final Pageable pageable = PageRequest.of(page, PAGE_SIZE);

    final Slice<StatementEntity> slice;
    if (since == null) {
      slice = repository.findAllByOrderByStoredAscIdAsc(pageable);
    } else {
      slice = repository.findByStoredGreaterThanEqualOrderByStoredAscIdAsc(since, pageable);
    }

    final var statements = slice.getContent().stream()
        .map(this::convertToStatement)
        .filter(Objects::nonNull)
        .toList();

    final var more = slice.hasNext() ? URI.create("/xapi/statements?more="
        + encodeMoreToken(page + 1, since)) : URI.create("");

    return StatementResult.builder().statements(statements).more(more).build();

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

    final Instant stored = Instant.now();

    repository.save(new StatementEntity(statementId,
        mapper.valueToTree(statement.withId(statementId).withStored(stored)), stored));

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

    final Instant stored = Instant.now();

    for (final Statement statement : statements) {
      log.info("processing statement: {}", statement);

      if (statement.getId() == null) {
        processedStatements.add(statement.withId(UUID.randomUUID()).withStored(stored));
      } else {
        processedStatements.add(statement.withStored(stored));
      }
    }

    // add custom logic here...

    repository.saveAll(processedStatements.stream()
        .map(s -> new StatementEntity(s.getId(), mapper.valueToTree(s), s.getStored())).toList());

    return processedStatements.stream().map(s -> s.getId()).toList();
  }

  private String encodeMoreToken(int page, Instant since) {

    final var sinceValue = since == null ? "" : since.toString();
    final var payload = page + "|" + sinceValue;

    return Base64.getUrlEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

  }

  private MoreToken decodeMoreToken(String token) {

    try {
      final var decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
      final var parts = decoded.split("\\|", -1);

      if (parts.length < 1 || parts[0].isBlank()) {
        throw new IllegalArgumentException("Invalid more token format: missing page number");
      }

      final var page = Integer.parseInt(parts[0]);
      final Instant since;

      if (parts.length > 1 && !parts[1].isBlank()) {
        since = Instant.parse(parts[1]);
      } else {
        since = null;
      }

      return new MoreToken(page, since);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid more token", ex);
    }

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

  private record MoreToken(int page, Instant since) {}

}
