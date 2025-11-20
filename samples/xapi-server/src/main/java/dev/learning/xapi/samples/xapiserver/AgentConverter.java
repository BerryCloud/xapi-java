/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converter for deserializing Agent from JSON string in request parameters.
 *
 * @author Thomas Turrell-Croft
 */
@Component
public class AgentConverter implements Converter<String, Agent> {

  private final ObjectMapper objectMapper;

  public AgentConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Agent convert(String source) {
    try {
      return objectMapper.readValue(source, Agent.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid agent JSON: " + source, e);
    }
  }
}
