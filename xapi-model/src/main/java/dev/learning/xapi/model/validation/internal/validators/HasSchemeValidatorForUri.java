/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.HasScheme;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.URI;

/**
 * The URI being validated must have a schema.
 *
 * @author Thomas Turrell-Croft
 */
public class HasSchemeValidatorForUri extends DisableableValidator<HasScheme, URI> {

  @Override
  public boolean isValidIfEnabled(URI value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return value.getScheme() != null;

  }

}
