/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Server configuration.
 */
@Configuration
public class ServerConfig {

  /**
   * Extends the default objectMapper configuration.
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {

      objectMapper.coercionConfigFor(LogicalType.Boolean).setCoercion(CoercionInputShape.String,
          CoercionAction.Fail);

      objectMapper.coercionConfigFor(LogicalType.Textual).setCoercion(CoercionInputShape.Integer,
          CoercionAction.Fail);

      objectMapper.coercionConfigFor(LogicalType.Float).setCoercion(CoercionInputShape.String,
          CoercionAction.Fail);

    });

  }

}
