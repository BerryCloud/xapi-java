/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonMappingException;
import tools.jackson.databind.KeyDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Strict Locale deserializer.
 *
 * @author István Rátkai (Selindek)
 */
public class StrictLocaleDeserializer extends StdDeserializer<Locale> {

  private static final long serialVersionUID = 7182941951585541965L;

  /** Default constructor. */
  public StrictLocaleDeserializer() {
    super(String.class);
  }

  /** {@inheritDoc} */
  @Override
  public Locale deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    if (jsonParser.getCurrentToken() != JsonToken.VALUE_STRING) {
      throw deserializationContext.wrongTokenException(
          jsonParser,
          (JavaType) null,
          JsonToken.VALUE_STRING,
          "Attempted to parse non-String value to Locale");
    }

    return validateLocale(jsonParser.getValueAsString(), deserializationContext);
  }

  static Locale validateLocale(String localeString, DeserializationContext deserializationContext)
      throws JsonMappingException {

    var locale = Locale.forLanguageTag(localeString);

    try {
      // test validity of language and country codes (throws exception)
      locale.getISO3Language();
      locale.getISO3Country();
    } catch (final MissingResourceException e) {
      locale = null;
    }
    // test the validity of the whole key
    if (locale == null || !locale.toLanguageTag().equalsIgnoreCase(localeString)) {
      throw deserializationContext.weirdStringException(
          localeString, Locale.class, "Invalid locale");
    }
    return locale;
  }

  /**
   * Strict Locale Key deserializer.
   *
   * @author István Rátkai (Selindek)
   */
  public static class StrictLocaleKeyDeserializer extends KeyDeserializer {

    /** {@inheritDoc} */
    @Override
    public Object deserializeKey(String key, DeserializationContext deserializationContext)
        throws IOException {

      return validateLocale(key, deserializationContext);
    }
  }
}
