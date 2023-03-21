/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.Context;
import dev.learning.xapi.model.Statement;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StatementRevisionValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("StatementRevisionValidator tests")
class StatementRevisionValidatorTest {

  private static final StatementRevisionValidator validator = new StatementRevisionValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasNoContextThenResultIsTrue() {

    // When Value has No Context
    var value = Statement.builder().build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasNoContextRevisionThenResultIsTrue() {

    // When Value has No Context Revision
    var value = Statement.builder().context(Context.builder().build()).build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasContextRevisionButNoObjectThenResultIsFalse() {

    // When Value has Context Revision But No Object
    var value = Statement.builder().context(c->c.revision("revision")).build();

    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasContextRevisionAndInvalidObjectThenResultIsFalse() {

    // When Value has Context Revision And Invalid Object
    var value = Statement.builder().context(c->c.revision("revision"))

        .statementReferenceObject(sr->sr.id(UUID.randomUUID()))

        .build();

    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasContextRevisionAndValidObjectThenResultIsTrue() {

    // When Value has Context Revision And Valid Object
    var value = Statement.builder().context(c->c.revision("revision"))

        .activityObject(a->a.id(URI.create("http://example.com/activity")))

        .build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

}
