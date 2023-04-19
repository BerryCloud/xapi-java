/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.Mbox;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Value;

/**
 * The String being validated must be a valid mbox.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="http://xmlns.com/foaf/0.1/#term_mbox">Mbox</a>
 */
public class MboxValidator implements ConstraintValidator<Mbox, String> {

  public static final String PREFIX = "mailto:";

  @Value("#{!${xApi.model.validateMbox:true}}")
  private boolean disabled;

  EmailValidator emailValidator;

  @Override
  public void initialize(Mbox mbox) {

    emailValidator = new EmailValidator();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (disabled || value == null) {
      return true;
    }

    return value.startsWith(PREFIX)
        && emailValidator.isValid(value.substring(PREFIX.length()), context);
  }

}
