/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.validation.constraints.ValidStatementVerb;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

/**
 * The Statement being validated must have a valid verb.
 * <p>
 * If the verb is 'voided' then the object must be a {@link StatementReference}.
 * </p>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#requirements-1">
 *      Voiding Statement Requirements</a>
 */
public class StatementVerbValidator
    implements ConstraintValidator<ValidStatementVerb, CoreStatement> {

  @Value("#{!${xApi.model.validateStatementVerb:true}}")
  private boolean disabled;

  @Override
  public boolean isValid(CoreStatement value, ConstraintValidatorContext context) {

    return disabled || value == null || value.getVerb() == null || !value.getVerb().isVoided()
        || value.getObject() instanceof StatementReference;

  }

}
