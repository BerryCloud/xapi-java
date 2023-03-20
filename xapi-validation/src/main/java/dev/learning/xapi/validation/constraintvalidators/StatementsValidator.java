/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.validation.constraintvalidators;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.validation.constraints.Statements;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Validates a list of statements.
 *
 * @author Thomas Turrell-Croft
 */
public class StatementsValidator implements ConstraintValidator<Statements, List<Statement>> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(List<Statement> values, ConstraintValidatorContext context) {

    Set<UUID> set = new HashSet<UUID>();
    for (Statement statement : values) {
      if (!set.add(statement.getId())) {
        return false;
      }
    }
    return true;

  }
}
