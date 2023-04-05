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
@AutoConfigureBefore(value = JacksonProperties.class)
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
   * StrictObjectTypeCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictObjectType", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictObjectTypeCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictObjectTypeModule())
    );
  }
  
  /**
   * StrictLocaleCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictLocale", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictLocaleCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictLocaleModule())
    );
  }

  /**
   * StrictTimestampCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictTimestamp", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictTimestampCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictTimestampModule())
    );
  }

  /**
   * StrictNullValuesCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictNullValues", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictNullValuesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.registerModule(new XapiStrictNullValuesModule())
    );
  }
  
  /**
   * SstrictPropertiesCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictProperties", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictPropertiesCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
    );
  }

  /**
   * StrictJsonCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictJson", havingValue = "true",
      matchIfMissing = true)
  public Jackson2ObjectMapperBuilderCustomizer strictJsonCustomizer() {
    return builder -> builder.postConfigurer(objectMapper -> 
      objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
    );
  }
  
  /**
   * StrictLiteralsCustomizer.
   */
  @Bean
  @ConditionalOnProperty(name = "xApi.model.strictLiterals", havingValue = "true",
      matchIfMissing = true)
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
