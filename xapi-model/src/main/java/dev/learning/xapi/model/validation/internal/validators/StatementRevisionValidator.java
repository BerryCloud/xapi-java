/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.CoreStatement;
import dev.learning.xapi.model.validation.constraints.ValidStatementRevision;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

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
    implements ConstraintValidator<ValidStatementRevision, CoreStatement> {

  @Value("#{!${xApi.model.validateStatementRevision:true}}")
  private boolean disabled;

  @Override
  public boolean isValid(CoreStatement value, ConstraintValidatorContext context) {

    return disabled || value == null || value.getContext() == null
        || value.getContext().getRevision() == null || value.getObject() instanceof Activity;
  }

}
