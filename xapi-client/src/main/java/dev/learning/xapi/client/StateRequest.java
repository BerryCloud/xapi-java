package dev.learning.xapi.client;

import java.util.Map;
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
  protected void query(UriBuilder uriBuilder, Map<String, Object> variableMap) {
    super.query(uriBuilder, variableMap);
    uriBuilder.queryParam("stateId", "{stateId}");
    variableMap.put("stateId", stateId);
  }
}