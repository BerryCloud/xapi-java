/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimestampDeserializer extends StdDeserializer<Instant> {

  private static final long serialVersionUID = -2025907823340425934L;

  public TimestampDeserializer() {
    this(null);
  }

  protected TimestampDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * {@inheritDoc} Converts text to {@link java.time.Instant} using {@link Timestamp}.
   *
   * @return {@link java.time.Instant} of text input
   */
  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

    final var text = p.getText();

    // Negative zero offset is not permitted in ISO 8601 however it is permitted in RFC 3339 which
    // considers the negative zero to indicate that the offset is unknown.
    // The xAPI specification states that timestamps SHOULD be formatted per RFC 3339 however the
    // conformance tests fail if negative zero offsets are accepted.
    if (text.endsWith("-00") || text.endsWith("-0000") || text.endsWith("-00:00")) {
      throw ctxt.instantiationException(handledType(), "Negative timezone offset can not be zero");
    }

    // Permit +00:00, +0000 and +00
    final var formatter =
        new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendPattern("[XXXXX][XXXX][X]").toFormatter();

    final var dt = formatter.parseBest(text, Instant::from, LocalDateTime::from);

    if (dt instanceof final Instant instant) {
      return instant;
    } else {
      return Instant.from(ZonedDateTime.of((LocalDateTime) dt, ZoneOffset.UTC));
    }

  }

}
