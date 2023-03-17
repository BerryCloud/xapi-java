/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.ValidableStatement;
import dev.learning.xapi.model.validation.constraints.ValidStatementPlatform;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must have a valid platform.
 * <p>
 * If context.platform present, then object must be an {@link Activity}.
 * </p>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-10">
 *      Statement Context Requirements</a>
 */
public class StatementPlatformValidator implements
    ConstraintValidator<ValidStatementPlatform, ValidableStatement> {

  @Override
  public boolean isValid(ValidableStatement value, ConstraintValidatorContext context) {

    return value == null || value.getContext() == null || value.getContext().getPlatform() == null
        || value.getObject() instanceof Activity; 
  }

}
