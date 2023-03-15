/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Context;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.validation.constraints.ValidStatement;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The SubStatement being validated must be valid.
 */
public class ValidatorForSubStatement implements ConstraintValidator<ValidStatement, SubStatement> {

  @Override
  public boolean isValid(SubStatement value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    return isValidRevision(value, context) && isValidPlatform(value, context);
  }

  private boolean isValidRevision(SubStatement value, ConstraintValidatorContext context) {

    Context c = value.getContext();

    if (c == null || c.getRevision() == null || value.getObject() instanceof Activity) {
      return true;
    }

    context.buildConstraintViolationWithTemplate(
        "invalid revision property, object must of type activity").addConstraintViolation();

    return false;
  }

  private boolean isValidPlatform(SubStatement value, ConstraintValidatorContext context) {

    Context c = value.getContext();

    if (c == null || c.getPlatform() == null || value.getObject() instanceof Activity) {
      return true;
    }

    context.buildConstraintViolationWithTemplate(
        "invalid platform property, object must be of type activity").addConstraintViolation();

    return false;
  }

}
