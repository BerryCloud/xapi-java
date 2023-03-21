/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This enumeration class represents all valid xAPI object types.
 *
 * @author István Rátkai (Selindek)
 */
public enum ObjectType {

  /**
   * Activity object type.
   */
  @JsonProperty("Activity")
  ACTIVITY,

  /**
   * Agent object type.
   */
  @JsonProperty("Agent")
  AGENT,

  /**
   * Person object type.
   * <p>
   * <strong>Note:</strong> Version 0.9 statements use the person object type.
   * </p>
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#Appendix3A">Converting
   *      Statements to 1.0.0</a>
   */
  @JsonProperty("Person")
  PERSON,

  /**
   * Group object type.
   */
  @JsonProperty("Group")
  GROUP,

  /**
   * SubStatement object type.
   */
  @JsonProperty("SubStatement")
  SUBSTATEMENT,

  /**
   * StatementRef object type.
   */
  @JsonProperty("StatementRef")
  STATEMENTREF;

  // **Warning** do not add fields that are not required by the xAPI specification.
}
