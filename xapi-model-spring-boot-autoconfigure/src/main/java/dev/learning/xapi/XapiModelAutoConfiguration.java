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
import dev.learning.xapi.jackson.XapiStrictObjectTypeModule;
import dev.learning.xapi.jackson.XapiStrictTimestampModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XapiModelAutoConfiguration.
 *
 * @author István Rátkai (Selindek)
 */
@Configuration
@AutoConfigureBefore(JacksonProperties.class)
public class XapiModelAutoConfiguration {

  /**
   * SingleValueArrayCustomizer.
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer singleValueArrayCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    );
  }

  /**
   * ValidateObjectTypeCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateObjectType", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateObjectTypeCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictObjectTypeModule())
    );
  }
  
  /**
   * ValidateLocaleCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateLocale", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateLocaleCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictLocaleModule())
    );
  }

  /**
   * ValidateTimestampCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateTimestamp", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateTimestampCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictTimestampModule())
    );
  }

  /**
   * ValidateNullValuesCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateNullValues", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateNullValuesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictNullValuesModule())
    );
  }
  
  /**
   * ValidatePropertiesCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateProperties", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validatePropertiesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
    );
  }

  /**
   * ValidateJsonCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateJson", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateJsonCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
    );
  }
  
  /**
   * ValidateLiteralsCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.validateLiterals", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer validateLiteralsCustomizer() {
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
