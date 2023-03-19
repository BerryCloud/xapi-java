/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.HasScheme;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.URI;

/**
 * The URI being validated must have a schema.
 *
 * @author Thomas Turrell-Croft
 */
public class HasSchemeValidatorForUri implements ConstraintValidator<HasScheme, URI> {

  @Override
  public boolean isValid(URI value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return value.getScheme() != null;

  }

}
