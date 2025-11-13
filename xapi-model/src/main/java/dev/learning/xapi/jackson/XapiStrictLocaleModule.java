/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.learning.xapi.jackson.StrictLocaleDeserializer.StrictLocaleKeyDeserializer;
import java.util.Locale;

/**
 * xAPI JSON module for registering custom deserializer {@link Locale} objects.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiStrictLocaleModule extends SimpleModule {

  private static final long serialVersionUID = 8667729487482112691L;

  /**
   * XapiStrictLocaleModule constructor. Adds custom {@link StrictLocaleDeserializer} to the
   * ObjectMapper.
   */
  public XapiStrictLocaleModule() {
    super("xApi Strict Locale Module");

    addDeserializer(Locale.class, new StrictLocaleDeserializer());
    addKeyDeserializer(Locale.class, new StrictLocaleKeyDeserializer());

  }
}
