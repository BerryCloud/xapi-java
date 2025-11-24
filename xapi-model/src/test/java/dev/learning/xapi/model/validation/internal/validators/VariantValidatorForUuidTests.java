/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.validation.constraints.Variant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.AnnotationUtils;

/**
 * VariantValidatorForUuid Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("VariantValidatorForUuidTest tests")
class VariantValidatorForUuidTests {

  private final VariantValidatorForUuid constraintValidator = new VariantValidatorForUuid();

  @Variant(2)
  private UUID variant2;

  @Variant(6)
  private UUID variant6;

  @Test
  void whenIsValidIsCalledWithNullUUIDThenValidIsTrue() {

    // When IsValid Is Called With Null UUID
    final var valid = constraintValidator.isValid(null, null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void GivenValidatorIsInitializedWith2WhenIsValidIsCalledWithVersion4Variant2UUIDThenValidIsTrue()
      throws Exception {

    // Given Validator Is Initialized With 2
    constraintValidator.initialize(AnnotationUtils
        .findAnnotation(getClass().getDeclaredField("variant2"), Variant.class).get());

    // When IsValid Is Called With Version 4 Variant 2 UUID
    final var valid =
        constraintValidator.isValid(UUID.fromString("3441c47b-c098-4245-b22e-db3d1242098f"), null);

    // Then Valid Is True
    assertTrue(valid);
  }

  @Test
  void GivenValidatorIsInitializedWith2whenIsValidIsCalledWithVersion4VariantMicrosoftGUIDUUIDThenResultIsFalse()
      throws Exception {

    // Given Validator Is Initialized With 2
    constraintValidator.initialize(AnnotationUtils
        .findAnnotation(getClass().getDeclaredField("variant2"), Variant.class).get());

    // When IsValid Is Called With Version 4 Variant Microsoft GUID UUID
    final var valid =
        constraintValidator.isValid(UUID.fromString("4c30eeb1-15ac-4833-cb0d-88613ba65267"), null);

    // Then Valid Is False
    assertFalse(valid);
  }


  @Test
  void GivenValidatorIsInitializedWith6whenIsValidIsCalledWithVersion4VariantMicrosoftGUIDUUIDThenResultIsTrue()
      throws Exception {

    // Given Validator Is Initialized With 6
    constraintValidator.initialize(AnnotationUtils
        .findAnnotation(getClass().getDeclaredField("variant6"), Variant.class).get());

    // When IsValid Is Called With Version 4 Variant Microsoft GUID UUID
    final var valid =
        constraintValidator.isValid(UUID.fromString("4c30eeb1-15ac-4833-cb0d-88613ba65267"), null);

    // Then Valid Is False
    assertTrue(valid);
  }

}
