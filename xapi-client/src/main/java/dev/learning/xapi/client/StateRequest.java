package dev.learning.xapi.client;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

/**
 * Abstract superclass for state requests manipulating a single state document.
 *
 * @author István Rátkai (Selindek)
 * @param <T> The response type of the request
 */
@SuperBuilder
@Getter
abstract class StateRequest<T> extends StatesRequest<T> {

  /**
   * The <strong>stateId</strong> query parameter.
   */
  @NonNull
  private final String stateId;


  @Override
  protected URI query(UriBuilder uriBuilder, Map<String, ?> uriVaribles) {

    // Map<String, Object> variableMap

    return uriBuilder

        .queryParam("activityId", "{activityId}")

        .queryParam("agent", "{agent}")

        .queryParamIfPresent("registration", Optional.ofNullable(registration))

        .queryParam("stateId", "{stateId}")

        .build(uriVaribles);

    // variableMap.put("activityId", activityId);
    // variableMap.put("agent", agent);
  }

}
