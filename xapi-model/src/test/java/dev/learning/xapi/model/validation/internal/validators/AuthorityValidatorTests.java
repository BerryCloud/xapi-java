/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import org.junit.jupiter.api.Test;

class AuthorityValidatorTests {

  private final AuthorityValidator constraintValidator = new AuthorityValidator();

  @Test
  void whenCallingIsValidWithNullThenResultIsTrue() {

    // When Calling Is Valid With Null
    final var result = constraintValidator.isValid(null, null);

    // Then Result Is True
    assertThat(result, is(true));
  }

  @Test
  void whenCallingIsValidWithAgentThenResultIsTrue() {

    final Agent agent =
        Agent.builder().name("A N Other").mbox("mailto:another@example.com").build();

    // When Calling Is Valid With Agent
    final var result = constraintValidator.isValid(agent, null);

    // Then Result Is True
    assertThat(result, is(true));
  }

  @Test
  void whenCallingIsValidWithIdentifiedGroupThenResultIsFalse() {

    final Group group =
        Group.builder().name("A N Other").mbox("mailto:another@example.com").build();

    // When Calling Is Valid With Identified Group
    final var result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithIdentifiedGroupWithMembersThenResultIsFalse() {

    final Group group = Group.builder().name("A N Other").mbox("mailto:another@example.com")
        .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling Is Valid With Identified Group
    final var result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithGroupWithOneMemberThenResultIsFalse() {

    final Group group = Group.builder()
        .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling Is Valid With Group With One Member
    final var result = constraintValidator.isValid(group, null);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  void whenCallingIsValidWithGroupWithTwoMembersThenResultIsTrue() {

    final Group group =
        Group.builder().addMember(m -> m.name("A N Other").mbox("mailto:another@example.com"))
            .addMember(m -> m.name("A N Other").mbox("mailto:another@example.com")).build();

    // When Calling IsValid With Group With Two Members
    final var result = constraintValidator.isValid(group, null);

    // Then Result Is True
    assertThat(result, is(true));

  }

}
