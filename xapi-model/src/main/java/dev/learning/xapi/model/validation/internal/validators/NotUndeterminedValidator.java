/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.NotUndetermined;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;

/**
 * The Locale being validated must be a non undetermined {@link Locale}.
 *
 * @author István Rátkai (Selindek)
 */
public class NotUndeterminedValidator implements ConstraintValidator<NotUndetermined, Locale> {

  @Value("#{!${xApi.model.validateLocaleNotUndetermined:true}}")
  private boolean disabled;

  @Override
  public boolean isValid(Locale value, ConstraintValidatorContext context) {

    if (disabled || value == null) {
      return true;
    }

    return !value.toLanguageTag().equalsIgnoreCase("und");

  }

}
