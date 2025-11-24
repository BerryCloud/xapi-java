/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import tools.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the xAPI Agent object.
 *
 * @author Thomas Turrell-Croft
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#agent">xAPI Agent</a>
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(value = {"firstName", "lastName"})
public class Agent extends Actor {

  private AgentObjectType objectType;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /** Builder for Agent. */
  public abstract static class Builder<C extends Agent, B extends Builder<C, B>>
      extends Actor.Builder<C, B> {

    // This static class extends the lombok builder.

  }

  /**
   * This enumeration class represents the optional xAPI Agent object type.
   *
   * @author István Rátkai (Selindek)
   */
  public enum AgentObjectType {

    /** Agent object type. */
    @JsonProperty("Agent")
    AGENT;
  }
}
