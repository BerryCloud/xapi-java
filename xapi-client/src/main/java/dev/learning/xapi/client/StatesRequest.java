package dev.learning.xapi.client;

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
  private final Agent agent;

  // TODO why not final? Because it is Optional?
  /**
   * The optional <strong>registration</strong> query parameter.
   */
  private UUID registration;

  @Override
  protected String getPath() {
    return "activities/state";
  }

  @Override
  protected void query(UriBuilder uriBuilder, Map<String, Object> variableMap) {
    uriBuilder

        .queryParam("activityId", "{activityId}")

        .queryParam("agent", "{agent}")

        .queryParamIfPresent("registration", Optional.ofNullable(registration));

    variableMap.put("activityId", activityId);
    variableMap.put("agent", agent);
  }

  public static abstract class Builder<T, C extends StatesRequest<T>, B extends StatesRequest.Builder<T, C, B>>
      extends XapiRequest.Builder<T, C, B> {

    /**
     * Consumer Builder for agent.
     *
     * @param account The Consumer Builder for agent.
     *
     * @return This builder
     *
     * @see StatesRequest#agent
     */
    public B agent(Consumer<Agent.Builder<?, ?>> account) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      account.accept(builder);

      return agent(builder.build());

    }

    /**
     * Sets the agent.
     *
     * @param account The Agent of the StatesRequest.
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
