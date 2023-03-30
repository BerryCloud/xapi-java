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
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
public class LocaleValidator implements ConstraintValidator<ValidLocale, Locale> {

  @Override
  public boolean isValid(Locale locale, ConstraintValidatorContext context) {

    if (locale == null) {
      return true;
    }

    try {
      locale.getISO3Language();
      locale.getISO3Country();

      return true;
    } catch (final MissingResourceException e1) {

      // Handle locale instantiated with Locale#Locale(String)
      final var blar = Locale.forLanguageTag(locale.toString());

      try {
        blar.getISO3Language();
        blar.getISO3Country();

        return true;
      } catch (final MissingResourceException e2) {

        return false;
      }
    }
  }

}
