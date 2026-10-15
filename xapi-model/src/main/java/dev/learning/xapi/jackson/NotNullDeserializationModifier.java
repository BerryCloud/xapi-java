/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.ValueDeserializerModifier;
import tools.jackson.databind.deser.std.DelegatingDeserializer;
import tools.jackson.databind.util.AccessPattern;

/**
 * Deserialization Modifier for restricting <code>null</code> literals in Statements.
 *
 * @author István Rátkai (Selindek)
 */
public class NotNullDeserializationModifier extends ValueDeserializerModifier {

  /** {@inheritDoc} */
  @Override
  public ValueDeserializer<?> modifyDeserializer(
      DeserializationConfig config,
      BeanDescription.Supplier beanDescRef,
      ValueDeserializer<?> deserializer) {

    final var beanDesc = beanDescRef.get();
    // Use standard deserializer on explicit Object class. it happens only in Extensions
    if (beanDesc.getBeanClass() == Object.class) {
      return deserializer;
    }
    return new NotNullDeserializer(deserializer);
  }

  private static class NotNullDeserializer extends DelegatingDeserializer {

    private static final long serialVersionUID = -3153072039006440049L;

    private NotNullDeserializer(ValueDeserializer<?> delegate) {
      super(delegate);
    }

    @Override
    protected ValueDeserializer<?> newDelegatingInstance(ValueDeserializer<?> delegate) {
      return new NotNullDeserializer(delegate);
    }

    @Override
    public AccessPattern getNullAccessPattern() {
      return AccessPattern.DYNAMIC;
    }

    @Override
    public Object getNullValue(DeserializationContext ctxt) throws DatabindException {
      throw ctxt.instantiationException(handledType(), "null literal is not allowed");
    }

    @Override
    public Object getAbsentValue(DeserializationContext ctxt) {
      return null;
    }
  }
}
