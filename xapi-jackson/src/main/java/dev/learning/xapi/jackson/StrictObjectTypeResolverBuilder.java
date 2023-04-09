/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import dev.learning.xapi.model.ObjectType;
import java.io.IOException;
import java.util.Collection;

/**
 * Custom TypeResolverBuilder which accepts only valid strings as type identifiers.
 *
 * @author István Rátkai (Selindek)
 */
public class StrictObjectTypeResolverBuilder extends StdTypeResolverBuilder {

  @Override
  public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType,
      Collection<NamedType> subtypes) {
    final var subTypeValidator = verifyBaseTypeValidity(config, baseType);
    final var idRes = idResolver(config, baseType, subTypeValidator, subtypes, false, true);
    final var defaultImpl = defineDefaultImpl(config, baseType);

    return new StrictObjectTypePropertyDeserializer(baseType, idRes, _typeProperty, _typeIdVisible,
        defaultImpl, _includeAs);

  }

  /**
   * Custom {@link AsPropertyTypeDeserializer} which accepts only valid {@link ObjectType} as
   * property value.
   */
  public static class StrictObjectTypePropertyDeserializer extends AsPropertyTypeDeserializer {

    private static final long serialVersionUID = 1139909729567678431L;

    public StrictObjectTypePropertyDeserializer(JavaType baseType, TypeIdResolver idRes,
        String typeProperty, boolean typeIdVisible, JavaType defaultImpl, As includeAs) {
      super(baseType, idRes, typeProperty, typeIdVisible, defaultImpl, includeAs);
    }

    public StrictObjectTypePropertyDeserializer(AsPropertyTypeDeserializer src,
        BeanProperty property) {
      super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
      return (prop == _property) ? this : new StrictObjectTypePropertyDeserializer(this, prop);
    }

    @Override
    protected Object _deserializeTypedForId(JsonParser p, DeserializationContext ctxt,
        TokenBuffer tb, String typeId) throws IOException {

      // This is the actual custom logic.
      if (ObjectType.getByValue(typeId).isEmpty()) {
        throw new InvalidTypeIdException(p, "Invalid objectType", _baseType, typeId);
      }
      // Everything else is just unavoidable duplication of the original code.

      return super._deserializeTypedForId(p, ctxt, tb, typeId);
    }

  }

}
