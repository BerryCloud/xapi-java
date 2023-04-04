/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Optional;

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
  ACTIVITY("Activity"),

  /**
   * Agent object type.
   */
  @JsonProperty("Agent")
  AGENT("Agent"),

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
  PERSON("Person"),

  /**
   * Group object type.
   */
  @JsonProperty("Group")
  GROUP("Group"),

  /**
   * SubStatement object type.
   */
  @JsonProperty("SubStatement")
  SUBSTATEMENT("SubStatement"),

  /**
   * StatementRef object type.
   */
  @JsonProperty("StatementRef")
  STATEMENTREF("StatementRef");

  private String value;

  ObjectType(String value) {
    this.value = value;
  }

  public static Optional<ObjectType> getByValue(String url) {
    return Arrays.stream(ObjectType.values()).filter(env -> env.value.equals(url)).findFirst();
  }

  // **Warning** do not add fields that are not required by the xAPI specification.
}
