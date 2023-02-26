/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for communicating with LRS or service which implements some of the xAPI communication
 * resources.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI
 *      communication resources</a>
 */
public class XapiClient {

  private final WebClient webClient;

  /**
   * Default constructor for XapiClient.
   *
   * @param builder a {@link WebClient.Builder} object. The caller must set the baseUrl and the
   *        authorization header.
   */
  public XapiClient(WebClient.Builder builder) {
    this.webClient = builder

        .defaultHeader("X-Experience-API-Version", "1.0.3")

        .build();
  }

  /**
   * Gets a Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getStatement(GetStatementRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Statement.class);

  }

  /**
   * Gets a Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getStatement(
      Consumer<GetStatementRequest.Builder<?, ?>> request) {

    final GetStatementRequest.Builder<?, ?> builder = GetStatementRequest.builder();

    request.accept(builder);

    return getStatement(builder.build());

  }

  /**
   * Posts Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> postStatement(PostStatementRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .bodyValue(request.getStatement())

        .retrieve()

        .toEntity(UUID[].class)

        .map(i -> ResponseEntity.ok().headers(i.getHeaders()).body(i.getBody()[0]));

  }

  /**
   * Posts Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> postStatement(Consumer<PostStatementRequest.Builder> request) {

    final PostStatementRequest.Builder builder = PostStatementRequest.builder();

    request.accept(builder);

    return postStatement(builder.build());

  }

  /**
   * Post Statements.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and an array of Statement
   * identifiers.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID[]>> postStatements(PostStatementsRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .bodyValue(request.getStatements())

        .retrieve()

        .toEntity(UUID[].class);

  }

  /**
   * Posts Statements.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and an array of Statement
   * identifiers.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID[]>> postStatements(
      Consumer<PostStatementsRequest.Builder> request) {

    final PostStatementsRequest.Builder builder = PostStatementsRequest.builder();

    request.accept(builder);

    return postStatements(builder.build());

  }

  /**
   * Gets a voided Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the voided Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getVoidedStatement(GetVoidedStatementRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Statement.class);

  }

  /**
   * Gets a voided Statement.
   * 
   * <p>
   * The returned ResponseEntity contains the response headers and the voided Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getVoidedStatement(
      Consumer<GetVoidedStatementRequest.Builder<?, ?>> request) {

    final GetVoidedStatementRequest.Builder<?, ?> builder = GetVoidedStatementRequest.builder();

    request.accept(builder);

    return getVoidedStatement(builder.build());

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getStatements() {

    return getStatements(GetStatementsRequest.builder().build());
  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The parameters of the get statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getStatements(GetStatementsRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(StatementResult.class);

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The Consumer Builder for the get statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getStatements(
      Consumer<GetStatementsRequest.Builder> request) {

    final GetStatementsRequest.Builder builder = GetStatementsRequest.builder();

    request.accept(builder);

    return getStatements(builder.build());

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The parameters of the get more statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getMoreStatements(GetMoreStatementsRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(StatementResult.class);

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The Consumer Builder for the get more statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getMoreStatements(
      Consumer<GetMoreStatementsRequest.Builder> request) {

    final GetMoreStatementsRequest.Builder builder = GetMoreStatementsRequest.builder();

    request.accept(builder);

    return getMoreStatements(builder.build());

  }

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the get state request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getState(GetStateRequest request, Class<T> bodyType) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(bodyType);

  }

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the get state request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getState(Consumer<GetStateRequest.Builder<?, ?>> request,
      Class<T> bodyType) {

    final GetStateRequest.Builder<?, ?> builder = GetStateRequest.builder();

    request.accept(builder);

    return getState(builder.build(), bodyType);

  }

  /**
   * Posts a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the post state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postState(PostStateRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getState())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Posts a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the post state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postState(Consumer<PostStateRequest.Builder<?, ?>> request) {

    final PostStateRequest.Builder<?, ?> builder = PostStateRequest.builder();

    request.accept(builder);

    return postState(builder.build());

  }

  /**
   * Puts a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the put state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putState(PutStateRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getState())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Puts a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the put state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putState(Consumer<PutStateRequest.Builder<?, ?>> request) {

    final PutStateRequest.Builder<?, ?> builder = PutStateRequest.builder();

    request.accept(builder);

    return putState(builder.build());

  }

  /**
   * Deletes a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteState(DeleteStateRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the delete state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteState(
      Consumer<DeleteStateRequest.Builder<?, ?>> request) {

    final DeleteStateRequest.Builder<?, ?> builder = DeleteStateRequest.builder();

    request.accept(builder);

    return deleteState(builder.build());

  }

  /**
   * Gets all stateId's specified by the given activityId, agent and optional registration and since
   * parameters.
   *
   * @param request The parameters of the get states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<String[]>> getStates(GetStatesRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(String[].class);

  }

  /**
   * Gets all stateId's specified by the given activityId, agent and optional registration and since
   * parameters.
   *
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the get states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<String[]>> getStates(
      Consumer<GetStatesRequest.Builder<?, ?>> request) {

    final GetStatesRequest.Builder<?, ?> builder = GetStatesRequest.builder();

    request.accept(builder);

    return getStates(builder.build());

  }

  /**
   * Deletes all documents specified by the given activityId, agent and optional registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteStates(DeleteStatesRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes all documents specified by the given activityId, agent and optional registration.
   *
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the delete states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteStates(
      Consumer<DeleteStatesRequest.Builder<?, ?>> request) {

    final DeleteStatesRequest.Builder<?, ?> builder = DeleteStatesRequest.builder();

    request.accept(builder);

    return deleteStates(builder.build());

  }

  public <T> Mono<ResponseEntity<T>> getAgentProfile(GetAgentProfileRequest request,
      Class<T> bodyType) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(bodyType);

  }

  public <T> Mono<ResponseEntity<T>> getAgentProfile(
      Consumer<GetAgentProfileRequest.Builder<?, ?>> request, Class<T> bodyType) {

    final GetAgentProfileRequest.Builder<?, ?> builder = GetAgentProfileRequest.builder();

    request.accept(builder);

    return getAgentProfile(builder.build(), bodyType);

  }



}
