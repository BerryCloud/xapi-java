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
 * StatementPlatformValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("StatementPlatformValidator tests")
class StatementPlatformValidatorTest {

  private static final StatementPlatformValidator validator = new StatementPlatformValidator();

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
  void whenValueHasNoContextPlatformThenResultIsTrue() {

    // When Value has No Context Platform
    var value = Statement.builder().context(Context.builder().build()).build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasContextPlatformButNoObjectThenResultIsFalse() {

    // When Value has Context Platform But No Object
    var value = Statement.builder().context(c->c.platform("platform")).build();

    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasContextPlatformAndInvalidObjectThenResultIsFalse() {

    // When Value has Context Platform And Invalid Object
    var value = Statement.builder().context(c->c.platform("platform"))

        .statementReferenceObject(sr->sr.id(UUID.randomUUID()))

        .build();

    var result = validator.isValid(value, null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasContextPlatformAndValidObjectThenResultIsTrue() {

    // When Value has Context Platform And Valid Object
    var value = Statement.builder().context(c->c.platform("platform"))

        .activityObject(a->a.id(URI.create("http://example.com/activity")))

        .build();

    var result = validator.isValid(value, null);

    // Then Result Is True
    assertTrue(result);
  }

}
