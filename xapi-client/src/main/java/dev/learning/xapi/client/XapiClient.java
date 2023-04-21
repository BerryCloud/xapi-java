/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.About;
import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Person;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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

  private static final ParameterizedTypeReference<List<UUID>> LIST_UUID_TYPE =
      new ParameterizedTypeReference<>() {};

  private static final ParameterizedTypeReference<List<String>> LIST_STRING_TYPE =
      new ParameterizedTypeReference<>() {};

  /**
   * Default constructor for XapiClient.
   *
   * @param builder a {@link WebClient.Builder} object. The caller must set the baseUrl and the
   *        authorization header.
   */
  public XapiClient(WebClient.Builder builder) {
    this.webClient = builder

        .defaultHeader("X-Experience-API-Version", "1.0.3")

        .codecs(configurer -> {

          configurer.customCodecs()
              .register(new StatementHttpMessageWriter(configurer.getWriters()));

          configurer.customCodecs().register(new StatementHttpMessageReader());

        }).build();
  }

  // Statement Resource

  /**
   * Gets a Statement.
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getStatement(GetStatementRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Statement.class);

  }

  /**
   * Gets a Statement.
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
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> postStatement(PostStatementRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .bodyValue(request.getStatement())

        .retrieve()

        .toEntity(LIST_UUID_TYPE)

        .map(i -> ResponseEntity.ok().headers(i.getHeaders()).body(i.getBody().get(0)));

  }

  /**
   * Posts Statement.
   * <p>
   * The returned ResponseEntity contains the response headers and the Statement identifier.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<UUID>> postStatement(Consumer<PostStatementRequest.Builder> request) {

    final var builder = PostStatementRequest.builder();

    request.accept(builder);

    return postStatement(builder.build());

  }

  /**
   * Post Statements.
   * <p>
   * The returned ResponseEntity contains the response headers and an array of Statement
   * identifiers.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<UUID>>> postStatements(PostStatementsRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .bodyValue(request.getStatements())

        .retrieve()

        .toEntity(LIST_UUID_TYPE);

  }

  /**
   * Posts Statements.
   * <p>
   * The returned ResponseEntity contains the response headers and an array of Statement
   * identifiers.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<UUID>>> postStatements(
      Consumer<PostStatementsRequest.Builder> request) {

    final var builder = PostStatementsRequest.builder();

    request.accept(builder);

    return postStatements(builder.build());

  }

  /**
   * Gets a voided Statement.
   * <p>
   * The returned ResponseEntity contains the response headers and the voided Statement.
   * </p>
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Statement>> getVoidedStatement(GetVoidedStatementRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Statement.class);

  }

  /**
   * Gets a voided Statement.
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
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The parameters of the get statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getStatements(GetStatementsRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(StatementResult.class);

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
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

    final var builder = GetStatementsRequest.builder();

    request.accept(builder);

    return getStatements(builder.build());

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
   * <p>
   * The returned ResponseEntity contains the response headers and StatementResult.
   * </p>
   *
   * @param request The parameters of the get more statements request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<StatementResult>> getMoreStatements(GetMoreStatementsRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(StatementResult.class);

  }

  /**
   * Gets a StatementResult object, a list of Statements. If additional results are available, an
   * URL to retrieve them will be included in the StatementResult Object.
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

    final var builder = GetMoreStatementsRequest.builder();

    request.accept(builder);

    return getMoreStatements(builder.build());

  }

  // State Resource

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the get state request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getState(GetStateRequest request, Class<T> bodyType) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(bodyType);

  }

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
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
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the post state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postState(PostStateRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

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
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the put state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putState(PutStateRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

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
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteState(DeleteStateRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes a single document specified by the given stateId activity, agent, and optional
   * registration.
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
  public Mono<ResponseEntity<List<String>>> getStates(GetStatesRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(LIST_STRING_TYPE);

  }

  /**
   * Gets all stateId's specified by the given activityId, agent and optional registration and since
   * parameters.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the get states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<String>>> getStates(
      Consumer<GetStatesRequest.Builder<?, ?>> request) {

    final GetStatesRequest.Builder<?, ?> builder = GetStatesRequest.builder();

    request.accept(builder);

    return getStates(builder.build());

  }

  /**
   * Deletes all documents specified by the given activityId, agent and optional registration.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete states request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteStates(DeleteStatesRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes all documents specified by the given activityId, agent and optional registration.
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

  // Agents Resource

  /**
   * Return a special, Person Object for a specified Agent. The Person Object is very similar to an
   * Agent Object, but instead of each attribute having a single value, each attribute has an array
   * value, and it is legal to include multiple identifying properties.
   *
   * @param request The parameters of the get agents request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Person>> getAgents(GetAgentsRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Person.class);

  }

  /**
   * Return a special, Person Object for a specified Agent. The Person Object is very similar to an
   * Agent Object, but instead of each attribute having a single value, each attribute has an array
   * value, and it is legal to include multiple identifying properties.
   *
   * @param request The Consumer Builder for the get agents request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Person>> getAgents(Consumer<GetAgentsRequest.Builder> request) {

    final var builder = GetAgentsRequest.builder();

    request.accept(builder);

    return getAgents(builder.build());

  }

  // Activities Resource

  /**
   * Loads the complete Activity Object specified.
   *
   * @param request The parameters of the get activity request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Activity>> getActivity(GetActivityRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(Activity.class);

  }

  /**
   * Loads the complete Activity Object specified.
   *
   * @param request The Consumer Builder for the get activity request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Activity>> getActivity(Consumer<GetActivityRequest.Builder> request) {

    final var builder = GetActivityRequest.builder();

    request.accept(builder);

    return getActivity(builder.build());

  }

  // Agent Profile Resource

  /**
   * Gets a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the get agent profile request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getAgentProfile(GetAgentProfileRequest request,
      Class<T> bodyType) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(bodyType);

  }

  /**
   * Gets a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the get agent profile request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getAgentProfile(
      Consumer<GetAgentProfileRequest.Builder<?, ?>> request, Class<T> bodyType) {

    final GetAgentProfileRequest.Builder<?, ?> builder = GetAgentProfileRequest.builder();

    request.accept(builder);

    return getAgentProfile(builder.build(), bodyType);

  }

  /**
   * Deletes a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteAgentProfile(DeleteAgentProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the delete agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteAgentProfile(
      Consumer<DeleteAgentProfileRequest.Builder<?, ?>> request) {

    final DeleteAgentProfileRequest.Builder<?, ?> builder = DeleteAgentProfileRequest.builder();

    request.accept(builder);

    return deleteAgentProfile(builder.build());

  }

  /**
   * Puts a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the put agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putAgentProfile(PutAgentProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getProfile())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Puts a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the put agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putAgentProfile(
      Consumer<PutAgentProfileRequest.Builder<?, ?>> request) {

    final PutAgentProfileRequest.Builder<?, ?> builder = PutAgentProfileRequest.builder();

    request.accept(builder);

    return putAgentProfile(builder.build());

  }

  /**
   * Posts a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the post agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postAgentProfile(PostAgentProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getProfile())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Posts a single agent profile by the given agent and profileId.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the post agent profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postAgentProfile(
      Consumer<PostAgentProfileRequest.Builder<?, ?>> request) {

    final PostAgentProfileRequest.Builder<?, ?> builder = PostAgentProfileRequest.builder();

    request.accept(builder);

    return postAgentProfile(builder.build());

  }

  /**
   * Gets profile ids of all Profile documents for an Agent. If "since" parameter is specified, this
   * is limited to entries that have been stored or updated since the specified Timestamp
   * (exclusive).
   *
   * @param request The parameters of the get agent profiles request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<String>>> getAgentProfiles(GetAgentProfilesRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(LIST_STRING_TYPE);

  }

  /**
   * Gets profile ids of all Profile documents for an Agent. If "since" parameter is specified, this
   * is limited to entries that have been stored or updated since the specified Timestamp
   * (exclusive).
   *
   * @param request The Consumer Builder for the get agent profiles request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<String>>> getAgentProfiles(
      Consumer<GetAgentProfilesRequest.Builder> request) {

    final var builder = GetAgentProfilesRequest.builder();

    request.accept(builder);

    return getAgentProfiles(builder.build());

  }

  // Activity Profile Resource

  /**
   * Fetches the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the get activity profile request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getActivityProfile(GetActivityProfileRequest request,
      Class<T> bodyType) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(bodyType);

  }

  /**
   * Fetches the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the get activity profile request
   *
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getActivityProfile(
      Consumer<GetActivityProfileRequest.Builder<?, ?>> request, Class<T> bodyType) {

    final GetActivityProfileRequest.Builder<?, ?> builder = GetActivityProfileRequest.builder();

    request.accept(builder);

    return getActivityProfile(builder.build(), bodyType);

  }

  /**
   * Changes or stores the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the post activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postActivityProfile(PostActivityProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getActivityProfile())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Changes or stores the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the post activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postActivityProfile(
      Consumer<PostActivityProfileRequest.Builder<?, ?>> request) {

    final PostActivityProfileRequest.Builder<?, ?> builder = PostActivityProfileRequest.builder();

    request.accept(builder);

    return postActivityProfile(builder.build());

  }

  /**
   * Stores the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The parameters of the put activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putActivityProfile(PutActivityProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .contentType(request.getContentType())

        .bodyValue(request.getActivityProfile())

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Stores the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers and body.
   * </p>
   *
   * @param request The Consumer Builder for the put activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> putActivityProfile(
      Consumer<PutActivityProfileRequest.Builder<?, ?>> request) {

    final PutActivityProfileRequest.Builder<?, ?> builder = PutActivityProfileRequest.builder();

    request.accept(builder);

    return putActivityProfile(builder.build());

  }

  /**
   * Deletes the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the delete activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteActivityProfile(DeleteActivityProfileRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toBodilessEntity();

  }

  /**
   * Deletes the specified Profile document in the context of the specified Activity.
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the delete activity profile request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteActivityProfile(
      Consumer<DeleteActivityProfileRequest.Builder<?, ?>> request) {

    final DeleteActivityProfileRequest.Builder<?, ?> builder =
        DeleteActivityProfileRequest.builder();

    request.accept(builder);

    return deleteActivityProfile(builder.build());

  }

  /**
   * Fetches Profile ids of all Profile documents for an Activity. If "since" parameter is
   * specified, this is limited to entries that have been stored or updated since the specified
   * Timestamp (exclusive).
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The parameters of the get activity profiles request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<String>>> getActivityProfiles(
      GetActivityProfilesRequest request) {

    final Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(LIST_STRING_TYPE);

  }

  /**
   * Fetches Profile ids of all Profile documents for an Activity. If "since" parameter is
   * specified, this is limited to entries that have been stored or updated since the specified
   * Timestamp (exclusive).
   * <p>
   * The returned ResponseEntity contains the response headers.
   * </p>
   *
   * @param request The Consumer Builder for the get activity profiles request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<List<String>>> getActivityProfiles(
      Consumer<GetActivityProfilesRequest.Builder> request) {

    final var builder = GetActivityProfilesRequest.builder();

    request.accept(builder);

    return getActivityProfiles(builder.build());

  }

  // About Resource

  /**
   * Returns JSON Object containing information about this LRS, including the xAPI version
   * supported.
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<About>> getAbout() {

    return this.webClient

        .get()

        .uri(u -> u.path("/about").build())

        .retrieve()

        .toEntity(About.class);

  }

  // Enhanced features

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
   * Gets all of the Statements as a {@link StatementIterator}.
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
    return voidStatement(targetStatement, targetStatement.getActor());
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

    /**
     * Convenient method for transforming this StatementIterator to a {@link Stream}.
     *
     * @return a {@link Stream} of {@link Statement}s
     */
    public Stream<Statement> toStream() {
      final Iterable<Statement> iterable = () -> this;
      return StreamSupport.stream(iterable.spliterator(), false);
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
