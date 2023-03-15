/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Context;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.Verb;
import dev.learning.xapi.model.validation.constraints.ValidStatement;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Statement being validated must be valid.
 */
public class ValidatorForStatement implements ConstraintValidator<ValidStatement, Statement> {

  @Override
  public boolean isValid(Statement value, ConstraintValidatorContext context) {

    if (value == null) {
      // Can not happen with annotation target set to type
      return true;
    }

    // TODO consider adding conversion logic here

    context.disableDefaultConstraintViolation();

    return isVoidingVerbValid(value, context) && isValidRevision(value, context)
        && isValidPlatform(value, context) && isValidAuthority(value, context);
  }

  private boolean isVoidingVerbValid(Statement value, ConstraintValidatorContext context) {

    Verb verb = value.getVerb();

    if (verb == null || !verb.isVoided()
        || (verb.isVoided() && value.getObject() instanceof StatementReference)) {
      return true;
    }

    context
        .buildConstraintViolationWithTemplate(
            "invalid voiding statement, object must be of type statement reference")
        .addConstraintViolation();

    return false;
  }

  private boolean isValidRevision(Statement value, ConstraintValidatorContext context) {

    Context c = value.getContext();

    if (c == null || c.getRevision() == null || value.getObject() instanceof Activity) {
      return true;
    }

    context.buildConstraintViolationWithTemplate(
        "invalid revision property, object must of type activity").addConstraintViolation();

    return false;
  }

  private boolean isValidPlatform(Statement value, ConstraintValidatorContext context) {

    Context c = value.getContext();

    if (c == null || c.getPlatform() == null || value.getObject() instanceof Activity) {
      return true;
    }

    context.buildConstraintViolationWithTemplate(
        "invalid platform property, object must be of type activity").addConstraintViolation();

    return false;
  }

  private boolean isValidAuthority(Statement value, ConstraintValidatorContext context) {

    // can be null or Agent
    if (value.getAuthority() == null || value.getAuthority() instanceof Agent) {
      return true;
    }

    final var group = (Group) value.getAuthority();
    // ... or must be an anonymous Group with exactly two members
    if (group.getAccount() == null && group.getMbox() == null && group.getMboxSha1sum() == null
        && group.getOpenid() == null && group.getMember() != null
        && group.getMember().size() == 2) {
      return true;

    }

    context.buildConstraintViolationWithTemplate("invalid authority property")
        .addConstraintViolation();

    return false;
  }

}
