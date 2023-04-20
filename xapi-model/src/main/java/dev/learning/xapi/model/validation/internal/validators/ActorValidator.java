/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.StatementObject;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.disableable.DisableableValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The {@link StatementObject} being validated must be valid.
 *
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#actor">Actor</a>
 */
public class ActorValidator extends DisableableValidator<ValidActor, Object> {

  /**
   * Checks if this {@link Actor} contains exactly one identifier.
   *
   * @return true if this object is valid.
   */
  @Override
  public boolean isValidIfEnabled(Object value, ConstraintValidatorContext context) {

    if (value instanceof final Group group) {
      return group.getAccount() == null && group.getMbox() == null && group.getMboxSha1sum() == null
          && group.getOpenid() == null ? group.getMember() != null && !group.getMember().isEmpty()
              : hasSingleIdentifier(group);
    } else if (value instanceof final Agent agent) {
      return hasSingleIdentifier(agent);
    }

    return true;

  }

  private boolean hasSingleIdentifier(Actor value) {

    var n = 0;

    if (value.getMbox() != null) {
      n++;
    }
    if (value.getMboxSha1sum() != null) {
      n++;
    }
    if (value.getOpenid() != null) {
      n++;
    }
    if (value.getAccount() != null) {
      n++;
    }

    return n == 1;
  }

}
