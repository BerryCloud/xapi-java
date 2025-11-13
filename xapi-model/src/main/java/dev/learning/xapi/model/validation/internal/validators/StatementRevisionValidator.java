/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.validation.constraints.ValidStatementRevision;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must have a valid revision.
 * <p>
 * If context.revision present, then object must be an {@link Activity}.
 * </p>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-10">
 *      Statement Context Requirements</a>
 */
public class StatementRevisionValidator
    extends DisableableValidator<ValidStatementRevision, CoreStatement> {

  @Override
  public boolean isValidIfEnabled(CoreStatement value, ConstraintValidatorContext context) {

    return value == null || value.getContext() == null || value.getContext().getRevision() == null
        || value.getObject() instanceof Activity;
  }

}
