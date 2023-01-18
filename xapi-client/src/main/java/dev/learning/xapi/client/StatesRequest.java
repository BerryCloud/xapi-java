package dev.learning.xapi.client;

import dev.learning.xapi.model.Actor;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

/**
 * Abstract superclass of xAPI state resource request.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#23-state-resource">State
 *      Resource</a>
 * @author István Rátkai (Selindek)
 * @param <T> The response type of the request
 */
@SuperBuilder
@Getter
abstract class StatesRequest<T> extends XapiRequest<T> {

  /**
   * The <strong>activityId</strong> query parameter.
   */
  @NonNull
  private final URI activityId;

  /**
   * The <strong>agent</strong> query parameter.
   */
  @NonNull
  private final Actor agent;

  /**
   * The optional <strong>registration</strong> query parameter.
   */
  @Default
  private Optional<UUID> registration = Optional.empty();

  @Override
  protected String getPath() {
    return "activities/state";
  }

  @Override
  protected void query(UriBuilder uriBuilder, Map<String, Object> variableMap) {
    uriBuilder

        .queryParam("activityId", "{activityId}")

        .queryParam("agent", "{agent}")

        .queryParamIfPresent("registration", registration);

    variableMap.put("activityId", activityId);
    variableMap.put("agent", agent);
  }
}
