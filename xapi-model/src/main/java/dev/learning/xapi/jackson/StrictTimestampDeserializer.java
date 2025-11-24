/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import dev.learning.xapi.jackson.model.strict.XapiTimestamp;
import dev.learning.xapi.jackson.model.strict.XapiTimestamp.XapiTimestampParseException;
import java.io.IOException;
import java.time.Instant;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Strict Timestamp deserializer.
 *
 * @author István Rátkai (Selindek)
 */
public class StrictTimestampDeserializer extends StdDeserializer<Instant> {

  private static final long serialVersionUID = -2025907823340425934L;

  /** Default constructor. */
  public StrictTimestampDeserializer() {
    this(null);
  }

  /**
   * Constructor with value class.
   *
   * @param vc the value class
   */
  protected StrictTimestampDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * {@inheritDoc} Converts text to {@link java.time.Instant} using {@link XapiTimestamp}.
   *
   * @return {@link java.time.Instant} of text input
   */
  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

    try {
      return XapiTimestamp.parse(p.getText());
    } catch (final XapiTimestampParseException ex) {
      throw ctxt.instantiationException(handledType(), ex.getMessage());
    }
  }
}
