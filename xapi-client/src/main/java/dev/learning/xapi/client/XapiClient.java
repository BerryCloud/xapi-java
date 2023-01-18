package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Actor;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
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
 * @author Thomas Turrell-Croft
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI
 *      communication resources</a>
 */
@Slf4j
public class XapiClient {

  private final WebClient client;
  private final ObjectMapper objectMapper;

  /**
   * Default constructor for XapiClient.
   *
   * @param builder      a {@link WebClient.Builder} object. The caller must set the baseUrl and the
   *                     authorization header.
   * @param objectMapper an {@link ObjectMapper}. It is used for converting {@link Actor} query
   *                     parameters to JSON string during xAPI requests.
   */
  public XapiClient(WebClient.Builder builder, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.client = builder

        .defaultHeader("X-Experience-API-Version", "1.0.3")

        .build();
  }

  /**
   * Sends an xAPI request.
   *
   * @param <T>     The response type is defined by the <i>request</i> parameter.
   * @param request an {@link XapiRequest} object describing the xAPI request.
   * @return a {@link ResponseEntity} containing the response object defined by the <i>request</i>
   *         parameter.
   */
  @SuppressWarnings("unchecked")
  public <T> ResponseEntity<T> send(XapiRequest<T> request) {
    return _send(request,
        (Class<T>) GenericTypeResolver.resolveTypeArgument(request.getClass(), XapiRequest.class));
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
   * @param <T>          The response type is defined by the <i>responseType</i> parameter.
   * @param request      an {@link GetStateRequest} object.
   * @param responseType a {@link Class} object defining the response type of the returning state.
   * @return a {@link ResponseEntity} containing the response object .
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#description-4">State
   *      Resources Description</a>
   * @throws RuntimeException when the returned state cannot be converted to the expected JAVA
   *                          class.
   */
  public <T> ResponseEntity<T> send(GetStateRequest request, Class<T> responseType) {
    return _send(request, responseType);
  }

  private <T> ResponseEntity<T> _send(XapiRequest<?> request, Class<T> responseType) {

    final RequestBodySpec r = client

        .method(request.getMethod())

        .uri(uriBuilder -> {
          final var variableMap = new HashMap<String, Object>();
          request.query(uriBuilder, variableMap);
          convertActors(variableMap);
          return uriBuilder.path(request.getPath()).build(variableMap);
        })

        .headers(headers -> request.headers(headers))

    ;

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

  private void convertActors(HashMap<String, Object> variableMap) {
    variableMap.entrySet().stream().filter(s -> s.getValue() instanceof Actor).forEach(s -> {
      try {
        s.setValue(objectMapper.writeValueAsString(s.getValue()));
      } catch (final JsonProcessingException e) {
        // Should not happen
      }
    });

  }
}
