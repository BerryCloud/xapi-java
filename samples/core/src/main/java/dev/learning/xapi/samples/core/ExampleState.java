/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.core;

import java.time.Instant;

/**
 * Class which can be serialized and deserialized by Jackson.
 *
 * @author Thomas Turrell-Croft
 */
public class ExampleState {

  private String message;
  private Instant timestamp;

  /** Constructor for ExampleState. */
  public ExampleState(String message, Instant timestamp) {
    super();
    this.message = message;
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "ExampleState [message=" + message + ", timestamp=" + timestamp + "]";
  }
}
