/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.Variant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

/**
 * The UUID being validated must have the specified variant.
 *
 * @author Thomas Turrell-Croft
 */
public class VariantValidatorForUuid implements ConstraintValidator<Variant, UUID> {

  @Value("#{!${xApi.model.validateUuidVariant:true}}")
  private boolean disabled;

  private int variant;

  @Override
  public void initialize(Variant constraintAnnotation) {

    variant = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(UUID value, ConstraintValidatorContext context) {

    if (disabled || value == null) {
      return true;
    }

    return value.variant() == variant;

  }

}
