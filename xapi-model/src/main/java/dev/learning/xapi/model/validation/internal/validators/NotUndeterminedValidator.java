/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.NotUndetermined;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;

/**
 * The Locale being validated must be a non undetermined {@link Locale}.
 *
 * @author István Rátkai (Selindek)
 */
public class NotUndeterminedValidator extends DisableableValidator<NotUndetermined, Locale> {

  @Override
  public boolean isValidIfEnabled(Locale value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return !value.toLanguageTag().equalsIgnoreCase("und");

  }

}
