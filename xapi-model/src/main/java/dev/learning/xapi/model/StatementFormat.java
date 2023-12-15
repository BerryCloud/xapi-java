/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import java.io.Serializable;

/**
 * This enumeration class represents all valid Statement formats.
 *
 * @author Thomas Turrell-Croft
 */
public enum StatementFormat implements Serializable {

  IDS("ids"),

  EXACT("exact"),

  CANONICAL("canonical");

  private final String format;

  StatementFormat(String format) {
    this.format = format;
  }

  public String getFormat() {
    return format;
  }

  @Override
  public String toString() {
    return format;
  }

  // **Warning** do not add fields that are not required by the xAPI specification.
}
