/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.Agent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/** Binds JSON agent request parameters using the configured xAPI mapper. */
@Component
public class AgentConverter implements Converter<String, Agent> {

  private final ObjectMapper mapper;

  /**
   * Create the agent converter.
   *
   * @param mapper the configured xAPI mapper
   */
  public AgentConverter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Deserialize a JSON agent for request parameter binding.
   *
   * @param source the JSON agent
   * @return the deserialized agent
   * @throws JacksonException if the value cannot be deserialized as an agent
   * @throws IllegalArgumentException if the JSON value is null
   */
  @Override
  public Agent convert(String source) {
    Agent agent = mapper.readValue(source, Agent.class);
    if (agent == null) {
      throw new IllegalArgumentException("agent must be a JSON Agent");
    }
    return agent;
  }
}
