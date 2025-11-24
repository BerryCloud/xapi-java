/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.validation.constraints.ValidStatementVerb;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must have a valid verb.
 *
 * <p>If the verb is 'voided' then the object must be a {@link StatementReference}.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-1">
 *     Voiding Statement Requirements</a>
 */
public class StatementVerbValidator
    extends DisableableValidator<ValidStatementVerb, CoreStatement> {

  @Override
  public boolean isValidIfEnabled(CoreStatement value, ConstraintValidatorContext context) {

    return value == null
        || value.getVerb() == null
        || !value.getVerb().isVoided()
        || value.getObject() instanceof StatementReference;
  }
}
