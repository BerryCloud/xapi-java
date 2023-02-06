package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Actor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for communicating with LRS or service which implements some of the xAPI communication
 * resources.
 *
 * @author István Rátkai (Selindek)
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
   * @param objectMapper an {@link ObjectMapper}. It is used for converting {@link Actor} query
   *        parameters to JSON string during xAPI requests.
   */
  public XapiClient(WebClient.Builder builder) {
    this.webClient = builder

        .defaultHeader("X-Experience-API-Version", "1.0.3")

        .build();
  }

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * The returned ResponseEntity contains the response headers and body.
   * 
   * @param <T>
   * 
   * @param <T>
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
   * The returned ResponseEntity contains the response headers and body.
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

  public <T> Mono<ResponseEntity<String[]>> getStates(GetStatesRequest request) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .method(request.getMethod())

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(String[].class);

  }

  public <T> Mono<ResponseEntity<String[]>> getStates(
      Consumer<GetStatesRequest.Builder<?, ?>> request) {

    final GetStatesRequest.Builder<?, ?> builder = GetStatesRequest.builder();

    request.accept(builder);

    return getStates(builder.build());

  }

  /**
   * Posts a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * The returned ResponseEntity contains the response headers and body.
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
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * The returned ResponseEntity contains the response headers and body.
   * 
   * @param request The Consumer Builder for the get state request
   *
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> postState(Consumer<PostStateRequest.Builder<?, ?>> request) {

    final PostStateRequest.Builder<?, ?> builder = PostStateRequest.builder();

    request.accept(builder);

    return postState(builder.build());

  }

  /**
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * The returned ResponseEntity contains the response headers and body.
   * 
   * @param <T>
   * 
   * @param <T>
   * 
   * @param request The parameters of the get state request
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
   * Gets a single document specified by the given stateId activity, agent, and optional
   * registration.
   *
   * The returned ResponseEntity contains the response headers and body.
   * 
   * @param request The Consumer Builder for the get state request
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
   * The returned ResponseEntity contains the response headers.
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
   * The returned ResponseEntity contains the response headers.
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
   * Deletes all documents specified by the given activityId, agent and optional registration.
   * 
   * The returned ResponseEntity contains the response headers.
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
   * The returned ResponseEntity contains the response headers.
   * 
   * @param request The Consumer Builder for the delete state request
   * 
   * @return the ResponseEntity
   */
  public Mono<ResponseEntity<Void>> deleteStates(
      Consumer<DeleteStatesRequest.Builder<?, ?>> deleteStatesRequest) {

    final DeleteStatesRequest.Builder<?, ?> builder = DeleteStatesRequest.builder();

    deleteStatesRequest.accept(builder);

    return deleteStates(builder.build());

  }

}
