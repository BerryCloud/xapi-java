/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class VariantValidatorForUuidTest {

  private VariantValidatorForUuid constraintValidator = new VariantValidatorForUuid();

  @Test
  void whenIsValidIsCalledWithNullUUIDThenValidIsTrue() {

    // When IsValid Is Called With Null UUID
    boolean valid = constraintValidator.isValid(null, null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void whenIsValidIsCalledWithVersion4Variant2UUIDThenValidIsTrue() {

    // When IsValid Is Called With Version 4 Variant 2 UUID
    boolean valid =
        constraintValidator.isValid(UUID.fromString("3441c47b-c098-4245-b22e-db3d1242098f"), null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void whenIsValidIsCalledWithVersion4VariantMicrosoftGUIDUUIDThenResultIsFalse() {

    // When IsValid Is Called With Version 4 Variant Microsoft GUID UUID
    boolean valid =
        constraintValidator.isValid(UUID.fromString("4c30eeb1-15ac-4833-cb0d-88613ba65267"), null);

    // Then Valid Is False
    assertFalse(valid);
  }


  //
}
