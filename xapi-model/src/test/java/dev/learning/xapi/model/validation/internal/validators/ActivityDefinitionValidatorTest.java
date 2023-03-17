/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dev.learning.xapi.model.ActivityDefinition;
import dev.learning.xapi.model.InteractionType;

/**
 * ActivityDefinitionValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("ActivityDefinitionValidator tests")
public class ActivityDefinitionValidatorTest {

  private static final ActivityDefinitionValidator validator = new ActivityDefinitionValidator();
  
  @Test
  void whenValueIsNullThenResultIsTrue() {
    
    // When Value Is Null
    var result = validator.isValid(null, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  
  @Test
  void whenValueHasInteractionTypeThenResultIsTrue() {
    
    // When Value Has InteractionType
    var value = ActivityDefinition.builder().interactionType(InteractionType.CHOICE).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueHasNoInteractionTypeButHasCorrectResponsepatternThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has CorrectResponsePatters
    var value = ActivityDefinition.builder().correctResponsesPattern(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }
  
  @Test
  void whenValueHasNoInteractionTypeButHasChoicesThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has Choices
    var value = ActivityDefinition.builder().choices(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasNoInteractionTypeButHasScaleThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has Scale
    var value = ActivityDefinition.builder().scale(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasNoInteractionTypeButHasSourceThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has Source
    var value = ActivityDefinition.builder().source(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasNoInteractionTypeButHasTargetThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has Target
    var value = ActivityDefinition.builder().target(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasNoInteractionTypeButHasStepsThenResultIsFalse() {
    
    // When Value Has No InteractionType But Has Steps
    var value = ActivityDefinition.builder().steps(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }


}
