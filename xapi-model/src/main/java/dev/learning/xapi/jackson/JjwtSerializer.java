/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import io.jsonwebtoken.io.AbstractSerializer;
import java.io.OutputStream;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/** Jackson 3 JSON serializer for JJWT headers and claims, including xAPI model objects. */
public final class JjwtSerializer<T> extends AbstractSerializer<T> {

  private final ObjectMapper mapper;

  /** Create a Jackson 3 serializer preserving the existing signed-payload property order. */
  public JjwtSerializer() {
    this(JsonMapper.builder().disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build());
  }

  /**
   * Create a serializer using an application's configured mapper.
   *
   * @param mapper the JSON mapper
   */
  public JjwtSerializer(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  protected void doSerialize(T value, OutputStream out) throws Exception {
    // JJWT owns the stream; writing bytes avoids closing it through Jackson's generator.
    out.write(mapper.writeValueAsBytes(value));
  }
}
