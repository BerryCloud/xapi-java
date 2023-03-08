/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Return a special, Person Object for a specified Agent. The Person Object is very similar to an
 * Agent Object, but instead of each attribute having a single value, each attribute has an array
 * value, and it is legal to include multiple identifying properties.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#24-agents-resource">Agents
 *      Resource</a>
 *
 * @author Thomas Turrell-Croft
 */
@Builder
public class GetAgentsRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * The Agent representation to use in fetching expanded Agent information.
   */
  @NonNull
  private Agent agent;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("agent", agentToJsonString());

    return uriBuilder.path("/agents").queryParam("agent", "{agent}");
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  /**
   * Builder for GetAgentsRequest.
   */
  public static class Builder {

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent.
     *
     * @return This builder
     *
     * @see GetAgentsRequest#agent
     */
    public Builder agent(Consumer<Agent.Builder<?, ?>> agent) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      agent.accept(builder);

      return agent(builder.build());

    }

    /**
     * Sets the agent.
     *
     * @param agent The Agent of the GetAgentsRequest.
     *
     * @return This builder
     *
     * @see GetAgentsRequest#agent
     */
    public Builder agent(Agent agent) {

      this.agent = agent;

      return this;

    }

  }


  // Exception in write value as string should be impossible.
  @SneakyThrows
  private String agentToJsonString() {

    return objectMapper.writeValueAsString(agent);

  }


}
