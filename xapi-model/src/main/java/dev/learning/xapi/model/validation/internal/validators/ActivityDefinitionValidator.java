/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.ActivityDefinition;
import dev.learning.xapi.model.validation.constraints.ValidActivityDefinition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

/**
 * The {@link ActivityDefinition} being validated must be valid.
 *
 * @author István Rátkai (Selindek)
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#activity-definition">Activity
 *      Definition</a>
 */
public class ActivityDefinitionValidator
    implements ConstraintValidator<ValidActivityDefinition, ActivityDefinition> {

  @Value("#{!${xApi.model.validateActivityDefinition:true}}")
  private boolean disabled;

  @Override
  public boolean isValid(ActivityDefinition value, ConstraintValidatorContext context) {

    if (disabled || value == null) {
      return true;
    }

    return !(value.getInteractionType() == null && (value.getCorrectResponsesPattern() != null
        || value.getChoices() != null || value.getScale() != null || value.getSource() != null
        || value.getTarget() != null || value.getSteps() != null));

  }

}
