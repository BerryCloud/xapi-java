/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <p>
 * Enhanced Client for communicating with LRS or service which implements some of the xAPI
 * communication resources.
 * </p>
 * It adds some convenient methods to the default {@link XapiClient}.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiEnhancedClient extends XapiClient {

  /**
   * Default constructor for XapiEnhancedClient.
   *
   * @param builder a {@link WebClient.Builder} object. The caller must set the baseUrl and the
   *        authorization header.
   */
  public XapiEnhancedClient(WebClient.Builder builder) {
    super(builder);
  }

  /**
   * Gets a list of Statements as a {@link StatementIterator}.
   * <p>
   * This method loads ALL of Statements which fullfills the request filters from the LRS
   * dynamically. (It sends additional
   * {@link XapiClient#getMoreStatements(java.util.function.Consumer)} request if all the previously
   * loaded Statements were processed from the iterator.)
   * </p>
   *
   * @param request The parameters of the get statements request
   *
   * @return a {@link StatementIterator} object as a {@link Mono}.
   */
  public Mono<StatementIterator> getStatementIterator(GetStatementsRequest request) {

    return getStatements(request).map(result -> new StatementIterator(result));

  }

  /**
   * Gets a list of Statements as a {@link StatementIterator}.
   * <p>
   * This method loads ALL of Statements which fullfills the request filters from the LRS
   * dynamically. (It sends additional
   * {@link XapiClient#getMoreStatements(java.util.function.Consumer)} request if all the previously
   * loaded Statements were processed from the iterator.)
   * </p>
   *
   * @param request The parameters of the get statements request
   *
   * @return a {@link StatementIterator} object as a {@link Mono}.
   */
  public Mono<StatementIterator> getStatementIterator(
      Consumer<GetStatementsRequest.Builder> request) {

    final var builder = GetStatementsRequest.builder();

    request.accept(builder);

    return getStatementIterator(builder.build());

  }

  /**
   * Gets a list of Statements as a {@link StatementIterator}.
   * <p>
   * This method loads ALL of Statements which fullfills the request filters from the LRS
   * dynamically. (It sends additional
   * {@link XapiClient#getMoreStatements(java.util.function.Consumer)} request if all the previously
   * loaded Statements were processed from the iterator.)
   * </p>
   *
   * @return a {@link StatementIterator} object as a {@link Mono}.
   */
  public Mono<StatementIterator> getStatementIterator() {

    return getStatementIterator(r -> {
    });

  }

  /**
   * <p>
   * Voids a {@link Statement}.
   * </p>
   * The Actor of the voiding statement will be the same as the Actor of the target Statement.
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier of the
   * generated voiding Statement.
   * </p>
   *
   * @param targetStatement The {@link Statement} to be voided
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> voidStatement(Statement targetStatement) {
    return voidStatement(targetStatement.getId(), targetStatement.getActor());
  }

  /**
   * Voids a {@link Statement}.
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier of the
   * generated voiding Statement.
   * </p>
   *
   * @param targetStatement The {@link Statement} to be voided
   * @param actor the Actor of the voiding Statement
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> voidStatement(Statement targetStatement, Actor actor) {
    return voidStatement(targetStatement.getId(), actor);
  }

  /**
   * Voids a {@link Statement}.
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier of the
   * generated voiding Statement.
   * </p>
   *
   * @param targetStatementId The id of the {@link Statement} to be voided
   * @param actor the Actor of the voiding Statement
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> voidStatement(UUID targetStatementId, Actor actor) {
    Assert.notNull(targetStatementId, "Target Statement id cannot be null");
    Assert.notNull(actor, "Actor cannot be null");

    return postStatement(r -> r

        .statement(s -> s

            .actor(actor)

            .verb(Verb.VOIDED)

            .statementReferenceObject(o -> o

                .id(targetStatementId)

            )

        )

    );

  }

  /**
   * <p>
   * StatementIterator.
   * </p>
   * Iterates through the Statements of the result of a
   * {@link XapiClient#getStatements(GetStatementsRequest)}. If more Statements are available it
   * automatically loads them from the server.
   *
   * @author István Rátkai (Selindek)
   */
  @RequiredArgsConstructor
  public class StatementIterator implements Iterator<Statement> {

    private URI more;
    private Iterator<Statement> statements;

    private StatementIterator(ResponseEntity<StatementResult> response) {
      init(response);
    }

    private void init(ResponseEntity<StatementResult> response) {
      final var statementResult = response.getBody();
      more = statementResult.hasMore() ? statementResult.getMore() : null;
      final var s = statementResult.getStatements();
      statements = s == null ? Collections.emptyIterator() : s.iterator();
    }

    @Override
    public boolean hasNext() {
      return statements.hasNext() || more != null;
    }

    @Override
    public Statement next() {
      if (!statements.hasNext()) {
        if (more == null) {
          throw new NoSuchElementException();
        }
        init(getMoreStatements(r -> r.more(more)).block());
      }
      return statements.next();
    }

  }
}
