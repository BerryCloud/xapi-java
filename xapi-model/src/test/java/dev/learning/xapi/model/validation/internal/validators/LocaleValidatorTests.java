/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * LocaleValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("LocaleValidator tests")
class LocaleValidatorTests {

  private static final LocaleValidator validator = new LocaleValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnUKLocaleThenResultIsTrue() {

    // When Calling Is Valid On UK Locale
    final var result = validator.isValid(Locale.UK, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnENLocaleThenResultIsTrue() {

    // When Calling Is Valid On EN Locale
    final var result = validator.isValid(Locale.ENGLISH, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnUSLocaleThenResultIsTrue() {

    // When Calling Is Valid On US Locale
    final var result = validator.isValid(Locale.US, null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"und", "chs", "zh-CHS", "zh-CN", "zh-Hans", "zh-Hant", "zh-HK"})
  void whenCallingIsValidOnLocaleWithValidLanguageThenResultIsTrue(String arg) {

    // when Calling Is Valid On Locale With Valid Language
    final var result = validator.isValid(new Locale(arg), null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"unknown", "a12345678", "123456789", "12345678", "1234567"})
  void whenCallingIsValidOnLocaleWithInvalidLanguageThenResultIsFalse(String arg) {

    // When Calling Is Valid On Locale With Invalid Language
    final var result = validator.isValid(new Locale(arg), null);

    // Then Result Is False
    assertFalse(result);
  }

}
