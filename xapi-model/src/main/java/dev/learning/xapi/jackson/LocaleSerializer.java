/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Specific Locale serializer using {@link Locale#toLanguageTag()} instead of
 * {@link Locale#toString()}.
 *
 * @author István Rátkai (Selindek)
 */
public class LocaleSerializer extends StdSerializer<Locale> {

  private static final long serialVersionUID = 7182941951585541965L;

  public LocaleSerializer() {
    super(Locale.class);
  }

  @Override
  public void serialize(Locale value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeString(value.toLanguageTag());
  }

  /**
   * <p>
   * Locale Key Serializer.
   * </p>
   * For serializing Locale keys in {@link Map}s
   *
   * @author István Rátkai (Selindek)
   */
  public static class LocaleKeySerializer extends StdSerializer<Locale> {

    private static final long serialVersionUID = 7182941951585541965L;

    public LocaleKeySerializer() {
      super(Locale.class);
    }

    @Override
    public void serialize(Locale value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      gen.writeFieldName(value.toLanguageTag());
    }

  }

}
