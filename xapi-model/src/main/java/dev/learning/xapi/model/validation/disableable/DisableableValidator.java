/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.disableable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import lombok.Setter;

/**
 * <p>
 * Abstract superclass for disableable validators.
 * </p>
 * Validators extending this class are enabled by default.
 * {@link ValidatorDisabler#DEFAULT_DISABLER} can be used for completely disable a validator, or a
 * custom {@link ValidatorDisabler} bean can be injected for enabling/disabling a validator based on
 * a custom logic.
 *
 * @author István Rátkai (Selindek)
 */
public abstract class DisableableValidator<A extends Annotation, T>
    implements ConstraintValidator<A, T> {

  @Setter
  private ValidatorDisabler disabler = ValidatorDisabler.DEFAULT_ENABLER;

  @Override
  public boolean isValid(T value, ConstraintValidatorContext context) {
    return isDisabled() || isValidIfEnabled(value, context);
  }

  /**
   * <p>
   * Convenient method for implementing the validation logic independently from the disabled/enabled
   * logic.
   * </p>
   * If some more complex validation logic is needed (eg. some partial validation is needed even if
   * the validator is disabled), then the {@link #isDisabled()} method can be used directly from the
   * {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} method.
   *
   * @param value object to validate
   * @param context context in which the constraint is evaluated
   *
   * @return {@code false} if {@code value} does not pass the constraint
   */
  protected boolean isValidIfEnabled(T value, ConstraintValidatorContext context) {
    return true;
  }

  /**
   * Whether this validator is disabled.
   */
  public boolean isDisabled() {
    return disabler.isDisabled(this);
  }

}
