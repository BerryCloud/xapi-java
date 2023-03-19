/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.validation.constraints.ValidStatementVerb;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must have a valid revision.
 * <p>
 * if verb is 'voided' then object must be a {@link StatementReference}.
 * </p>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-1">
 *      Voiding Statement Requirements</a>
 */
public class StatementVerbValidator
    implements ConstraintValidator<ValidStatementVerb, CoreStatement> {

  @Override
  public boolean isValid(CoreStatement value, ConstraintValidatorContext context) {

    return value == null || value.getVerb() == null || !value.getVerb().isVoided()
        || value.getObject() instanceof StatementReference;

  }

}
