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
  void whenCallingIsValidOnLocaleWithUKKeyThenResultIsTrue() {

    // When Calling Is Valid On Locale With UK Key
    final var result = validator.isValid(Locale.UK, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLocaleWithENKeyThenResultIsTrue() {

    // When Calling Is Valid On Locale With EN Key
    final var result = validator.isValid(Locale.ENGLISH, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLocaleWithENAndUKKeysThenResultIsTrue() {

    // When Calling Is Valid On Locale With EN And UK Keys
    final var result = validator.isValid(Locale.UK, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLocaleWithENAndUnknownKeysThenResultIsFalse() {

    // When Calling Is Valid On Locale With EN And Unknown Keys
    final var result = validator.isValid(Locale.forLanguageTag("unknown"), null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenCallingIsValidOnLocaleWithChineseSimplifiedKeyUsingForLangugeTagThenResultIsTrue() {

    // When Calling Is Valid On Locale With Chinese Simplified Key
    final var result = validator.isValid(Locale.forLanguageTag("zh-CHS"), null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"und", "zh-CHS", "zh-CN", "zh-Hans", "zh-Hant", "zh-HK"})
  void whenCallingIsValidOnLocaleWithValidKeyThenResultIsTrue(String arg) {

    // When Calling Is Valid On Locale With Valid Key
    final var result = validator.isValid(new Locale(arg), null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLocaleWithUnknownKeyThenResultIsFalse() {

    // When Calling Is Valid On Locale With Unknown Key
    final var result = validator.isValid(new Locale("unknown"), null);

    // Then Result Is False
    assertFalse(result);
  }

}
