/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson.model.strict;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Utility class for parsing ISO 8601 timestamps according to the strict xAPI rules.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiTimestamp {

  private XapiTimestamp() {
    // Should not be instantiated
  }

  /**
   * {@inheritDoc} Converts ISO 8601 string to {@link java.time.Instant}.
   *
   * @return {@link java.time.Instant} of text input
   */
  public static Instant parse(String text) {
    // Negative zero offset is not permitted in ISO 8601 however it is permitted in RFC 3339 which
    // considers the negative zero to indicate that the offset is unknown.
    // The xAPI specification states that timestamps should be formatted per RFC 3339 however the
    // conformance tests fail if negative zero offsets are accepted.
    if (text.endsWith("-00") || text.endsWith("-0000") || text.endsWith("-00:00")) {
      throw new XapiTimestampParseException("Negative timezone offset can not be zero");
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

  public static class XapiTimestampParseException extends RuntimeException {

    private static final long serialVersionUID = -3097189442067704841L;

    XapiTimestampParseException(String message) {

      super(message);
    }

  }


}
