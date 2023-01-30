package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Actor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
@Slf4j
public class XapiClient {

  private final WebClient client;

  /**
   * Default constructor for XapiClient.
   *
   * @param builder a {@link WebClient.Builder} object. The caller must set the baseUrl and the
   *        authorization header.
   * @param objectMapper an {@link ObjectMapper}. It is used for converting {@link Actor} query
   *        parameters to JSON string during xAPI requests.
   */
  public XapiClient(WebClient.Builder builder) {
    this.client = builder

        .defaultHeader("X-Experience-API-Version", "1.0.3")

        .build();
  }



  /**
   * Sends an xAPI request.
   *
   * @param <T> The response type is defined by the <i>request</i> parameter.
   * @param request an {@link XapiRequest} object describing the xAPI request.
   * @return a {@link ResponseEntity} containing the response object defined by the <i>request</i>
   *         parameter.
   */
  /*
   * public <T> ResponseEntity<T> send(Request<T> request) {
   * 
   * return sendRequest(request, request.getResponseType()); }
   */

  public <C extends PutStateRequest,
      B extends PutStateRequest.Builder<C, B>> ResponseEntity<Void> putState(
          Consumer<PutStateRequest.Builder<?, ?>> putStateRequest) {

    final PutStateRequest.Builder<?, ?> builder = PutStateRequest.builder();

    putStateRequest.accept(builder);

    return send(builder);
  }



  public <C extends PostStateRequest,
      B extends PostStateRequest.Builder<C, B>> ResponseEntity<Void> postState(
          Consumer<PostStateRequest.Builder<?, ?>> postStateRequest) {

    final PostStateRequest.Builder<?, ?> builder = PostStateRequest.builder();

    postStateRequest.accept(builder);

    return send(builder);

  }



  public <C extends DeleteStateRequest,
      B extends DeleteStateRequest.Builder<C, B>> ResponseEntity<Void> deleteState(
          Consumer<DeleteStateRequest.Builder<?, ?>> deleteStateRequest) {

    final DeleteStateRequest.Builder<?, ?> builder = DeleteStateRequest.builder();

    deleteStateRequest.accept(builder);

    return send(builder);

  }


  public <C extends DeleteStatesRequest,
      B extends DeleteStatesRequest.Builder<C, B>> ResponseEntity<Void> deleteStates(
          Consumer<DeleteStatesRequest.Builder<?, ?>> deleteStatesRequest) {

    final DeleteStatesRequest.Builder<?, ?> builder = DeleteStatesRequest.builder();

    deleteStatesRequest.accept(builder);

    return send(builder);

  }



  public <C extends GetStateRequest<T>, B extends GetStateRequest.Builder<T, C, B>,
      T> ResponseEntity<?> getState(Consumer<GetStateRequest.Builder<T, ?, ?>> getStateRequest) {

    final GetStateRequest.Builder<T, ?, ?> builder = GetStateRequest.builder();

    getStateRequest.accept(builder);

    return send(builder);

  }

  public <C extends GetStatesRequest<List<T>>, B extends GetStatesRequest.Builder<List<T>, C, B>,
      T> ResponseEntity<?> getStates(
          Consumer<GetStatesRequest.Builder<List<T>, ?, ?>> getStatesRequest) {

    final GetStatesRequest.Builder<List<T>, ?, ?> builder = GetStatesRequest.builder();

    getStatesRequest.accept(builder);

    return send(builder);

  }



  private <C extends Request<T>, B extends Request.Builder<T, C, B>,
      T> ResponseEntity<T> send(Request.Builder<T, C, B> builder) {

    var request = builder.build();

    return sendRequest(request, request.getResponseType());

  }


  /**
   * <p>
   * Convenient type-safe method for sending a {@link GetStateRequest} which expects an instance of
   * a given JAVA class as a response.
   * </p>
   * <p>
   * The {@link GetStateRequest} is the only xAPI request where the type of the response is not
   * defined. Learning Record Providers can store ANY kind of data here. The type conversion of the
   * returned state object happens based on the <strong>Content-Type</strong> header provided when
   * the state was stored. If the stored state is incompatible with the
   * <strong>Content-Type</strong> header or it cannot be converted to the expected response type
   * then a {@link RuntimeException} is thrown.
   * </p>
   * <p>
   * If the generic {@link XapiClient#send(XapiRequest)} method is used with {@link GetStateRequest}
   * request then the state is returned as a String.
   * </p>
   *
   * @param <T> The response type is defined by the <i>responseType</i> parameter.
   * @param request an {@link GetStateRequest} object.
   * @param responseType a {@link Class} object defining the response type of the returning state.
   * @return a {@link ResponseEntity} containing the response object .
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#description-4">State
   *      Resources Description</a>
   * @throws RuntimeException when the returned state cannot be converted to the expected JAVA
   *         class.
   */
  public <T> ResponseEntity<T> send(GetStateRequest<?> request, @NonNull Class<T> responseType) {
    return sendRequest(request, responseType);
  }

  private <T> ResponseEntity<T> sendRequest(Request<?> request, @NonNull Class<T> responseType) {

    // UriSpec<RequestBodySpec> uriSpec = client.method(request.getMethod());

    // RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> request.url(uriBuilder).build());

    // RequestHeadersSpec<?> headersSpec = bodySpec.h

    // ResponseSpec responseSpec = headersSpec.header

    Map<String, Object> queryParams = new HashMap<>();

    final RequestBodySpec r = client

        .method(request.getMethod())

        .uri(uriBuilder -> request.url(uriBuilder, queryParams).build(queryParams))

        .headers(headers -> new HttpHeaders());

    final var body = request.getBody();

    if (body != null) {
      r.bodyValue(body);
    }

    return r.retrieve().toEntity(responseType)

        .onErrorResume(WebClientResponseException.class, ex -> {
          if (ex.getStatusCode().value() == 404) {
            return Mono.just(new ResponseEntity<T>(HttpStatusCode.valueOf(404)));
          }
          log.warn("Unsuccessful request: ", ex);
          log.debug(ex.getResponseBodyAsString());
          return Mono.error(ex);
        })

        .block();
  }



}
