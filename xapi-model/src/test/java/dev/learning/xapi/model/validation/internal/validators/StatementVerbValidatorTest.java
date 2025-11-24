/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StatementVerbValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("StatementVerbValidator tests")
class StatementVerbValidatorTest {

  private static final StatementVerbValidator validator = new StatementVerbValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    final var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasNoVerbThenResultIsTrue() {

    // When Value has No Verb
    final var value = Statement.builder().build();

    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasNonVoidedVerbThenResultIsTrue() {

    // When Value has Non-Voided Verb
    final var value = Statement.builder().verb(Verb.ANSWERED).build();

    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasVoidedVerbButNoObjectThenResultIsFalse() {

    // When Value has Voided Verb But No Object
    final var value = Statement.builder().verb(Verb.VOIDED).build();

    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasVoidedVerbAndInvalidObjectThenResultIsFalse() {

    // When Value has Voided Verb And Invalid Object
    final var value = Statement.builder().verb(Verb.VOIDED)

        .activityObject(a -> a.id(URI.create("http://example.com/activity")))

        .build();

    final var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasVoidedVerbAndValidObjectThenResultIsTrue() {

    // When Value has Voided Verb And Valid Object
    final var value = Statement.builder().verb(Verb.VOIDED)

        .statementReferenceObject(sr -> sr.id(UUID.randomUUID()))

        .build();

    final var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }
}
