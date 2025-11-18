/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.Mbox;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

/**
 * The String being validated must be a valid mbox.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="http://xmlns.com/foaf/0.1/#term_mbox">Mbox</a>
 */
public class MboxValidator extends DisableableValidator<Mbox, String> {

  public static final String PREFIX = "mailto:";

  /**
   * Cached email validator instance to avoid repeated object creation.
   */
  private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();

  @Override
  public void initialize(Mbox mbox) {
    // No initialization needed - using static validator
  }

  @Override
  public boolean isValidIfEnabled(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    return value.startsWith(PREFIX)
        && EMAIL_VALIDATOR.isValid(value.substring(PREFIX.length()), context);
  }

}
