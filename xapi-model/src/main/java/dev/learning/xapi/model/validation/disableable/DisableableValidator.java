/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
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
   * Convenient method for completely removing the disabled/enabled logic from subclasses.
   * </p>
   * By adding the validator logic into this method a subclass can contain only the actual validator
   * logic. But if some more complex validation logic is needed (eg. some partial validation is
   * needed even if the validator is disabled), then the {@link #isDisabled()} method can be used
   * directly from the {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
   * method.
   */
  public boolean isValidIfEnabled(T value, ConstraintValidatorContext context) {
    return true;
  }

  /**
   * Whether this validator is disabled.
   */
  public boolean isDisabled() {
    return disabler.isDisabled(this);
  }

}
