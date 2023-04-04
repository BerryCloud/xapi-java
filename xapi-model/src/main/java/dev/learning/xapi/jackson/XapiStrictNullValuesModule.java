/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * xAPI JSON module for setting strict deserializer modifier.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiStrictNullValuesModule extends SimpleModule {

  private static final long serialVersionUID = -5943467400927276326L;

  /**
   * XapiStrictNullValuesModule constructor. Set {@link NotNullDeserializationModifier} to the
   * ObjectMapper.
   */
  public XapiStrictNullValuesModule() {
    super("xApi Strict Null Values Module");

    setDeserializerModifier(new NotNullDeserializationModifier());
  }
}
