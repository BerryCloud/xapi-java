/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.validation.constraints.ValidStatementPlatform;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must have a valid platform.
 *
 * <p>If context.platform present, then object must be an {@link Activity}.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-10">
 *     Statement Context Requirements</a>
 */
public class StatementPlatformValidator
    extends DisableableValidator<ValidStatementPlatform, CoreStatement> {

  @Override
  public boolean isValidIfEnabled(CoreStatement value, ConstraintValidatorContext context) {

    return value == null
        || value.getContext() == null
        || value.getContext().getPlatform() == null
        || value.getObject() instanceof Activity;
  }
}
