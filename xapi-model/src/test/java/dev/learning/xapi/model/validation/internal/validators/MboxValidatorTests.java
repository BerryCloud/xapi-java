/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.validation.constraints.Mbox;
import jakarta.validation.Payload;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * MboxValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("MboxValidator tests")
class MboxValidatorTests {

  private static final MboxValidator validator = new MboxValidator();

  @BeforeAll
  static void init() {
    validator.initialize(
        new Mbox() {

          @Override
          public Class<? extends Annotation> annotationType() {
            return null;
          }

          @Override
          public Class<? extends Payload>[] payload() {
            return null;
          }

          @Override
          public String message() {
            return null;
          }

          @Override
          public Class<?>[] groups() {
            return null;
          }
        });
  }

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueIsValidMboxThenResultIsTrue() {

    // When Value Is Valid Mbox
    final var result = validator.isValid("mailto:fred@example.com", null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"email:fred@example.com", "fred@example.com", "mailto:fred@example@com"})
  void whenValueIsInvalidThenResultIsFalse(String value) {

    // When Value Is Invalid
    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }
}
