/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Deserialization Modifier for restricting <code>null</code> literals in Statements.
 *
 * @author István Rátkai (Selindek)
 */
public class NotNullDeserializationModifier extends BeanDeserializerModifier {

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
      BeanDescription beanDesc, JsonDeserializer<?> deserializer) {

    // Use standard deserializer on explicit Object class. it happens only in Extensions
    if (beanDesc.getBeanClass() == Object.class) {
      return deserializer;
    }
    return new NotNullDeserializer<>(deserializer, beanDesc.getBeanClass());
  }

  private static class NotNullDeserializer<T> extends StdDeserializer<T>
      implements ResolvableDeserializer {

    private static final long serialVersionUID = -3153072039006440049L;
    // This field is not Serializable, but this class is never serialized.
    private final JsonDeserializer<T> defaultDeserializer; // NOSONAR

    private NotNullDeserializer(JsonDeserializer<T> defaultDeserializer, Class<?> vc) {
      super(vc);
      this.defaultDeserializer = defaultDeserializer;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return defaultDeserializer.deserialize(p, ctxt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getNullValue(DeserializationContext ctxt) throws JsonMappingException {
      throw ctxt.instantiationException(defaultDeserializer.handledType(),
          "null literal is not allowed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getAbsentValue(DeserializationContext ctxt) throws JsonMappingException {
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
      if (defaultDeserializer instanceof final ResolvableDeserializer resolvableDeserializer) {
        resolvableDeserializer.resolve(ctxt);
      }
    }

  }
}
