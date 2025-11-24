/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import tools.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * StatementReference Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("StatementReference tests")
class StatementReferenceTest {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingStatementReferenceThenResultIsInstanceOfStatementReference()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:statement_reference/statement_reference.json");

    // When Deserializing StatementReference
    final var result = objectMapper.readValue(file, StatementReference.class);

    // Then Result Is Instance Of StatementReference
    assertThat(result, instanceOf(StatementReference.class));
  }

  @Test
  void whenDeserializingStatementReferenceThenIdIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:statement_reference/statement_reference.json");

    // When Deserializing StatementReference
    final var result = objectMapper.readValue(file, StatementReference.class);

    // Then Id Is Expected
    assertThat(result.getId(), is(UUID.fromString("099bbde8-780e-483f-8181-128393db0f53")));
  }

  @Test
  void whenSerializingContextThenResultIsEqualToExpectedJson() throws IOException {

    final var statementReference =
        StatementReference.builder()
            .id(UUID.fromString("099bbde8-780e-483f-8181-128393db0f53"))
            .build();

    // When Serializing Context
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(statementReference));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(
            objectMapper.readTree(
                ResourceUtils.getFile("classpath:statement_reference/statement_reference.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final var statementReference =
        objectMapper.readValue(
            ResourceUtils.getFile("classpath:statement_reference/statement_reference.json"),
            StatementReference.class);

    // When Calling ToString
    final var result = statementReference.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is("StatementReference(objectType=StatementRef, id=099bbde8-780e-483f-8181-128393db0f53)"));
  }

  @Test
  void
      whenValidatingStatementReferenceWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final var statementReference =
        StatementReference.builder()
            .id(UUID.fromString("099bbde8-780e-483f-8181-128393db0f53"))
            .build();

    // When Validating Statement Reference With All Required Properties
    final Set<ConstraintViolation<StatementReference>> constraintViolations =
        validator.validate(statementReference);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));
  }

  @Test
  void whenStatementReferenceIsBuiltWithoutIdThenNullPointerIsThrown() {

    final var statementReference = StatementReference.builder().build();

    // When Validating Statement Reference Without Id
    final Set<ConstraintViolation<StatementReference>> constraintViolations =
        validator.validate(statementReference);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));
  }
}
