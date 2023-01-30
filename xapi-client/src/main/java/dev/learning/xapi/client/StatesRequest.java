package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
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
abstract class StatesRequest<T> extends Request<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * The <strong>activityId</strong> query parameter.
   */
  @NonNull
  private final URI activityId;

  /**
   * The <strong>agent</strong> query parameter.
   */
  @NonNull
  private final Agent agent;

  /**
   * The optional <strong>registration</strong> query parameter.
   */
  private final UUID registration;

  @Override
  protected UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("activityId", activityId);
    queryParams.put("agent", agentToJsonString());

    return uriBuilder.path("activities/state")

        .queryParam("activityId", "{activityId}")

        .queryParam("agent", "{agent}")

        .queryParamIfPresent("registration", Optional.ofNullable(registration));
  }


  public String agentToJsonString() {

    try {
      return objectMapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      // Should not happen
      return null;
    }

  }


  public abstract static class Builder<T, C extends StatesRequest<T>,
      B extends StatesRequest.Builder<T, C, B>> extends Request.Builder<T, C, B> {

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent.
     *
     * @return This builder
     *
     * @see StatesRequest#agent
     */
    public B agent(Consumer<Agent.Builder<?, ?>> agent) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      agent.accept(builder);

      return agent(builder.build());

    }

    /**
     * Sets the agent.
     *
     * @param agent The Agent of the StatesRequest.
     *
     * @return This builder
     *
     * @see StatesRequest#agent
     */
    public B agent(Agent agent) {

      this.agent = agent;

      return self();

    }

    /**
     * Sets the activityId.
     *
     * @param activityId The activityId of the StatesRequest.
     *
     * @return This builder
     *
     * @see StatesRequest#activityId
     */
    public B activityId(String activityId) {

      this.activityId = URI.create(activityId);

      return self();

    }

    /**
     * Sets the activityId.
     *
     * @param activityId The activityId of the StatesRequest.
     *
     * @return This builder
     *
     * @see StatesRequest#activityId
     */
    public B activityId(URI activityId) {

      this.activityId = activityId;

      return self();

    }

    /**
     * Sets the registration.
     *
     * @param registration The registration of the StatesRequest.
     *
     * @return This builder
     *
     * @see StatesRequest#registration
     */
    public B registration(String registration) {

      this.registration = UUID.fromString(registration);

      return self();

    }

    /**
     * Sets the registration.
     *
     * @param registration The registration of the StatesRequest.
     *
     * @return This builder
     *
     * @see StatesRequest#registration
     */
    public B registration(UUID registration) {

      this.registration = registration;

      return self();

    }

  }

}
