/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import tools.jackson.annotation.JsonTypeInfo.As;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.exc.InvalidTypeIdException;
import tools.jackson.databind.jsontype.NamedType;
import tools.jackson.databind.jsontype.TypeDeserializer;
import tools.jackson.databind.jsontype.TypeIdResolver;
import tools.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import tools.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import tools.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Custom TypeResolverBuilder which accepts only valid strings as type identifiers.
 *
 * @author István Rátkai (Selindek)
 */
public class StrictObjectTypeResolverBuilder extends StdTypeResolverBuilder {

  @Override
  public TypeDeserializer buildTypeDeserializer(
      DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
    final var subTypeValidator = verifyBaseTypeValidity(config, baseType);
    final var idRes = idResolver(config, baseType, subTypeValidator, subtypes, false, true);
    final var defaultImpl = defineDefaultImpl(config, baseType);

    return new StrictObjectTypePropertyDeserializer(
        baseType, idRes, _typeProperty, _typeIdVisible, defaultImpl, _includeAs);
  }

  /**
   * Custom {@link AsPropertyTypeDeserializer} which accepts only valid objectType as property
   * value.
   */
  public static class StrictObjectTypePropertyDeserializer extends AsPropertyTypeDeserializer {

    private static final long serialVersionUID = 1139909729567678431L;

    private static final HashSet<String> validObjectTypes =
        new HashSet<>(
            Arrays.asList("Activity", "Agent", "Person", "Group", "SubStatement", "StatementRef"));

    /**
     * Constructor for StrictObjectTypePropertyDeserializer.
     *
     * @param baseType the base type
     * @param idRes the type id resolver
     * @param typeProperty the type property name
     * @param typeIdVisible whether type id is visible
     * @param defaultImpl the default implementation
     * @param includeAs the inclusion type
     */
    public StrictObjectTypePropertyDeserializer(
        JavaType baseType,
        TypeIdResolver idRes,
        String typeProperty,
        boolean typeIdVisible,
        JavaType defaultImpl,
        As includeAs) {
      super(baseType, idRes, typeProperty, typeIdVisible, defaultImpl, includeAs, true);
    }

    /**
     * Constructor for StrictObjectTypePropertyDeserializer.
     *
     * @param src the source deserializer
     * @param property the bean property
     */
    public StrictObjectTypePropertyDeserializer(
        AsPropertyTypeDeserializer src, BeanProperty property) {
      super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
      return (prop == _property) ? this : new StrictObjectTypePropertyDeserializer(this, prop);
    }

    @Override
    protected Object _deserializeTypedForId(
        JsonParser p, DeserializationContext ctxt, TokenBuffer tb, String typeId)
        throws IOException {

      // This is the actual custom logic.
      if (!validObjectTypes.contains(typeId)) {
        throw new InvalidTypeIdException(p, "Invalid objectType", _baseType, typeId);
      }
      // Everything else is just unavoidable duplication of the original code.

      return super._deserializeTypedForId(p, ctxt, tb, typeId);
    }
  }
}
