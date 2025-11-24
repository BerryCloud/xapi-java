/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.ActivityDefinition;
import dev.learning.xapi.model.InteractionType;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ActivityDefinitionValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("ActivityDefinitionValidator tests")
class ActivityDefinitionValidatorTests {

  private static final ActivityDefinitionValidator validator = new ActivityDefinitionValidator();

  @Test
  void whenIsValidIsCalledWithNullActivityDefinitionThenResultIsTrue() {

    // When IsValid Is Called With Null Activity Definition
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThenResultIsTrue() {

    final var value = ActivityDefinition.builder().build();

    // When IsValid Is Called With Activity Definition
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasInteractionTypeThenResultIsTrue() {

    final var value = ActivityDefinition.builder().interactionType(InteractionType.CHOICE).build();

    // When IsValid Is Called With Activity Definition That Has Interaction Type
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasCorrectResponsesPatternThenResultIsFalse() {

    final var value =
        ActivityDefinition.builder().correctResponsesPattern(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Correct Responses Pattern
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void
      whenIsValidIsCalledWithActivityDefinitionThatHasCorrectResponsesPatternAndInteractionTypeThenResultIsTrue() {

    final var value =
        ActivityDefinition.builder()
            .correctResponsesPattern(new ArrayList<>())
            .interactionType(InteractionType.CHOICE)
            .build();

    // When IsValid Is Called With Activity Definition That Has Correct Responses Pattern And
    // InteractionType
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasChoicesThenResultIsFalse() {

    final var value = ActivityDefinition.builder().choices(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Choices
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasChoicesAndInteractionTypeThenResultIsTrue() {

    final var value =
        ActivityDefinition.builder()
            .choices(new ArrayList<>())
            .interactionType(InteractionType.CHOICE)
            .build();

    // When IsValid Is Called With Activity Definition That Has Choices And InteractionType
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasScaleThenResultIsFalse() {

    final var value = ActivityDefinition.builder().scale(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Scale
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasScaleAndInteractionTypeThenResultIsTrue() {

    final var value =
        ActivityDefinition.builder()
            .scale(new ArrayList<>())
            .interactionType(InteractionType.LIKERT)
            .build();

    // When IsValid Is Called With Activity Definition That Has Scale And InteractionType
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasSourceThenResultIsFalse() {

    final var value = ActivityDefinition.builder().source(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Source
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionAndInteractionTypeThatHasSourceThenResultIsTrue() {

    final var value =
        ActivityDefinition.builder()
            .source(new ArrayList<>())
            .interactionType(InteractionType.MATCHING)
            .build();

    // When IsValid Is Called With Activity Definition That Has Source And InteractionType
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasTargetThenResultIsFalse() {

    // When IsValid Is Called With Activity Definition That Has Target
    final var value = ActivityDefinition.builder().target(new ArrayList<>()).build();

    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasTargetAndInteractionTypeThenResultIsTrue() {

    // When IsValid Is Called With Activity Definition That Has Target And InteractionType
    final var value =
        ActivityDefinition.builder()
            .target(new ArrayList<>())
            .interactionType(InteractionType.MATCHING)
            .build();

    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasStepsThenResultIsFalse() {

    final var value = ActivityDefinition.builder().steps(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Steps
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasStepsAndInteractionTypeThenResultIsTrue() {

    final var value =
        ActivityDefinition.builder()
            .steps(new ArrayList<>())
            .interactionType(InteractionType.PERFORMANCE)
            .build();

    // When IsValid Is Called With Activity Definition That Has Steps And InteractionType
    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }
}
