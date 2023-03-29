/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.LanguageMap;
import dev.learning.xapi.model.validation.constraints.ValidLanguageMap;
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
public class LanguageMapValidator implements ConstraintValidator<ValidLanguageMap, LanguageMap> {

  @Override
  public boolean isValid(LanguageMap value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return value.keySet().stream().allMatch(this::valid);

  }

  private boolean valid(Locale locale) {

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
