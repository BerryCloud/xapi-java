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
   * @param <T> the body type
   *
   * @param request The parameters of the get state request
   * 
   * @return the ResponseEntity
   */
  public <T> Mono<ResponseEntity<T>> getState(GetStateRequest<T> request, Class<T> body) {

    Map<String, Object> queryParams = new HashMap<>();

    return this.webClient

        .get()

        .uri(u -> request.url(u, queryParams).build(queryParams))

        .retrieve()

        .toEntity(body);

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
  public <T> Mono<ResponseEntity<T>> getState(Consumer<GetStateRequest.Builder<T, ?, ?>> request,
      Class<T> body) {

    final GetStateRequest.Builder<T, ?, ?> builder = GetStateRequest.builder();

    request.accept(builder);

    return getState(builder.build(), body);

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

        .delete()

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

        .delete()

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
