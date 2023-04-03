/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;

import dev.learning.xapi.jackson.XapiStrictLocaleModule;
import dev.learning.xapi.jackson.XapiStrictNullValuesModule;
import dev.learning.xapi.jackson.XapiStrictTimestampModule;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configure xApi model.
 *
 * @author István Rátkai (Selindek)
 */
@Configuration
@AutoConfigureBefore(value = JacksonProperties.class)
public class XapiModelAutoConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer Customizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictLocale")
  public Jackson2ObjectMapperBuilderCustomizer strictLocaleCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.registerModule(new XapiStrictLocaleModule());
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictTimestamp")
  public Jackson2ObjectMapperBuilderCustomizer strictTimestampCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.registerModule(new XapiStrictTimestampModule());
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictNullValues")
  public Jackson2ObjectMapperBuilderCustomizer strictNullValuesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.registerModule(new XapiStrictNullValuesModule());
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictProperties")
  public Jackson2ObjectMapperBuilderCustomizer strictPropertiesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictJson")
  public Jackson2ObjectMapperBuilderCustomizer strictJsonCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {
      objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
    });
  }

  @Bean
  @ConditionalOnProperty("xApi.model.strictLiterals")
  public Jackson2ObjectMapperBuilderCustomizer strictLiteralsCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> {

      objectMapper.coercionConfigFor(LogicalType.Boolean)

          .setCoercion(CoercionInputShape.String, CoercionAction.Fail);

      objectMapper.coercionConfigFor(LogicalType.Textual)

          .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail);

      objectMapper.coercionConfigFor(LogicalType.Float)

          .setCoercion(CoercionInputShape.String, CoercionAction.Fail);

      objectMapper.coercionConfigFor(LogicalType.Integer)

          .setCoercion(CoercionInputShape.String, CoercionAction.Fail);

    });

  }
}
