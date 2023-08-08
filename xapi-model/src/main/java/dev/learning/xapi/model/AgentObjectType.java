/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This enumeration class represents the optional xAPI Agent object type.
 *
 * @author István Rátkai (Selindek)
 */
public enum AgentObjectType {

  /**
   * Agent object type.
   */
  @JsonProperty("Agent")
  AGENT;

}
