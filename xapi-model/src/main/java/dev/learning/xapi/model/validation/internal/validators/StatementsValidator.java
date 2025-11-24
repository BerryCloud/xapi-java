/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.validation.constraints.Statements;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
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
public class StatementsValidator extends DisableableValidator<Statements, List<Statement>> {

  /** {@inheritDoc} */
  @Override
  public boolean isValidIfEnabled(List<Statement> values, ConstraintValidatorContext context) {

    if (values == null) {
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
