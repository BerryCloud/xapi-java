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

import dev.learning.xapi.model.validation.internal.validators.MboxValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must be a valid <a href="http://xmlns.com/foaf/0.1/#term_mbox">Mbox</a>.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@Documented
@Constraint(validatedBy = {MboxValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Mbox {

  /**
   * Error Message.
   */
  String message() default "must be a valid mbox";

  /**
   * Groups.
   */
  Class<?>[] groups() default {};

  /**
   * Payload.
   */
  Class<? extends Payload>[] payload() default {};
}
