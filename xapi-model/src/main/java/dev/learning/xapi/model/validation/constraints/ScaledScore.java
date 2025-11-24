/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import dev.learning.xapi.model.validation.internal.validators.ScaledScoreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must be a valid scaled score.
 *
 * @author István Rátkai (Selindek)
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#details-13">xAPI Score
 *     details</a>
 */
@Documented
@Constraint(validatedBy = {ScaledScoreValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ScaledScore {

  /**
   * Error Message.
   *
   * @return the error message
   */
  String message() default "scaled score must be between -1 and 1";

  /**
   * Groups.
   *
   * @return the validation groups
   */
  Class<?>[] groups() default {};

  /**
   * Payload.
   *
   * @return the payload
   */
  Class<? extends Payload>[] payload() default {};
}
