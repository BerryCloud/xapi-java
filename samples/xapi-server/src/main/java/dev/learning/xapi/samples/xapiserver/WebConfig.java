/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration for the xAPI server.
 * 
 * <p>
 * Configures custom converters for HTTP request parameters, including the timestamp converter
 * which handles ISO 8601 formatted timestamps with strict xAPI validation.
 * </p>
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final InstantConverter instantConverter;

  public WebConfig(InstantConverter instantConverter) {
    this.instantConverter = instantConverter;
  }

  /**
   * Registers custom converters for HTTP parameter binding.
   * 
   * <p>
   * The {@link InstantConverter} is explicitly registered to handle conversion of ISO 8601
   * timestamp strings to {@link java.time.Instant} objects. This converter enforces strict xAPI
   * compliance, including proper timezone handling and rejection of negative zero offsets.
   * </p>
   *
   * @param registry the formatter registry to add converters to
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(instantConverter);
  }
}
