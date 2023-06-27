/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.learning.xapi.jackson.model.strict.XapiTimestamp;
import java.io.IOException;
import java.time.Instant;

/**
 * Strict Timestamp deserializer.
 *
 * @author István Rátkai (Selindek)
 */
public class StrictTimestampDeserializer extends StdDeserializer<Instant> {

  private static final long serialVersionUID = -2025907823340425934L;

  public StrictTimestampDeserializer() {
    this(null);
  }

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

    return XapiTimestamp.parse(p.getText());
  }

}
