/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import io.jsonwebtoken.io.AbstractDeserializer;
import java.io.Reader;
import java.util.Map;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/** Jackson 3 JSON deserializer for JJWT headers and claims. */
public final class JjwtDeserializer extends AbstractDeserializer<Map<String, ?>> {

  private final ObjectMapper mapper;

  /** Create a deserializer using Jackson 3's JSON defaults. */
  public JjwtDeserializer() {
    this(JsonMapper.builder().build());
  }

  /**
   * Create a deserializer using an application's configured mapper.
   *
   * @param mapper the JSON mapper
   */
  public JjwtDeserializer(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  protected Map<String, ?> doDeserialize(Reader reader) {
    return mapper.readValue(reader, new TypeReference<Map<String, ?>>() {});
  }
}
