/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
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
    var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThenResultIsTrue() {

    var value = ActivityDefinition.builder().build();

    // When IsValid Is Called With Activity Definition
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasInteractionTypeThenResultIsTrue() {

    var value = ActivityDefinition.builder().interactionType(InteractionType.CHOICE).build();

    // When IsValid Is Called With Activity Definition That Has Interaction Type
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasCorrectResponsesPatternThenResultIsFalse() {

    var value = ActivityDefinition.builder().correctResponsesPattern(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Correct Responses Pattern
    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasCorrectResponsesPatternAndInteractionTypeThenResultIsTrue() {

    var value = ActivityDefinition.builder().correctResponsesPattern(new ArrayList<>())
        .interactionType(InteractionType.CHOICE).build();

    // When IsValid Is Called With Activity Definition That Has Correct Responses Pattern And
    // InteractionType
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasChoicesThenResultIsFalse() {

    var value = ActivityDefinition.builder().choices(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Choices
    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasChoicesAndInteractionTypeThenResultIsTrue() {

    var value = ActivityDefinition.builder().choices(new ArrayList<>())
        .interactionType(InteractionType.CHOICE).build();

    // When IsValid Is Called With Activity Definition That Has Choices And InteractionType
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasScaleThenResultIsFalse() {

    var value = ActivityDefinition.builder().scale(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Scale
    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasScaleAndInteractionTypeThenResultIsTrue() {

    var value = ActivityDefinition.builder().scale(new ArrayList<>())
        .interactionType(InteractionType.LIKERT).build();

    // When IsValid Is Called With Activity Definition That Has Scale And InteractionType
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasSourceThenResultIsFalse() {

    var value = ActivityDefinition.builder().source(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Source
    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionAndInteractionTypeThatHasSourceThenResultIsTrue() {

    var value = ActivityDefinition.builder().source(new ArrayList<>())
        .interactionType(InteractionType.MATCHING).build();

    // When IsValid Is Called With Activity Definition That Has Source And InteractionType
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasTargetThenResultIsFalse() {

    // When IsValid Is Called With Activity Definition That Has Target
    var value = ActivityDefinition.builder().target(new ArrayList<>()).build();

    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasTargetAndInteractionTypeThenResultIsTrue() {

    // When IsValid Is Called With Activity Definition That Has Target And InteractionType
    var value = ActivityDefinition.builder().target(new ArrayList<>())
        .interactionType(InteractionType.MATCHING).build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }


  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasStepsThenResultIsFalse() {

    var value = ActivityDefinition.builder().steps(new ArrayList<>()).build();

    // When IsValid Is Called With Activity Definition That Has Steps
    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenIsValidIsCalledWithActivityDefinitionThatHasStepsAndInteractionTypeThenResultIsTrue() {

    var value = ActivityDefinition.builder().steps(new ArrayList<>())
        .interactionType(InteractionType.PERFORMANCE).build();

    // When IsValid Is Called With Activity Definition That Has Steps And InteractionType
    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }


}
