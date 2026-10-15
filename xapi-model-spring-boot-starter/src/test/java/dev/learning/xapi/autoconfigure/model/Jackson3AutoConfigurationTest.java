/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.autoconfigure.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.jackson.XapiStrictObjectTypeModule;
import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.LanguageMap;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementObject;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.SubStatement;
import java.time.Instant;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.json.JsonMapper;

class Jackson3AutoConfigurationTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  XapiModelAutoConfiguration.class, JacksonAutoConfiguration.class));

  @Test
  void strictDefaultsRejectInvalidInput() {
    runner.run(
        context -> {
          JsonMapper mapper = context.getBean(JsonMapper.class);
          for (String json :
              new String[] {
                "{\"actor\":null}",
                "{\"unknown\":1}",
                "{} {}",
                "{\"timestamp\":\"2015-11-18T12:17:00-00:00\"}",
                "{\"verb\":{\"display\":{\"invalid_locale\":\"name\"}}}",
                "{\"object\":{\"objectType\":\"Invalid\"}}",
                "{\"result\":{\"success\":\"true\"}}"
              }) {
            assertThrows(
                JacksonException.class, () -> mapper.readValue(json, Statement.class), json);
          }
          assertNotNull(mapper.readValue("{}", Statement.class));
        });
  }

  @Test
  void nullOptOutAllowsExplicitNullAndExtensionNullsRemainLegal() {
    runner
        .withPropertyValues("xapi.model.validateNullValues=false")
        .run(
            context -> {
              assertNotNull(
                  context.getBean(JsonMapper.class).readValue("{\"actor\":null}", Statement.class));
            });
    runner.run(
        context -> {
          var statement =
              context
                  .getBean(JsonMapper.class)
                  .readValue(
                      "{\"context\":{\"extensions\":{\"https://example.com/key\":null}}}",
                      Statement.class);
          assertEquals(1, statement.getContext().getExtensions().size());
        });
  }

  @Test
  void propertiesAndJsonOptOutsOverrideJacksonDefaults() {
    runner
        .withPropertyValues("xapi.model.validateProperties=false", "xapi.model.validateJson=false")
        .run(
            context ->
                assertNotNull(
                    context
                        .getBean(JsonMapper.class)
                        .readValue("{\"unknown\":1} {}", Statement.class)));
  }

  @Test
  void timestampLocaleObjectTypeAndLiteralOptOutsStillWork() {
    runner
        .withPropertyValues(
            "xapi.model.validateTimestamp=false",
            "xapi.model.validateLocale=false",
            "xapi.model.validateObjectType=false",
            "xapi.model.validateLiterals=false")
        .run(
            context -> {
              JsonMapper mapper = context.getBean(JsonMapper.class);
              assertNotNull(
                  mapper.readValue(
                      "{\"timestamp\":\"2015-11-18T12:17:00-00:00\"}", Statement.class));
              assertNotNull(mapper.readValue("{\"zz\":\"name\"}", LanguageMap.class));
              assertFalse(
                  mapper.registeredModules().stream()
                      .anyMatch(XapiStrictObjectTypeModule.class::isInstance));
              // Opting out of the strict resolver retains the model's enum validation.
              assertThrows(
                  InvalidFormatException.class,
                  () ->
                      mapper.readValue(
                          "{\"objectType\":\"Invalid\",\"id\":\"https://example.com\"}",
                          StatementObject.class));
              assertEquals(
                  Boolean.TRUE,
                  mapper
                      .readValue("{\"result\":{\"success\":\"true\"}}", Statement.class)
                      .getResult()
                      .getSuccess());
            });
  }

  @Test
  void timestampsAndLanguageMapsRetainTheirWireRepresentations() {
    runner.run(
        context -> {
          JsonMapper mapper = context.getBean(JsonMapper.class);
          String json =
              "{\"timestamp\":\"2026-01-02T03:04:05.123456789Z\",\"verb\":{\"display\":{\"en-GB\":\"Learnt\",\"fr\":\"Appris\"}}}";
          Statement statement = mapper.readValue(json, Statement.class);
          assertEquals(Instant.parse("2026-01-02T03:04:05.123456789Z"), statement.getTimestamp());
          assertEquals("Learnt", statement.getVerb().getDisplay().get(Locale.UK));
          assertEquals(
              mapper.readTree(json), mapper.readTree(mapper.writeValueAsString(statement)));
        });
  }

  @Test
  void scalarContextActivitiesAndArrayOnlyAttachmentsRetainTheirRules() {
    runner.run(
        context -> {
          JsonMapper mapper = context.getBean(JsonMapper.class);
          Statement statement =
              mapper.readValue(
                  "{\"context\":{\"contextActivities\":{\"parent\":{\"id\":\"https://example.com\"}}}}",
                  Statement.class);
          assertEquals(1, statement.getContext().getContextActivities().getParent().size());
          assertThrows(
              JacksonException.class,
              () -> mapper.readValue("{\"attachments\":{}}", Statement.class));
          assertThrows(
              JacksonException.class,
              () ->
                  mapper.readValue(
                      "{\"objectType\":\"Group\",\"member\":{}}", StatementObject.class));
        });
  }

  @Test
  void invalidDiscriminatorTokensStillFailWithStrictValidation() {
    runner.run(
        context -> {
          JsonMapper mapper = context.getBean(JsonMapper.class);
          for (String token : new String[] {"42", "true", "{}", "[]", "\"group\"", "\"\""}) {
            assertThrows(
                JacksonException.class,
                () -> mapper.readValue("{\"objectType\":" + token + "}", StatementObject.class));
          }
        });
  }

  @Test
  void polymorphicObjectsAndNestedSubStatementsRoundTrip() {
    runner.run(
        context -> {
          JsonMapper mapper = context.getBean(JsonMapper.class);
          String[] json = {
            "{\"id\":\"https://example.com\"}",
            "{\"objectType\":\"Agent\",\"mbox\":\"mailto:a@example.com\"}",
            "{\"objectType\":\"Group\",\"member\":[{\"mbox\":\"mailto:a@example.com\"}]}",
            "{\"objectType\":\"StatementRef\",\"id\":\"8c902759-7f8d-4b24-8649-318ea3ecf139\"}",
            "{\"objectType\":\"SubStatement\",\"object\":{\"id\":\"https://example.com\"}}"
          };
          Class<?>[] types = {
            Activity.class, Agent.class, Group.class, StatementReference.class, SubStatement.class
          };
          for (int i = 0; i < json.length; i++) {
            var value = mapper.readValue(json[i], StatementObject.class);
            assertInstanceOf(types[i], value);
            assertEquals(
                value, mapper.readValue(mapper.writeValueAsString(value), StatementObject.class));
          }
        });
  }
}
