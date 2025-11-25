/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.ResourceUtils;

/**
 * Result Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Result tests")
class ResultTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingResultThenResultIsInstanceOfResult() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Result Is Instance Of Result
    assertThat(result, instanceOf(Result.class));
  }

  @Test
  void whenDeserializingResultThenScoreIsInstanceOfScore() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Score Is Instance Of Score
    assertThat(result.getScore(), instanceOf(Score.class));
  }

  @Test
  void whenDeserializingResultThenSuccessIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Success Is Expected
    assertThat(result.getSuccess(), is(true));
  }

  @Test
  void whenDeserializingResultThenCompletionIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Completion Is Expected
    assertThat(result.getCompletion(), is(true));
  }

  @Test
  void whenDeserializingResultThenResponseIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Response Is Expected
    assertThat(result.getResponse(), is("test"));
  }

  @Test
  void whenDeserializingResultThenDurationIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Duration Is Expected
    assertThat(result.getDuration(), is("P1D"));
  }

  @Test
  void whenSerializingResultThenResultIsEqualToExpectedJson() throws IOException {

    final var resultInstance =
        Result.builder()
            .score(s -> s.max(5.0f).min(0.0f).raw(1.0f).scaled(1.0f))
            .completion(true)
            .success(true)
            .duration("P1D")
            .response("test")
            .build();

    // When Serializing Result
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(resultInstance));

    // Then Result Is Equal To Expected Json
    assertThat(
        result, is(objectMapper.readTree(ResourceUtils.getFile("classpath:result/result.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() {

    final var resultInstance =
        Result.builder()
            .score(s -> s.max(5.0f).min(0.0f).raw(1.0f).scaled(1.0f))
            .completion(true)
            .success(true)
            .duration("P1D")
            .response("test")
            .build();

    // When Calling ToString
    final var result = resultInstance.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is(
            "Result(score=Score(scaled=1.0, raw=1.0, min=0.0, max=5.0), success=true, completion=true, "
                + "response=test, duration=P1D, extensions=null)"));
  }

  // Duration Validation Tests

  @ParameterizedTest
  @ValueSource(
      strings = {
        // Week format
        "P1W",
        "P52W",
        "P104W",
        // Day format
        "P1D",
        "P365D",
        // Time format
        "PT1H",
        "PT30M",
        "PT45S",
        "PT1.5S",
        "PT0.5S",
        // Combined date format
        "P1Y",
        "P1M",
        "P1Y2M",
        "P1Y2M3D",
        // Combined date and time format
        "P1DT1H",
        "P1DT1H30M",
        "P1DT1H30M45S",
        "P1Y2M3DT4H5M6S",
        "P1Y2M3DT4H5M6.7S",
        // Minimal valid formats
        "PT0S",
        "PT1S",
        "P0D",
        // Real-world examples
        "PT8H",
        "P90D",
        "P2Y",
        "PT15M30S"
      })
  @DisplayName("When Duration Is Valid Then Validation Passes")
  void whenDurationIsValidThenValidationPasses(String duration) {

    // Given Result With Valid Duration
    final var result = Result.builder().duration(duration).build();

    // When Validating Result
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);

    // Then Validation Passes
    assertThat(violations, empty());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        // Invalid formats
        "",
        "T1H",
        "1D",
        "PD",
        "PT",
        "P1",
        "1Y2M",
        // Invalid time without T
        "P1H",
        "P1M30S",
        // Invalid mixing weeks with other units
        "P1W1D",
        "P1W1Y",
        "P1WT1H",
        // Invalid decimal placement
        "P1.5D",
        "P1.5Y",
        "PT1.5H",
        "PT1.5M",
        // Missing P prefix
        "1Y2M3D",
        "T1H30M",
        // Invalid order
        "P1D1Y",
        "PT1S1M",
        "PT1M1H",
        // Double separators
        "P1Y2M3DTT1H",
        "PP1D",
        // Negative values
        "P-1D",
        "PT-1H"
      })
  @DisplayName("When Duration Is Invalid Then Validation Fails")
  void whenDurationIsInvalidThenValidationFails(String duration) {

    // Given Result With Invalid Duration
    final var result = Result.builder().duration(duration).build();

    // When Validating Result
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);

    // Then Validation Fails
    assertThat(violations, not(empty()));
    assertThat(violations, hasSize(1));
  }

  @Test
  @DisplayName("When Duration Is Null Then Validation Passes")
  void whenDurationIsNullThenValidationPasses() {

    // Given Result With Null Duration
    final var result = Result.builder().duration(null).build();

    // When Validating Result
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);

    // Then Validation Passes
    assertThat(violations, empty());
  }

  @Test
  @DisplayName("When Duration Has Many Digits Then Validation Completes Quickly")
  void whenDurationHasManyDigitsThenValidationCompletesQuickly() {

    // Given Result With Long But Valid Duration
    final var result = Result.builder().duration("P99999Y99999M99999DT99999H99999M99999S").build();

    // When Validating Result
    final long startTime = System.nanoTime();
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);
    final long endTime = System.nanoTime();
    final long durationMs = (endTime - startTime) / 1_000_000;

    // Then Validation Passes And Completes In Reasonable Time
    assertThat(violations, empty());
    // Validation should complete quickly - 500ms is generous for a regex match
    assertThat("Validation should complete in less than 500ms", durationMs < 500);
  }

  @Test
  @DisplayName("When Duration Is Adversarial Input Then Validation Completes Quickly Without ReDoS")
  void whenDurationIsAdversarialInputThenValidationCompletesQuicklyWithoutReDoS() {

    // Given Result With Adversarial Input That Could Cause ReDoS
    // This input is designed to trigger exponential backtracking in vulnerable regex patterns
    final var adversarialInput = "P" + "9".repeat(50) + "!";
    final var result = Result.builder().duration(adversarialInput).build();

    // When Validating Result
    final long startTime = System.nanoTime();
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);
    final long endTime = System.nanoTime();
    final long durationMs = (endTime - startTime) / 1_000_000;

    // Then Validation Fails Quickly (not vulnerable to ReDoS)
    assertThat(violations, not(empty()));
    // Validation should complete quickly even with adversarial input - 500ms is generous
    assertThat(
        "Validation should complete in less than 500ms even with adversarial input",
        durationMs < 500);
  }

  @Test
  @DisplayName("When Duration Has Multiple Optional Groups Then Validation Completes Quickly")
  void whenDurationHasMultipleOptionalGroupsThenValidationCompletesQuickly() {

    // Given Result With Input That Tests Multiple Optional Groups
    // This tests the pattern with input that doesn't match but exercises optional groups
    final var testInput = "PYMDTHMS";
    final var result = Result.builder().duration(testInput).build();

    // When Validating Result
    final long startTime = System.nanoTime();
    final Set<ConstraintViolation<Result>> violations = validator.validate(result);
    final long endTime = System.nanoTime();
    final long durationMs = (endTime - startTime) / 1_000_000;

    // Then Validation Fails Quickly Without Excessive Backtracking
    assertThat(violations, not(empty()));
    // Validation should complete quickly - 500ms is generous for a regex match
    assertThat("Validation should complete in less than 500ms", durationMs < 500);
  }
}
