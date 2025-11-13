/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.validation.constraints.ValidAuthority;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The Actor being validated must be null, an agent or an anonymous group with two agents.
 *
 * @author István Rátkai (Selindek)
 * @author Thomas Turrell-Croft
 */
public class AuthorityValidator extends DisableableValidator<ValidAuthority, Actor> {

  @Override
  public boolean isValidIfEnabled(Actor value, ConstraintValidatorContext context) {

    // can be null or Agent
    if (value == null || value instanceof Agent) {
      return true;
    }

    final var group = (Group) value;
    // ... or must be an anonymous Group with exactly two members
    return group.isAnonymous() && group.getMember().size() == 2;

  }

}
