/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Score;
import dev.learning.xapi.model.validation.constraints.VaildScore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

/**
 * The raw score must be greater or equal to min and less or equal to max.
 *
 * @author István Rátkai (Selindek)
 */
public class ScoreValidator implements ConstraintValidator<VaildScore, Score> {

  @Value("#{!${xApi.model.validateScore:true}}")
  private boolean disabled;

  @Override
  public boolean isValid(Score value, ConstraintValidatorContext context) {

    if (disabled || value == null) {
      return true;
    }

    return (value.getMax() == null || value.getMax() >= value.getRaw())
        && (value.getMin() == null || value.getMin() <= value.getRaw());
  }

}
