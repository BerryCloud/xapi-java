/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.jackson.model.strict.XapiTimestamp;
import java.time.Instant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * <p>
 * InstantConverter class.
 * </p>
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Component
public class InstantConverter implements Converter<String, Instant> {

  /**
   * Converts string to {@link java.time.Instant}. If the timezone is not specified in string, UTC
   * will be used.
   *
   * @param source the String representation of the datetime in ISO 8601 format (e.q.
   *        '2011-12-03T10:15:30+01:00')
   *
   * @return {@link java.time.Instant} of source input
   */
  @Override
  public Instant convert(String source) {

    return XapiTimestamp.parse(source);
  }

}
