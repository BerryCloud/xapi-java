/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.jackson.model.strict.XapiTimestamp.XapiTimestampParseException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link InstantConverter}.
 *
 * @author Thomas Turrell-Croft
 */
class InstantConverterTest {

  private final InstantConverter converter = new InstantConverter();

  @Test
  @DisplayName("When Converting Timestamp With UTC Timezone Then Conversion Succeeds")
  void whenConvertingTimestampWithUtcTimezoneThenConversionSucceeds() {

    // When Converting Timestamp With UTC Timezone
    final var result = converter.convert("2017-03-01T12:30:00.000Z");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Positive Timezone Offset Then Conversion Succeeds")
  void whenConvertingTimestampWithPositiveTimezoneOffsetThenConversionSucceeds() {

    // When Converting Timestamp With Positive Timezone Offset
    final var result = converter.convert("2017-03-01T12:30:00.000+00");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Positive Timezone Offset With Colon Then Conversion Succeeds")
  void whenConvertingTimestampWithPositiveTimezoneOffsetWithColonThenConversionSucceeds() {

    // When Converting Timestamp With Positive Timezone Offset With Colon
    final var result = converter.convert("2017-03-01T12:30:00.000+00:00");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Positive Four Digit Timezone Offset Then Conversion Succeeds")
  void whenConvertingTimestampWithPositiveFourDigitTimezoneOffsetThenConversionSucceeds() {

    // When Converting Timestamp With Positive Four Digit Timezone Offset
    final var result = converter.convert("2017-03-01T12:30:00.000+0000");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Non-Zero Positive Offset Then Conversion Succeeds")
  void whenConvertingTimestampWithNonZeroPositiveOffsetThenConversionSucceeds() {

    // When Converting Timestamp With Non-Zero Positive Offset
    final var result = converter.convert("2017-03-01T12:30:00.000+01:00");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T11:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Non-Zero Negative Offset Then Conversion Succeeds")
  void whenConvertingTimestampWithNonZeroNegativeOffsetThenConversionSucceeds() {

    // When Converting Timestamp With Non-Zero Negative Offset
    final var result = converter.convert("2017-03-01T12:30:00.000-05:00");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T17:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp Without Timezone Then UTC Is Assumed")
  void whenConvertingTimestampWithoutTimezoneThenUtcIsAssumed() {

    // When Converting Timestamp Without Timezone
    final var result = converter.convert("2017-03-01T12:30:00.000");

    // Then UTC Is Assumed
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.000Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Negative Zero Offset Two Digit Then Exception Is Thrown")
  void whenConvertingTimestampWithNegativeZeroOffsetTwoDigitThenExceptionIsThrown() {

    // When Converting Timestamp With Negative Zero Offset Two Digit
    // Then Exception Is Thrown
    assertThrows(XapiTimestampParseException.class,
        () -> converter.convert("2017-03-01T12:30:00.000-00"));
  }

  @Test
  @DisplayName("When Converting Timestamp With Negative Zero Offset Four Digit Then Exception Is Thrown")
  void whenConvertingTimestampWithNegativeZeroOffsetFourDigitThenExceptionIsThrown() {

    // When Converting Timestamp With Negative Zero Offset Four Digit
    // Then Exception Is Thrown
    assertThrows(XapiTimestampParseException.class,
        () -> converter.convert("2017-03-01T12:30:00.000-0000"));
  }

  @Test
  @DisplayName("When Converting Timestamp With Negative Zero Offset With Colon Then Exception Is Thrown")
  void whenConvertingTimestampWithNegativeZeroOffsetWithColonThenExceptionIsThrown() {

    // When Converting Timestamp With Negative Zero Offset With Colon
    // Then Exception Is Thrown
    assertThrows(XapiTimestampParseException.class,
        () -> converter.convert("2017-03-01T12:30:00.000-00:00"));
  }

  @Test
  @DisplayName("When Converting Timestamp Without Milliseconds Then Conversion Succeeds")
  void whenConvertingTimestampWithoutMillisecondsThenConversionSucceeds() {

    // When Converting Timestamp Without Milliseconds
    final var result = converter.convert("2017-03-01T12:30:00Z");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Microseconds Then Conversion Succeeds")
  void whenConvertingTimestampWithMicrosecondsThenConversionSucceeds() {

    // When Converting Timestamp With Microseconds
    final var result = converter.convert("2017-03-01T12:30:00.123456Z");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.123456Z"))));
  }

  @Test
  @DisplayName("When Converting Timestamp With Nanoseconds Then Conversion Succeeds")
  void whenConvertingTimestampWithNanosecondsThenConversionSucceeds() {

    // When Converting Timestamp With Nanoseconds
    final var result = converter.convert("2017-03-01T12:30:00.123456789Z");

    // Then Conversion Succeeds
    assertThat(result, is(notNullValue()));
    assertThat(result, is(equalTo(Instant.parse("2017-03-01T12:30:00.123456789Z"))));
  }
}
