package dev.learning.xapi.client;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.extern.slf4j.Slf4j;

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

  public XapiClient(WebClient.Builder builder) {
    this.client = builder
        
        .defaultHeader("X-Experience-API-Version", "1.0.3")
        
        .build();
  }

  public <T> ResponseEntity<T> send(XapiRequest<T> request) {
    return request.execute(client)
        
        .onErrorResume(WebClientResponseException.class,
            ex -> {
              if(ex.getStatusCode().value() == 404) {
                return Mono.just( new ResponseEntity<T>(HttpStatusCode.valueOf(404)));
              }
              log.warn("Unsuccessful request: ", ex);
              log.debug(ex.getResponseBodyAsString());
              return Mono.error(ex);
            })
        
    .block();
  }
}
