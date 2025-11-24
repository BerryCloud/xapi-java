/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

/**
 * Abstract superclass of xAPI state resource request.
 *
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#23-state-resource">State
 *     Resource</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
abstract class StatesRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /** The <strong>activityId</strong> query parameter. */
  @NonNull private final URI activityId;

  /** The <strong>agent</strong> query parameter. */
  @NonNull private final Agent agent;

  /** The optional <strong>registration</strong> query parameter. */
  private final UUID registration;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("activityId", activityId);
    queryParams.put("agent", agentToJsonString());

    return uriBuilder
        .path("/activities/state")
        .queryParam("activityId", "{activityId}")
        .queryParam("agent", "{agent}")
        .queryParamIfPresent("registration", Optional.ofNullable(registration));
  }

  // Exception in write value as string should be impossible.
  @SneakyThrows
  private String agentToJsonString() {

    return objectMapper.writeValueAsString(agent);
  }

  /** Builder for StatesRequest. */
  public abstract static class Builder<C extends StatesRequest, B extends Builder<C, B>> {

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent.
     * @return This builder
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
     * @return This builder
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
     * @return This builder
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
     * @return This builder
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
     * @return This builder
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
     * @return This builder
     * @see StatesRequest#registration
     */
    public B registration(UUID registration) {

      this.registration = registration;

      return self();
    }
  }
}
