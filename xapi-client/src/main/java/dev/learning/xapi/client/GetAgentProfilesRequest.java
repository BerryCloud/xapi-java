package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting multiple agent profiles.
 *
 * <p>
 * TODO add see
 * </p>
 *
 * @author Thomas Turrell-Croft
 */
@Getter
@Builder
public class GetAgentProfilesRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @NonNull
  private Agent agent;

  private Instant since;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("agent", agentToJsonString());

    if (since != null) {
      queryParams.put("since", since);
      uriBuilder.queryParam("since", "{since}");
    }

    return uriBuilder.path("/agents/profile").queryParam("agent", "{agent}");
  }

  @Override
  public HttpMethod getMethod() {

    return HttpMethod.GET;
  }

  /**
   * Builder for GetAgentProfilesRequest.
   */
  public static class Builder {

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent.
     *
     * @return This builder
     *
     * @see GetAgentProfilesRequest#agent
     */
    public Builder agent(Consumer<Agent.Builder<?, ?>> agent) {

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
     * @see GetAgentProfilesRequest#agent
     */
    public Builder agent(Agent agent) {

      this.agent = agent;

      return this;

    }

  }

  private String agentToJsonString() {

    try {
      return objectMapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      // Should not happen
      return null;
    }

  }

}
