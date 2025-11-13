/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * ScoreValidator Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("ScoreValidator tests")
class ScoreValidatorTests {

  private static final ScoreValidator validator = new ScoreValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @CsvSource({"0,100,50", "0,100,0", "0,100,100", "-100,0,-50"})
  void whenCallingIsValidWithValidScoreThenResultIsTrue(float min, float max, float raw) {

    final var s = Score.builder().min(min).max(max).raw(raw).build();

    // When Calling Is Valid With Valid Score
    final var result = validator.isValid(s, null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @CsvSource({"0,100,101", "0,100,-1"})
  void whenCallingIsValidWithInvalidScoreThenResultIsFalse(float min, float max, float raw) {

    final var s = Score.builder().min(min).max(max).raw(raw).build();

    // When Calling Is Valid With Invalid Score
    final var result = validator.isValid(s, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenCallingIsValidWithAScoreWithMinNullThenResultIsTrue() {

    final var s = Score.builder().min(null).max(100F).raw(50F).build();

    // When Calling Is Valid With A Score With Min Null
    final var result = validator.isValid(s, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidWithAScoreWithMaxNullThenResultIsTrue() {

    final var s = Score.builder().min(0F).max(null).raw(50F).build();

    // When Calling Is Valid With A Score With Max Null
    final var result = validator.isValid(s, null);

    // Then Result Is True
    assertTrue(result);
  }



}
