/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Score;
import dev.learning.xapi.model.validation.constraints.VaildScore;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The raw score must be greater or equal to min and less or equal to max.
 *
 * @author István Rátkai (Selindek)
 */
public class ScoreValidator extends DisableableValidator<VaildScore, Score> {

  @Override
  public boolean isValidIfEnabled(Score value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return (value.getMax() == null || value.getMax() >= value.getRaw())
        && (value.getMin() == null || value.getMin() <= value.getRaw());
  }
}
