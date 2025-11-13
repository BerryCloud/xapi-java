/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import dev.learning.xapi.model.validation.internal.validators.StatementsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated Statement list must be valid.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@Constraint(validatedBy = {StatementsValidator.class})
@Target({METHOD, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Statements {

  /**
   * The default message.
   */
  String message() default "all statements must have a unique id";

  /**
   * Groups.
   */
  Class<?>[] groups() default {};

  /**
   * Payload.
   */
  Class<? extends Payload>[] payload() default {};
}
