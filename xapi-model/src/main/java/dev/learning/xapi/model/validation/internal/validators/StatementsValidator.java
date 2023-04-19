/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.validation.constraints.Statements;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

/**
 * Validates a list of statements.
 *
 * @author Thomas Turrell-Croft
 */
public class StatementsValidator implements ConstraintValidator<Statements, List<Statement>> {

  @Value("#{!${xApi.model.validateStatementListIds:true}}")
  private boolean disabled;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(List<Statement> values, ConstraintValidatorContext context) {

    if (disabled || values == null) {
      return true;
    }

    final Set<UUID> set = new HashSet<>();
    for (final Statement statement : values) {
      if (statement.getId() != null && !set.add(statement.getId())) {
        return false;
      }
    }
    return true;

  }
}
