package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.util.Map;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

@SuperBuilder
abstract class AgentProfileRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @NonNull
  private Agent agent;

  @NonNull
  private String profileId;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("agent", agentToJsonString());
    queryParams.put("profileId", profileId);

    return uriBuilder.path("/agents/profile").queryParam("agent", "{agent}").queryParam(profileId,
        "{profileId}");
  }

  private String agentToJsonString() {

    try {
      return objectMapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      // Should not happen
      return null;
    }

  }

  /**
   * Builder for AgentProfileRequest.
   */
  public abstract static class Builder<C extends AgentProfileRequest, B extends Builder<C, B>> {

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent.
     *
     * @return This builder
     *
     * @see AgentProfileRequest#agent
     */
    public B agent(Consumer<Agent.Builder<?, ?>> agent) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      agent.accept(builder);

      return agent(builder.build());

    }

    /**
     * Sets the agent.
     *
     * @param agent The Agent of the AgentProfileRequest.
     *
     * @return This builder
     *
     * @see AgentProfileRequest#agent
     */
    public B agent(Agent agent) {

      this.agent = agent;

      return self();

    }

  }

}
