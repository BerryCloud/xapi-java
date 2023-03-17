/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.ScaledScore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Float being validated must be a valid scaled score.
 *
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#details-13">xAPI Score details</a>
 */
public class ScaledScoreValidator implements ConstraintValidator<ScaledScore, Float> {

  @Override
  public boolean isValid(Float value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    return value >= -1F && value <= 1F;
  }

}
