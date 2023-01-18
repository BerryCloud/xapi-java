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
   * @param <T>     The response type is defined by the request parameter.
   * @param request an {@link XapiRequest} object describing the xAPI request.
   * @return a {@link ResponseEntity} containing the response object defined by the <i>request</i>
   *         parameter.
   */
  @SuppressWarnings("unchecked")
  public <T> ResponseEntity<T> send(XapiRequest<T> request) {

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

    return r.retrieve().toEntity(
        (Class<T>) GenericTypeResolver.resolveTypeArgument(request.getClass(), XapiRequest.class))

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
