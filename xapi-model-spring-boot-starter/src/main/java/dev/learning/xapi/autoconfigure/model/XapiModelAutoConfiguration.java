/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.autoconfigure.model;

import dev.learning.xapi.jackson.XapiStrictLocaleModule;
import dev.learning.xapi.jackson.XapiStrictNullValuesModule;
import dev.learning.xapi.jackson.XapiStrictObjectTypeModule;
import dev.learning.xapi.jackson.XapiStrictTimestampModule;
import dev.learning.xapi.model.validation.disableable.ValidatorDisabler;
import dev.learning.xapi.model.validation.internal.validators.ActivityDefinitionValidator;
import dev.learning.xapi.model.validation.internal.validators.ActorValidator;
import dev.learning.xapi.model.validation.internal.validators.AuthorityValidator;
import dev.learning.xapi.model.validation.internal.validators.HasSchemeValidatorForUri;
import dev.learning.xapi.model.validation.internal.validators.MboxValidator;
import dev.learning.xapi.model.validation.internal.validators.NotUndeterminedValidator;
import dev.learning.xapi.model.validation.internal.validators.ScaledScoreValidator;
import dev.learning.xapi.model.validation.internal.validators.ScoreValidator;
import dev.learning.xapi.model.validation.internal.validators.StatementPlatformValidator;
import dev.learning.xapi.model.validation.internal.validators.StatementRevisionValidator;
import dev.learning.xapi.model.validation.internal.validators.StatementVerbValidator;
import dev.learning.xapi.model.validation.internal.validators.StatementsValidator;
import dev.learning.xapi.model.validation.internal.validators.VariantValidatorForUuid;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.type.LogicalType;

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
   *
   * @return the customizer bean
   */
  @Bean
  public JsonMapperBuilderCustomizer singleValueArrayCustomizer() {
    return builder -> builder.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }

  /**
   * ValidateObjectTypeCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateObjectType",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateObjectTypeCustomizer() {
    return builder -> builder.addModule(new XapiStrictObjectTypeModule());
  }

  /**
   * ValidateLocaleCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateLocale",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateLocaleCustomizer() {
    return builder -> builder.addModule(new XapiStrictLocaleModule());
  }

  /**
   * ValidateTimestampCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateTimestamp",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateTimestampCustomizer() {
    return builder -> builder.addModule(new XapiStrictTimestampModule());
  }

  /**
   * ValidateNullValuesCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateNullValues",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateNullValuesCustomizer() {
    return builder -> builder.addModule(new XapiStrictNullValuesModule());
  }

  /**
   * ValidatePropertiesCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateProperties",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validatePropertiesCustomizer() {
    return builder -> builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  /**
   * ValidateJsonCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateJson",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateJsonCustomizer() {
    return builder -> builder.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
  }

  /**
   * ValidateLiteralsCustomizer.
   *
   * @return the customizer bean
   */
  @Bean
  @ConditionalOnProperty(
      name = "xapi.model.validateLiterals",
      havingValue = "true",
      matchIfMissing = true)
  public JsonMapperBuilderCustomizer validateLiteralsCustomizer() {
    return builder -> {
      builder.withCoercionConfig(
          LogicalType.Boolean,
          config -> config.setCoercion(CoercionInputShape.String, CoercionAction.Fail));
      builder.withCoercionConfig(
          LogicalType.Textual,
          config -> config.setCoercion(CoercionInputShape.Integer, CoercionAction.Fail));
      builder.withCoercionConfig(
          LogicalType.Float,
          config -> config.setCoercion(CoercionInputShape.String, CoercionAction.Fail));
      builder.withCoercionConfig(
          LogicalType.Integer,
          config -> config.setCoercion(CoercionInputShape.String, CoercionAction.Fail));
    };
  }

  /**
   * ValidateActivityDefinitionPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateActivityDefinition", havingValue = "false")
  public BeanPostProcessor validateActivityDefinitionPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ActivityDefinitionValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateActorPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateActor", havingValue = "false")
  public BeanPostProcessor validateActorPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ActorValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateAuthorityPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateAuthority", havingValue = "false")
  public BeanPostProcessor validateAuthorityPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof AuthorityValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateUriSchemePostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateUriScheme", havingValue = "false")
  public BeanPostProcessor validateUriSchemePostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof HasSchemeValidatorForUri validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateMboxPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateMbox", havingValue = "false")
  public BeanPostProcessor validateMboxPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof MboxValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateLocaleNotUndeterminedPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateLocaleNotUndetermined", havingValue = "false")
  public BeanPostProcessor validateLocaleNotUndeterminedPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof NotUndeterminedValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateScaledScorePostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateScaledScore", havingValue = "false")
  public BeanPostProcessor validateScaledScorePostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ScaledScoreValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateScorePostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateScore", havingValue = "false")
  public BeanPostProcessor validateScorePostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ScoreValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateStatementPlatformPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateStatementPlatform", havingValue = "false")
  public BeanPostProcessor validateStatementPlatformPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof StatementPlatformValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateStatementRevisionPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateStatementRevision", havingValue = "false")
  public BeanPostProcessor validateStatementRevisionPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof StatementRevisionValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateStatementListIdsPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateStatementListIds", havingValue = "false")
  public BeanPostProcessor validateStatementListIdsPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof StatementsValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateStatementVerbPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateStatementVerb", havingValue = "false")
  public BeanPostProcessor validateStatementVerbPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof StatementVerbValidator validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }

  /**
   * ValidateUuidVariantPostProcessor.
   *
   * @return the bean post processor
   */
  @Bean
  @ConditionalOnProperty(name = "xapi.model.validateUuidVariant", havingValue = "false")
  public BeanPostProcessor validateUuidVariantPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof VariantValidatorForUuid validator) {
          validator.setDisabler(ValidatorDisabler.DEFAULT_DISABLER);
        }
        return bean;
      }
    };
  }
}
