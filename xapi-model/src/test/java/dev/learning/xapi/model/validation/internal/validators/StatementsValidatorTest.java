/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StatementsValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("StatementsValidator tests")
class StatementsValidatorTest {

  private static final StatementsValidator validator = new StatementsValidator();

  @Test
  void whenValidatingNullListlThenResultIsTrue() {

    // When Validating Null List
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValidatingEmptyListlThenResultIsTrue() {

    // When Validating Empty List
    final var result = validator.isValid(new ArrayList<Statement>(), null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValidatingOneStatementWithoutIdThenValidIsTrue() {

    final var statement =
        Statement.builder()
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    // When Validating One Statement Without Id
    final var valid = validator.isValid(Arrays.asList(statement), null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void whenValidatingTwoStatementsWithoutIdThenValidIsTrue() {

    final var statement1 =
        Statement.builder()
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    final var statement2 =
        Statement.builder()
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    // When Validating Two Statements Without Id
    final var valid = validator.isValid(Arrays.asList(statement1, statement2), null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void whenValidatingTwoStatementsWithIdThenValidIsTrue() {

    final var statement1 =
        Statement.builder()
            .id(UUID.randomUUID())
            .groupActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    final var statement2 =
        Statement.builder()
            .id(UUID.randomUUID())
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    // When Validating Two Statements Without Id
    final var valid = validator.isValid(Arrays.asList(statement1, statement2), null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void whenValidatingTwoStatementsWithSameIdThenValidIsFalse() {

    final var statement1 =
        Statement.builder()
            .id(UUID.fromString("b79a5a9b-d9f5-470b-afba-97228e26a031"))
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    final var statement2 =
        Statement.builder()
            .id(UUID.fromString("b79a5a9b-d9f5-470b-afba-97228e26a031"))
            .agentActor(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(Verb.ATTEMPTED)
            .activityObject(o -> o.id("https://example.com/activity/simplestatement"))
            .build();

    // When Validating Two Statements With Same Id
    final var valid = validator.isValid(Arrays.asList(statement1, statement2), null);

    // Then Valid Is False
    assertFalse(valid);
  }
}
