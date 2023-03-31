/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.ValidLocale;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * The Locale being validated must have a ISO3 Language and Country.
 * <p>
 * There is no way to reliably test a locale that was instantiated with
 * {@link Locale.forLanguageTag}. {@link Locale.forLanguageTag} treats most invalid locales as
 * undetermined (und).
 * </p>
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
public class LocaleValidator implements ConstraintValidator<ValidLocale, Locale> {

  @Override
  public boolean isValid(Locale originalLocale, ConstraintValidatorContext context) {

    if (originalLocale == null) {
      return true;
    }

    var localeString = originalLocale.toString().replace("_", "-");

    // The only language-code I've found which is actually an alias
    if (localeString.equalsIgnoreCase("zh-CHS")) {
      localeString = "chs";
    }

    var locale = Locale.forLanguageTag(localeString);
    try {
      // test validity of language and country codes (throws exception)
      locale.getISO3Language();
      locale.getISO3Country();
    } catch (final MissingResourceException e) {
      locale = null;
    }
    // test the validity of the whole key
    return locale != null && locale.toLanguageTag().equalsIgnoreCase(localeString);
  }

}
