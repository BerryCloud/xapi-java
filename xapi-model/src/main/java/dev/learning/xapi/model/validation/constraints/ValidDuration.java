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

import dev.learning.xapi.model.validation.internal.validators.DurationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must be a valid ISO 8601:2004 duration format.
 *
 * <p>Accepts formats like:
 *
 * <ul>
 *   <li>Week format: P1W, P52W
 *   <li>Day format: P1D, P365D
 *   <li>Time format: PT1H, PT30M, PT45S, PT1.5S
 *   <li>Combined format: P1Y2M3D, P1DT1H30M45S
 * </ul>
 *
 * @author Berry Cloud
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#result">xAPI
 *     Result</a>
 */
@Documented
@Constraint(validatedBy = {DurationValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ValidDuration {

  /**
   * Error Message.
   *
   * @return the error message
   */
  String message() default "Must be a valid ISO 8601:2004 duration format.";

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
