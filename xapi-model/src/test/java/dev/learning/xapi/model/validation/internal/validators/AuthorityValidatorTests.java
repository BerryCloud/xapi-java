/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import org.junit.jupiter.api.Test;

class AuthorityValidatorTests {

  private AuthorityValidator constraintValidator = new AuthorityValidator();

  @Test
  void whenCallingIsValidWithNullThenResultIsTrue() {

    // When Calling Is Valid With Null
    boolean result = constraintValidator.isValid(null, null);

    // Then Result Is True
    assertThat(result, is(true));
  }

  @Test
  void whenCallingIsValidWithAgentThenResultIsTrue() {

    Agent agent = Agent.builder().name("A N Other").mbox("mailto:another@example.com").build();

    // When Calling Is Valid With Agent
    boolean result = constraintValidator.isValid(agent, null);

    // Then Result Is True
    assertThat(result, is(true));
  }

  @Test
  void whenCallingIsValidWithIdentifiedGroupThenResultIsFalse() {

    Group group = Group.builder().name("A N Other").mbox("mailto:another@example.com").build();

    // When Calling Is Valid With Identified Group
    boolean result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithIdentifiedGroupWithMembersThenResultIsFalse() {

    Group group = Group.builder().name("A N Other").mbox("mailto:another@example.com")
        .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling Is Valid With Identified Group
    boolean result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithGroupWithOneMemberThenResultIsFalse() {

    Group group = Group.builder()
        .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling Is Valid With Group With One Member
    boolean result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithGroupWithTwoMembersThenResultIsTrue() {

    Group group =
        Group.builder().addMember(m -> m.name("A N Other").mbox("mailto:another@example.com"))
            .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling IsValid With Group With Two Members
    boolean result = constraintValidator.isValid(group, null);

    // Then Result Is True
    assertThat(result, is(true));

  }

}
