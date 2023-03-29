/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.LanguageMap;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ScaledScoreValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("ScaledSoreValidator tests")
class LanguageMapValidatorTests {

  private static final LanguageMapValidator validator = new LanguageMapValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithUKKeyThenResultIsTrue() {

    final var languageMap = new LanguageMap();
    languageMap.put(Locale.UK, "Hello World!");

    // When Calling Is Valid On Language Map With UK Key
    final var result = validator.isValid(languageMap, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithENKeyThenResultIsTrue() {

    final var languageMap = new LanguageMap();
    languageMap.put(Locale.ENGLISH, "Hello World!");

    // When Calling Is Valid On Language Map With EN Key
    final var result = validator.isValid(languageMap, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithENAndUKKeysThenResultIsTrue() {

    final var languageMap = new LanguageMap();
    languageMap.put(Locale.ENGLISH, "Hello World!");
    languageMap.put(Locale.UK, "Hello World!");

    // When Calling Is Valid On Language Map With EN And UK Keys
    final var result = validator.isValid(languageMap, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithENAndUnknownKeysThenResultIsFalse() {

    final var languageMap = new LanguageMap();
    languageMap.put(Locale.ENGLISH, "Hello World!");
    languageMap.put(Locale.forLanguageTag("unknown"), "Hello World!");

    // When Calling Is Valid On Language Map With EN And Unknown Keys
    final var result = validator.isValid(languageMap, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithChineseSimplifiedKeyUsingForLangugeTagThenResultIsTrue() {

    final var languageMap = new LanguageMap();
    languageMap.put(Locale.forLanguageTag("zh-CHS"), "Hello World!");

    // When Calling Is Valid On Language Map With Chinese Simplified Key
    final var result = validator.isValid(languageMap, null);

    // Then Result Is True
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"und", "zh-CHS", "zh-CN", "zh-Hans", "zh-Hant", "zh-HK"})
  void whenCallingIsValidOnLanguageMapWithValidKeyThenResultIsTrue(String arg) {

    final var languageMap = new LanguageMap();
    languageMap.put(new Locale(arg), "Hello World!");

    // When Calling Is Valid On Language Map With Valid Key
    final var result = validator.isValid(languageMap, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenCallingIsValidOnLanguageMapWithUnknownKeyThenResultIsFalse() {

    final var languageMap = new LanguageMap();
    languageMap.put(new Locale("unknown"), "Hello World!");

    // When Calling Is Valid On Language Map With Unknown Key
    final var result = validator.isValid(languageMap, null);

    // Then Result Is False
    assertFalse(result);
  }

}
