/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.Variant;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * The UUID being validated must have the specified variant.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
public class VariantValidatorForUuid extends DisableableValidator<Variant, UUID> {

  private int variant;

  @Override
  public void initialize(Variant constraintAnnotation) {

    variant = constraintAnnotation.value();
  }

  @Override
  public boolean isValidIfEnabled(UUID value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return value.variant() == variant;

  }

}
