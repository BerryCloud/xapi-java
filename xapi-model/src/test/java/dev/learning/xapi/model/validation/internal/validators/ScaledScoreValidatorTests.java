/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ScaledScoreValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("ScaledScoreValidator tests")
class ScaledScoreValidatorTests {

  private static final ScaledScoreValidator validator = new ScaledScoreValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueIsValidScaledScoreThenResultIsTrue() {

    // When Value Is Valid Score
    final var result = validator.isValid(0F, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueIsValidMinimumScoreThenResultIsTrue() {

    // When Value Is Valid Minimum Score
    final var result = validator.isValid(-1F, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueIsValidMaximumScoreThenResultIsTrue() {

    // When Value Is Valid Maximum Score
    final var result = validator.isValid(1F, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void WhenValueIsOverMaximumScoreThenResultIsFalse() {

    // when Value Is Over Maximum Score
    final var result = validator.isValid(1.001F, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueIsBelowMinimumScoreThenResultIsFalse() {

    // When Value Is Below Minimum Score
    final var result = validator.isValid(-1.001F, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueIsNanThenResultIsFalse() {

    // When Value Is NaN
    final var result = validator.isValid(Float.NaN, null);

    // Then Result Is False
    assertFalse(result);
  }

}
